package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;

public class TeamRegistration implements Serializable {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
