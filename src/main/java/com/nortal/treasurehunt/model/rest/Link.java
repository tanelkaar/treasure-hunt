package com.nortal.treasurehunt.model.rest;

import java.io.Serializable;

/**
 * HATEOAS link
 * @author Tanel Käär (Tanel.Kaar@nortal.com)
 */
public class Link implements Serializable {
  
  private String rel;
  private String href;
  
  public Link(String rel, String href) {
    this.rel = rel;
    this.href = href;
  }

  public String getRel() {
    return rel;
  }

  public String getHref() {
    return href;
  }
  
}
