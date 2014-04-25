package org.csu.cs517.tmcs.input.event;

public class CloseStation extends StationEvent {

  public CloseStation(String args) {
    super(args);
  }
  
  @Override
  public String toString() {
    if (this.timeunit != null) {
      return this.getClass().getSimpleName() + "(" + this.id + "," + this.timeunit + ")";
    } else {
      return this.getClass().getSimpleName() + "(" + this.id + ")";      
    }
  }
}
