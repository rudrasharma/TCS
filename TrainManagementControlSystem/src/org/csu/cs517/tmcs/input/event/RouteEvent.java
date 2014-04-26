package org.csu.cs517.tmcs.input.event;


public class RouteEvent extends InputEvent {

  protected String id;
  protected String timeunit;  

  public RouteEvent(String args) {
    super(0, args, InputEvent.ParseState.ROUTE);
  }
  
  public String getRouteId() {
    return id;
  }

  public String getTimeunit() {
    return timeunit;
  }

  @Override
  protected void parseComma() {
    if (this.currentState == InputEvent.ParseState.ROUTE) {
      this.currentState = InputEvent.ParseState.TIME;
      this.id = this.tempStringBuffer.toString();
      this.tempStringBuffer.setLength(0);
    }
  }
  
  @Override
  protected void parseEnd() {
    if (this.currentState == InputEvent.ParseState.ROUTE) {
      this.currentState = InputEvent.ParseState.TIME;
      this.id = this.tempStringBuffer.toString();
    } else if (this.currentState == InputEvent.ParseState.TIME) {
      this.currentState = InputEvent.ParseState.TIME;
      this.timeunit = this.tempStringBuffer.toString();      
    }
    this.currentState = InputEvent.ParseState.DONE;
    this.tempStringBuffer.setLength(0);
  }
}
