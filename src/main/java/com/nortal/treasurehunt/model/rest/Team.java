package com.nortal.treasurehunt.model.rest;

import java.util.ArrayList;
import java.util.List;

public class Team {

  private String name;
  private long challengesTotal = 0L;
  private long challengesCompleted = 0L;
  private boolean finished;
  private boolean waiting = true;
  private List<TeamAssignment> assignments;
  private String timeInGame = "00:00:00";
  private long secondsInGame = 0;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getChallengesCompleted() {
    return challengesCompleted;
  }

  public void setChallengesCompleted(long challengesCompleted) {
    this.challengesCompleted = challengesCompleted;
  }

  public List<TeamAssignment> getAssignments() {
    return assignments;
  }

  public Team addAssignment(TeamAssignment assignment) {
    this.challengesTotal++;
    if (this.assignments == null) {
      assignments = new ArrayList<TeamAssignment>();
    }
    assignments.add(assignment);
    if (assignment.getEndTime() != null) {
      challengesCompleted++;
    }
    if (assignment.getStartTime() != null && assignment.getEndTime() == null) {
      waiting = false;
    }
    finished = challengesTotal == challengesCompleted;
    secondsInGame += assignment.getSecondsInGame();
    recalculateTimeInGame();
    return this;
  }

  private void recalculateTimeInGame() {
    int hours = (int) secondsInGame / 3600;
    int minutes = (int) (secondsInGame - hours * 3600) / 60;
    int seconds = (int) secondsInGame % 60;
    timeInGame = String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public void setAssignments(List<TeamAssignment> assignments) {
    this.assignments = assignments;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public long getChallengesTotal() {
    return challengesTotal;
  }

  public void setChallengesTotal(long challengesTotal) {
    this.challengesTotal = challengesTotal;
  }

  public boolean isWaiting() {
    return waiting;
  }

  public void setWaiting(boolean waiting) {
    this.waiting = waiting;
  }

  public String getTimeInGame() {
    return timeInGame;
  }

  public void setTimeInGame(String timeInGame) {
    this.timeInGame = timeInGame;
  }

  public long getSecondsInGame() {
    return secondsInGame;
  }

  public void setSecondsInGame(long secondsInGame) {
    this.secondsInGame = secondsInGame;
  }

}
