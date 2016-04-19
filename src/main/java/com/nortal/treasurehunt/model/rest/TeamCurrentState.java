package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;
import java.util.List;

/**
 * Team state
 * @author Tanel Käär (Tanel.Kaar@nortal.com)
 */
public class TeamCurrentState implements Serializable {
  private Long id;
  private List<Link> links;
  private CurrentAssignment currentassignment;
  private Long challengesCompleted;
  private Long challengesTotal;
  private boolean gameEnded;
  private Messages messages;

  public Messages addSuccess(String message) {
    createMessagesIfNeeded();
    messages.addSuccess(message);
    return messages;
  }

  public Messages addWarning(String message) {
    createMessagesIfNeeded();
    messages.addWarning(message);
    return messages;
  }

  public Messages addError(String message) {
    createMessagesIfNeeded();
    messages.addError(message);
    return messages;
  }

  private void createMessagesIfNeeded() {
    if (messages == null) {
      messages = new Messages();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CurrentAssignment getCurrentassignment() {
    return currentassignment;
  }

  public void setCurrentassignment(CurrentAssignment currentassignment) {
    this.currentassignment = currentassignment;
  }

  public Long getChallengesCompleted() {
    return challengesCompleted;
  }

  public void setChallengesCompleted(Long challengescompleted) {
    this.challengesCompleted = challengescompleted;
    fillGameEnded();
  }

  public Long getChallengesTotal() {
    return challengesTotal;
  }

  public void setChallengesTotal(Long challengestotal) {
    this.challengesTotal = challengestotal;
    fillGameEnded();
  }

  private void fillGameEnded() {
    if(challengesCompleted != null && challengesCompleted != null) {
      gameEnded = challengesTotal.compareTo(challengesCompleted) == 0;
    }
  }
  public boolean getGameEnded() {
    return gameEnded;
  }

  public void setGameEnded(boolean gameEnded) {
    this.gameEnded = gameEnded;
  }

  public Messages getMessages() {
    return messages;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public static class Builder {
    private Long id;
    private List<Link> links;
    private CurrentAssignment currentassignment;
    private Long challengesCompleted;
    private Long challengesTotal;
    private boolean gameEnded;
    private Messages messages;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder links(List<Link> links) {
      this.links = links;
      return this;
    }

    public Builder currentassignment(CurrentAssignment currentassignment) {
      this.currentassignment = currentassignment;
      return this;
    }

    public Builder challengesCompleted(Long challengesCompleted) {
      this.challengesCompleted = challengesCompleted;
      return this;
    }

    public Builder challengesTotal(Long challengesTotal) {
      this.challengesTotal = challengesTotal;
      return this;
    }

    public Builder gameEnded(boolean gameEnded) {
      this.gameEnded = gameEnded;
      return this;
    }

    public Builder messages(Messages messages) {
      this.messages = messages;
      return this;
    }

    public TeamCurrentState build() {
      return new TeamCurrentState(this);
    }
  }

  private TeamCurrentState(Builder builder) {
    this.id = builder.id;
    this.links = builder.links;
    this.currentassignment = builder.currentassignment;
    this.challengesCompleted = builder.challengesCompleted;
    this.challengesTotal = builder.challengesTotal;
    this.gameEnded = builder.gameEnded;
    this.messages = builder.messages;
    this.fillGameEnded();
  }
}
