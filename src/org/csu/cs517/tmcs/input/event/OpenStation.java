package org.csu.cs517.tmcs.input.event;

public class OpenStation extends StationEvent {

  public OpenStation(String args) {
    super(args);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + this.id + ")";
  }
}
