package com.nortal.treasurehunt.service.impl;

import com.nortal.treasurehunt.model.rest.Currentassignment;
import com.nortal.treasurehunt.model.rest.SubmitSolution;
import com.nortal.treasurehunt.model.rest.Team;
import com.nortal.treasurehunt.model.rest.TeamAssignment;
import com.nortal.treasurehunt.model.rest.TeamAssignment.AssignmentStatus;
import com.nortal.treasurehunt.model.rest.TeamCurrentState;
import com.nortal.treasurehunt.model.rest.TeamRegistration;
import com.nortal.treasurehunt.service.TreasureHuntService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TreasureHuntServiceImpl implements TreasureHuntService {

  @Override
  public TeamCurrentState registerTeam(Long gameId, TeamRegistration teamRegistration) {
    TeamCurrentState currentState = fillDummyState(null);
    currentState.addSuccess("Team " + teamRegistration.getName() + " added, have fun!");
    return currentState;
  }

  protected TeamCurrentState fillDummyState(Long teamId) {
    TeamCurrentState currentState = new TeamCurrentState();
    currentState.setId(teamId == null ? 456L : teamId);
    currentState.setChallengesCompleted(2L);
    currentState.setChallengesTotal(8L);
    currentState.setCurrentassignment(new Currentassignment("Riddle number four\n how much do you weigh?"));
    currentState.setGameEnded(false);
    return currentState;
  }

  @Override
  public TeamCurrentState getCurrentState(Long gameId, Long teamId) {
    return fillDummyState(teamId);
  }

  @Override
  public TeamCurrentState getNewAssignment(Long gameId, Long teamId) {
    TeamCurrentState currentState = fillDummyState(teamId);
    currentState.setCurrentassignment(null);
    currentState.addWarning("No assignment available!\nGo and have a coffe meanwhile.");
    return currentState;
  }

  @Override
  public TeamCurrentState submitSolution(Long gameId, Long teamId, SubmitSolution submitSolution) {
    TeamCurrentState currentState = fillDummyState(teamId);
    currentState.addError("Incorrect answer: " + submitSolution.getSolution());
    return currentState;
  }

  @Override
  public List<Team> getGameData(Long gameId) {
    List<Team> teams = new ArrayList<Team>();
    for(int i = 0; i < 6; i++) {
      Team team = new Team();
      team.setName("Team nr " + (i + 1));
      long completed = 0;
      for(int j = 0; j < 8; j++) {
        TeamAssignment assignment = new TeamAssignment();
        if(i == j) {
          assignment.setStatus(AssignmentStatus.CURRENT);
          assignment.setStartTime(new Date(System.currentTimeMillis() - 30000));
          assignment.setTries(Long.valueOf((i + j) % 3));
        }
        else if((i + j) % 3 == 0) {
          assignment.setStatus(AssignmentStatus.COMPLETED);
          assignment.setStartTime(new Date(System.currentTimeMillis() - 600000));
          assignment.setEndTime(new Date(System.currentTimeMillis() - 400000));
          assignment.setTries(Long.valueOf((i + j) % 3) + 1);
          completed++;
        }
        team.addAssignment(assignment);
      }
      team.setChallengesCompleted(completed);
      teams.add(team);
    }
    return teams;
  }

}
