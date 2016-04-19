package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;

public class CurrentAssignment implements Serializable {
  
  public CurrentAssignment(String text) {
    this.text = text;
  }

  private String text;

  public String getText() {
    return text;
  }
}
