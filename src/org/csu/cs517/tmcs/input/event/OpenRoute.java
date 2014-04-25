package org.csu.cs517.tmcs.input.event;

public class OpenRoute extends RouteEvent {

  public OpenRoute(String args) {
    super(args);
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + this.id + ")";
  }
}
