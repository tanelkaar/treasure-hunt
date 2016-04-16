package com.nortal.treasurehunt.service;

import com.nortal.treasurehunt.model.rest.SubmitSolution;
import com.nortal.treasurehunt.model.rest.Team;
import com.nortal.treasurehunt.model.rest.TeamCurrentState;
import com.nortal.treasurehunt.model.rest.TeamRegistration;
import java.util.List;

public interface TreasureHuntService {

  /**
   * Registers new team or retuns state of an existing team
   */
  public TeamCurrentState registerTeam(Long gameId, TeamRegistration teamRegistration);

  /**
   * Get current state of specified team
   */
  public TeamCurrentState getCurrentState(Long gameId, Long teamId);

  /**
   * Request new assignment
   */
  public TeamCurrentState getNewAssignment(Long gameId, Long teamId);

  /**
   * Submit solution for current assignment
   */
  public TeamCurrentState submitSolution(Long gameId, Long teamId, SubmitSolution submitSolution);

  /**
   * Get game data to display on dashboard
   */
  public List<Team> getGameData(Long gameId);
}
