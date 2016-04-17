package com.nortal.treasurehunt.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

public class TeamAssignment implements Serializable {

  private AssignmentStatus status = AssignmentStatus.WAITING;
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
  private Date startTime;
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
  private Date endTime;
  private String completedIn;
  private Long tries;

  public enum AssignmentStatus {
    CURRENT,
    COMPLETED,
    WAITING
  }

  public AssignmentStatus getStatus() {
    return status;
  }

  public void setStatus(AssignmentStatus status) {
    this.status = status;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
    fillCompletedIn();
    if(startTime != null && status == null) {
      status = AssignmentStatus.CURRENT;
    }
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
    fillCompletedIn();
    if(endTime != null) {
      status = AssignmentStatus.COMPLETED;
    }
  }

  private void fillCompletedIn() {
    if(startTime != null && endTime != null) {
      Long diff = (endTime.getTime() - startTime.getTime()) / 1000;
      completedIn = ((int)(diff / 60)) + "m " + (diff % 60) + "s";
    }
  }

  public String getCompletedIn() {
    return completedIn;
  }

  public Long getTries() {
    return tries;
  }

  public void setTries(Long tries) {
    this.tries = tries;
  }

  public void setCompletedIn(String completedIn) {
    this.completedIn = completedIn;
  }
}
