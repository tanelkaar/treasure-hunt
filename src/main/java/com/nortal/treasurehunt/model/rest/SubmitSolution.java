package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;

public class SubmitSolution implements Serializable {

  private String solution;

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }
  
}
