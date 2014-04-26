package org.csu.cs517.tmcs.input.event;

public class End extends InputEvent {

  public End(String args) {
    super(2, args, InputEvent.ParseState.DONE);
  }

}
