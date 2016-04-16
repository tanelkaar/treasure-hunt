package com.nortal.treasurehunt.model.rest;

import java.util.ArrayList;
import java.util.List;

public class Team {

  private String name;
  private Long challengesCompleted;
  private List<TeamAssignment> assignments;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Long getChallengesCompleted() {
    return challengesCompleted;
  }
  public void setChallengesCompleted(Long challengesCompleted) {
    this.challengesCompleted = challengesCompleted;
  }
  public List<TeamAssignment> getAssignments() {
    return assignments;
  }
  public Team addAssignment(TeamAssignment assignment) {
    if(this.assignments == null) {
      assignments = new ArrayList<TeamAssignment>();
    }
    assignments.add(assignment);
    return this;
  }
  public void setAssignments(List<TeamAssignment> assignments) {
    this.assignments = assignments;
  }
  
}
