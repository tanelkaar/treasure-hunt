package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;

public class Currentassignment implements Serializable {
  
  public Currentassignment(String text) {
    this.text = text;
  }

  private String text;

  public String getText() {
    return text;
  }
}
