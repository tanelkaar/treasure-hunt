package com.nortal.treasurehunt.dao;

import com.nortal.treasurehunt.model.Assignment;
import com.nortal.treasurehunt.model.rest.Currentassignment;
import com.nortal.treasurehunt.model.rest.Team;
import com.nortal.treasurehunt.model.rest.TeamAssignment;
import com.nortal.treasurehunt.model.rest.TeamCurrentState;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Tanel Käär (Tanel.Kaar@nortal.com)
 */
@Repository
public class TreasureHuntDAO extends JdbcDaoSupport {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostConstruct
  public void init() {
    setJdbcTemplate(jdbcTemplate);
  }

  public boolean gameExists(Long gameId) {
    String sql = "SELECT count(*) FROM game WHERE id = ?";
    return getJdbcTemplate().queryForObject(sql, Long.class, gameId) == 1;
  }

  public boolean teamExists(Long gameId, Long teamId) {
    String sql = "SELECT count(*) FROM team WHERE id = ? AND game_id = ?";
    return getJdbcTemplate().queryForObject(sql, Long.class, teamId, gameId) == 1;
  }

  public Long createTeam(String teamName, Long gameId) {
    Long id = getJdbcTemplate().queryForObject("call NEXT VALUE FOR s_team",
        Long.class);
    String sql =
        "INSERT INTO TEAM(ID, GAME_ID, NAME, START_TIME) "
            + "VALUES(?, ?, ?, current_timestamp)";
    getJdbcTemplate().update(sql, id, gameId, teamName);
    return id;
  }

  public Long getTeamId(Long gameId, String teamName) {
    String sql = "SELECT id FROM team WHERE UPPER(name) = UPPER(?) AND game_id = ?";
    try {
      return getJdbcTemplate()
          .queryForObject(sql, Long.class, teamName, gameId);
    } catch (IncorrectResultSizeDataAccessException e) {
      if (e.getActualSize() == 0) {
        return null;
      }
      throw e;
    }
  }

  public TeamCurrentState getTeamCurrentState(Long teamId) {
    String sql =
        "SELECT t.id, c.TEXT AS current_assignment_text, "
            + "(SELECT count(*) FROM ASSIGNMENT am WHERE am.TEAM_ID = t.id AND am.END_TIME IS NOT null) AS challenges_completed, "
            + "(SELECT count(*) FROM challenge ch WHERE ch.game_id = t.game_id) AS challenges_total "
            + "FROM team t "
            + "LEFT OUTER JOIN ASSIGNMENT a ON a.TEAM_ID = t.id AND a.END_TIME IS NULL "
            + "LEFT OUTER JOIN challenge c ON c.ID = a.CHALLENGE_ID "
            + "WHERE t.id = ?";
    return getJdbcTemplate().queryForObject(sql,
        new RowMapper<TeamCurrentState>() {

          @Override
          public TeamCurrentState mapRow(ResultSet rs, int rowNum)
              throws SQLException {
            String assignmentText = rs.getString("current_assignment_text");
            Currentassignment assignment = assignmentText == null ? null
                : new Currentassignment(assignmentText);

            return new TeamCurrentState.Builder().id(rs.getLong("id"))
                .challengesCompleted(rs.getLong("challenges_completed"))
                .challengesTotal(rs.getLong("challenges_total"))
                .currentassignment(assignment).build();
          }
        }, teamId);
  }

  public boolean hasCurrentAssignment(Long teamId) {
    String sql = "SELECT count(*) FROM assignment WHERE team_id = ? AND end_time is null";
      return getJdbcTemplate().queryForObject(sql, Long.class, teamId) > 0;
   }

  public List<Long> getAvailableAssignmentIds(Long gameId, Long teamId) {
    String sql =
        "SELECT c.id "
            + "FROM challenge c "
            // not in progress
            + "WHERE NOT exists(SELECT 1 FROM ASSIGNMENT a WHERE a.CHALLENGE_ID = c.id AND a.END_TIME IS null) "
            // has hot been completed by this team
            + "AND NOT EXISTS (SELECT 1 FROM ASSIGNMENT a WHERE a.CHALLENGE_ID = c.id AND a.END_TIME IS NOT NULL AND a.TEAM_ID = ?) "
            + "AND c.GAME_ID = ? "
            // prefer those with least completion count
            + "ORDER BY (SELECT count(*) FROM ASSIGNMENT a WHERE a.CHALLENGE_ID = c.id AND a.END_TIME IS NOT null)";
    return getJdbcTemplate().queryForList(sql, Long.class, teamId, gameId);
  }

  public boolean assignChallenge(Long teamId, Long challengeId) {
    String sql =
        "INSERT INTO ASSIGNMENT (ID, TEAM_ID, CHALLENGE_ID, SUBMIT_COUNT, START_TIME) "
            + "SELECT NEXT VALUE FOR s_assignment, ?, ?, 0, CURRENT_TIMESTAMP "
            + "FROM team t "
            + "WHERE t.id = ?"
            // not assigned to someone else meanwhile
            + "AND NOT EXISTS (SELECT 1 FROM assignment a WHERE a.end_time IS NULL AND a.challenge_id = ?)";
    return getJdbcTemplate().update(sql, teamId, challengeId, teamId, challengeId) == 1;
  }

  public Assignment getCurrentAssignment(Long teamId) {
    String sql =
        "SELECT a.id, c.SOLUTION, "
            + "coalesce(c.CORRECT_SOLUTION_TEXT, g.CORRECT_SOLUTION_DEFAULT_TEXT) correct_text, "
            + "coalesce(c.WRONG_SOLUTION_TEXT, g.WRONG_SOLUTION_DEFAULT_TEXT) wrong_text "
            + "FROM ASSIGNMENT a "
            + "INNER JOIN challenge c ON a.CHALLENGE_ID = c.id "
            + "INNER JOIN game g ON g.id = c.GAME_ID "
            + "WHERE a.TEAM_ID = ?"
            + "AND a.END_TIME IS null";
    return getJdbcTemplate().queryForObject(sql, new RowMapper<Assignment>() {

      @Override
      public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Assignment.Builder()
          .id(rs.getLong("id"))
          .solution(rs.getString("solution"))
          .correctText(rs.getString("correct_text"))
          .wrongText(rs.getString("wrong_text")).build();
      }

    }, teamId);
  }

  public void increaseAssignmentSubmitCount(Long assignmentId) {
    String sql = "UPDATE assignment SET submit_count = submit_count + 1 WHERE id = ?";
    getJdbcTemplate().update(sql, assignmentId);
  }

  public void finishAssignment(Long assignmentId) {
    String sql = "UPDATE assignment SET end_time = CURRENT_TIMESTAMP WHERE id = ?";
    getJdbcTemplate().update(sql, assignmentId);
  }

  public Collection<Team> getGameData(Long gameId) {
    String sql =
        "SELECT t.id, t.name, a.START_TIME, a.END_TIME, a.SUBMIT_COUNT "
        + "FROM team t "
        + "INNER JOIN challenge c ON t.GAME_ID = c.GAME_ID "
        + "LEFT OUTER JOIN ASSIGNMENT a ON a.TEAM_ID = t.id AND a.CHALLENGE_ID = c.id  "
        + "WHERE t.GAME_ID = ? "
        + "ORDER BY t.id, c.id";
    return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<Team>>() {

      @Override
      public Collection<Team> extractData(ResultSet rs) throws SQLException,
          DataAccessException {
        LinkedHashMap<Long, Team> teams = new LinkedHashMap<>();
        while(rs.next()) {
          Long teamId = rs.getLong("id");
          Team team = teams.get(teamId);
          if(team == null) {
            team = new Team();
            team.setName(rs.getString("name"));
            teams.put(teamId, team);
          }
          TeamAssignment assignment = new TeamAssignment();
          assignment.setStartTime(rs.getTimestamp("START_TIME"));
          assignment.setEndTime(rs.getTimestamp("END_TIME"));
          assignment.setTries(getLong(rs, "SUBMIT_COUNT"));
          team.addAssignment(assignment);
        }
        return teams.values();
      }}, gameId);
  }

  public static Long getLong(ResultSet rs, String columnName) throws SQLException {
    Integer i = rs.getObject(columnName, Integer.class);
    if(i == null) {
      return null;
    }
    return i.longValue();
  }
}
