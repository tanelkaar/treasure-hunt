package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Messages implements Serializable {

  private List<String> success;
  private List<String> warning;
  private List<String> error;
  
  public List<String> getSuccess() {
    return success;
  }

  public List<String> getWarning() {
    return warning;
  }

  public List<String> getError() {
    return error;
  }

  public Messages addSuccess(String message) {
    if(success == null) {
      success = new ArrayList<String>();
    }
    success.add(message);
    return this;
  }
  
  public Messages addWarning(String message) {
    if(warning == null) {
      warning = new ArrayList<String>();
    }
    warning.add(message);
    return this;
  }
  
  public Messages addError(String message) {
    if(error == null) {
      error = new ArrayList<String>();
    }
    error.add(message);
    return this;
  }
  
}
