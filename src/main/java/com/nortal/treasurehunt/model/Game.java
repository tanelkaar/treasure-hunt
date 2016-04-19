package com.nortal.treasurehunt.model;

public class Game {

  private String noAssignmentsAvailableText;
  private String allChallengesCompletedText;

  public String getNoAssignmentsAvailableText() {
    return noAssignmentsAvailableText;
  }

  public void setNoAssignmentsAvailableText(String noAssignmentsAvailableText) {
    this.noAssignmentsAvailableText = noAssignmentsAvailableText;
  }

  public String getAllChallengesCompletedText() {
    return allChallengesCompletedText;
  }

  public void setAllChallengesCompletedText(String allChallengesCompletedText) {
    this.allChallengesCompletedText = allChallengesCompletedText;
  }

  public static class Builder {
    private String noAssignmentsAvailableText;
    private String allChallengesCompletedText;

    public Builder noAssignmentsAvailableText(String noAssignmentsAvailableText) {
      this.noAssignmentsAvailableText = noAssignmentsAvailableText;
      return this;
    }

    public Builder allChallengesCompletedText(String allChallengesCompletedText) {
      this.allChallengesCompletedText = allChallengesCompletedText;
      return this;
    }

    public Game build() {
      return new Game(this);
    }
  }

  private Game(Builder builder) {
    this.noAssignmentsAvailableText = builder.noAssignmentsAvailableText;
    this.allChallengesCompletedText = builder.allChallengesCompletedText;
  }
}
