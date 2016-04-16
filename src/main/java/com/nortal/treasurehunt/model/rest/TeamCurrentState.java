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
  private Currentassignment currentassignment;
  private Long challengesCompleted;
  private Long challengesTotal;
  private Boolean gameEnded;
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
    if(messages == null) {
      messages = new Messages();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Currentassignment getCurrentassignment() {
    return currentassignment;
  }

  public void setCurrentassignment(Currentassignment currentassignment) {
    this.currentassignment = currentassignment;
  }

  public Long getChallengesCompleted() {
    return challengesCompleted;
  }

  public void setChallengesCompleted(Long challengescompleted) {
    this.challengesCompleted = challengescompleted;
  }

  public Long getChallengesTotal() {
    return challengesTotal;
  }

  public void setChallengesTotal(Long challengestotal) {
    this.challengesTotal = challengestotal;
  }

  public Boolean getGameEnded() {
    return gameEnded;
  }

  public void setGameEnded(Boolean gameEnded) {
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
  
}
