package com.nortal.treasurehunt.exception;

import com.nortal.treasurehunt.model.rest.TeamCurrentState;

public class InputParameterValidationException extends Exception {

  private TeamCurrentState teamCurrentState;

  public InputParameterValidationException(TeamCurrentState teamCurrentState) {
    this.teamCurrentState = teamCurrentState;
  }

  public TeamCurrentState getTeamCurrentState() {
    return teamCurrentState;
  }

  public void setTeamCurrentState(TeamCurrentState teamCurrentState) {
    this.teamCurrentState = teamCurrentState;
  }
}
