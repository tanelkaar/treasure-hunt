package com.nortal.treasurehunt.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

public class TeamAssignment implements Serializable {

  private AssignmentStatus status = AssignmentStatus.WAITING;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  private Date startTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
  private Date endTime;
  private String completedIn;
  private Long tries;

  @JsonFormat(shape = JsonFormat.Shape.OBJECT)
  public enum AssignmentStatus {
    CURRENT,
    COMPLETED,
    WAITING;

    private String text;
    private String code;

    private AssignmentStatus() {
      code = name();
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }
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
    if (startTime != null && endTime == null) {
      status = AssignmentStatus.CURRENT;
    }
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
    fillCompletedIn();
    if (endTime != null) {
      status = AssignmentStatus.COMPLETED;
    }
  }

  private void fillCompletedIn() {
    if (startTime != null && endTime != null) {
      Long diff = (endTime.getTime() - startTime.getTime()) / 1000;
      completedIn = ((int) (diff / 60)) + "m " + (diff % 60) + "s";
      status = AssignmentStatus.COMPLETED;
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

  public long getSecondsInGame() {
    if (startTime == null) {
      return 0;
    }
    return ((endTime == null ? System.currentTimeMillis() : endTime.getTime())
    - startTime.getTime()) / 1000;
  }
}
