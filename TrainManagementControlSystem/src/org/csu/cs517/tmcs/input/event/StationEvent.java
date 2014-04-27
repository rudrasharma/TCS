package org.csu.cs517.tmcs.input.event;

public class StationEvent extends InputEvent {
  
  protected String id;
  protected String timeunit;

  public StationEvent(String args) {
    super(0, args, InputEvent.ParseState.STATION);
  }

  public String getStationId() {
    return id;
  }
  
  @Override
  protected void parseComma() {
    if (this.currentState == InputEvent.ParseState.STATION) {
      this.currentState = InputEvent.ParseState.TIME;
      this.id = this.tempStringBuffer.toString();
      this.tempStringBuffer.setLength(0);
    }
  }
  
  @Override
  protected void parseEnd() {
    if (this.currentState == InputEvent.ParseState.STATION) {
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
