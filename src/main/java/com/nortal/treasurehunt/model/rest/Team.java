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
    if(this.assignments == null) {
      assignments = new ArrayList<TeamAssignment>();
    }
    assignments.add(assignment);
    if(assignment.getEndTime() != null) {
      challengesCompleted++;
    }
    if(assignment.getStartTime() != null && assignment.getEndTime() == null) {
      waiting = false;
    }
    finished = challengesTotal == challengesCompleted;
    return this;
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

}
