package com.nortal.treasurehunt.service.impl;

import com.nortal.treasurehunt.dao.TreasureHuntDAO;
import com.nortal.treasurehunt.exception.InputParameterValidationException;
import com.nortal.treasurehunt.model.Assignment;
import com.nortal.treasurehunt.model.rest.Messages;
import com.nortal.treasurehunt.model.rest.SubmitSolution;
import com.nortal.treasurehunt.model.rest.Team;
import com.nortal.treasurehunt.model.rest.TeamCurrentState;
import com.nortal.treasurehunt.model.rest.TeamRegistration;
import com.nortal.treasurehunt.service.TreasureHuntService;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TreasureHuntServiceImpl implements TreasureHuntService {

  @Resource
  private TreasureHuntDAO treasureHuntDAO;

  @Override
  public TeamCurrentState registerTeam(Long gameId,
      TeamRegistration teamRegistration) {
    try {
      checkGame(gameId);
    } catch (InputParameterValidationException e) {
      return e.getTeamCurrentState();
    }

    if (teamRegistration == null
        || StringUtils.isEmpty(teamRegistration.getName())) {
      return new TeamCurrentState.Builder().messages(
          new Messages().addError("Missing team name!")).build();
    }
    Long teamId = treasureHuntDAO.getTeamId(gameId, teamRegistration.getName());
    if (teamId == null) {
      teamId = treasureHuntDAO.createTeam(teamRegistration.getName(), gameId);
    }
    return getTeamCurrentState(teamId);
  }

  private void checkGame(Long gameId) throws InputParameterValidationException {
    if (!treasureHuntDAO.gameExists(gameId)) {
      throw new InputParameterValidationException(new TeamCurrentState.Builder().messages(
          new Messages().addError("Invalid game identifier!")).build());
    }
  }
  private TeamCurrentState getTeamCurrentState(Long teamId) {
    return treasureHuntDAO.getTeamCurrentState(teamId);
  }

  @Override
  public TeamCurrentState getCurrentState(Long gameId, Long teamId) {
    try {
      checkGame(gameId);
      checkTeam(gameId, teamId);
    } catch (InputParameterValidationException e) {
      return e.getTeamCurrentState();
    }
    return getTeamCurrentState(teamId);
  }

  private void checkTeam(Long gameId, Long teamId) throws InputParameterValidationException {
    if (!treasureHuntDAO.teamExists(gameId, teamId)) {
      throw new InputParameterValidationException(new TeamCurrentState.Builder().messages(
          new Messages().addError("Invalid team identifier!")).build());
    }
  }

  @Override
  public TeamCurrentState assignNewAssignment(Long gameId, Long teamId) {
    try {
      checkGame(gameId);
      checkTeam(gameId, teamId);
    } catch (InputParameterValidationException e) {
      return e.getTeamCurrentState();
    }

    if(treasureHuntDAO.hasCurrentAssignment(teamId)) {
      TeamCurrentState currentState = getTeamCurrentState(teamId);
      currentState.addError("Existing assignment in progress!");
      return currentState;
    }

    List<Long> freeAssignmentIds = treasureHuntDAO.getAvailableAssignmentIds(gameId, teamId);
    if(freeAssignmentIds.isEmpty()) {
      TeamCurrentState currentState = getTeamCurrentState(teamId);
      currentState.addWarning("No free assignments available at the moment!");
      return currentState;
    }

    boolean assigned = treasureHuntDAO.assignChallenge(teamId, freeAssignmentIds.get(0));
    TeamCurrentState currentState = getTeamCurrentState(teamId);
    if(!assigned) {
      currentState.addWarning("No free assignments available at the moment!");
    }
    return currentState;
  }

  @Override
  public TeamCurrentState submitSolution(Long gameId, Long teamId,
      SubmitSolution submitSolution) {

    try {
      checkGame(gameId);
      checkTeam(gameId, teamId);
    } catch (InputParameterValidationException e) {
      return e.getTeamCurrentState();
    }

    if(!treasureHuntDAO.hasCurrentAssignment(teamId)) {
      TeamCurrentState currentState = getTeamCurrentState(teamId);
      currentState.addError("No assignment in progress!");
      return currentState;
    }

    if(submitSolution == null || StringUtils.isEmpty(submitSolution.getSolution())) {
      TeamCurrentState currentState = treasureHuntDAO.getTeamCurrentState(teamId);
      currentState.addError("No solution provided!");
      return currentState;
    }

    Assignment assignment = treasureHuntDAO.getCurrentAssignment(teamId);
    treasureHuntDAO.increaseAssignmentSubmitCount(assignment.getId());

    if(assignment.getSolution().toUpperCase().equals(submitSolution.getSolution().toUpperCase())) {
      treasureHuntDAO.finishAssignment(assignment.getId());
      TeamCurrentState currentState = treasureHuntDAO.getTeamCurrentState(teamId);
      currentState.addSuccess(assignment.getCorrectText());
      return currentState;
    }
    TeamCurrentState currentState = treasureHuntDAO.getTeamCurrentState(teamId);
    currentState.addError(assignment.getWrongText());
    return currentState;
  }

  @Override
  public Collection<Team> getGameData(Long gameId) {
    return treasureHuntDAO.getGameData(gameId);
  }

}
