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
import java.util.Locale;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TreasureHuntServiceImpl implements TreasureHuntService, InitializingBean {

  @Resource
  private TreasureHuntDAO treasureHuntDAO;
  @Resource
  private MessageSource messageSource;
  @Resource(name="defaultLocale")
  private String defaultLocale;
  private Locale locale;

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
          new Messages().addError(getMessage("error.missing_team_name"))).build();
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
          new Messages().addError(getMessage("error.invalid_game_identifier", gameId))).build());
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
          new Messages().addError(getMessage("error.invalid_team_identifier", teamId))).build());
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
      currentState.addError(getMessage("error.existing_assignment_in_progress"));
      return currentState;
    }

    List<Long> freeAssignmentIds = treasureHuntDAO.getAvailableAssignmentIds(gameId, teamId);
    if(freeAssignmentIds.isEmpty()) {
      TeamCurrentState currentState = getTeamCurrentState(teamId);
      currentState.addWarning(getMessage("warning.no_free_assignment_available"));
      return currentState;
    }

    boolean assigned = treasureHuntDAO.assignChallenge(teamId, freeAssignmentIds.get(0));
    TeamCurrentState currentState = getTeamCurrentState(teamId);
    if(!assigned) {
      currentState.addWarning(getMessage("warning.no_free_assignment_available"));
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
      currentState.addError(getMessage("error.no_assignment_in_progress"));
      return currentState;
    }

    if(submitSolution == null || StringUtils.isEmpty(submitSolution.getSolution())) {
      TeamCurrentState currentState = treasureHuntDAO.getTeamCurrentState(teamId);
      currentState.addError(getMessage("error.no_solution_provided"));
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

  private String getMessage(String code, Object ... args) {
    return messageSource.getMessage(code, args, locale);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.locale = StringUtils.parseLocaleString(defaultLocale);
  }
}
