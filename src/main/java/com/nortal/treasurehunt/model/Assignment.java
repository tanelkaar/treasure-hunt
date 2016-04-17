package com.nortal.treasurehunt.model;

public class Assignment {
  private Long id;
  private String solution;
  private String correctText;
  private String wrongText;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public String getCorrectText() {
    return correctText;
  }

  public void setCorrectText(String correctText) {
    this.correctText = correctText;
  }

  public String getWrongText() {
    return wrongText;
  }

  public void setWrongText(String wrongText) {
    this.wrongText = wrongText;
  }

  public static class Builder {
    private Long id;
    private String solution;
    private String correctText;
    private String wrongText;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder solution(String solution) {
      this.solution = solution;
      return this;
    }

    public Builder correctText(String correctText) {
      this.correctText = correctText;
      return this;
    }

    public Builder wrongText(String wrongText) {
      this.wrongText = wrongText;
      return this;
    }

    public Assignment build() {
      return new Assignment(this);
    }
  }

  private Assignment(Builder builder) {
    this.id = builder.id;
    this.solution = builder.solution;
    this.correctText = builder.correctText;
    this.wrongText = builder.wrongText;
  }
}
