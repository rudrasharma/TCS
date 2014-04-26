package org.csu.cs517.tmcs.input.event;

import org.csu.cs517.tmcs.simulation.Time;

public class CloseRoute extends RouteEvent {

  public CloseRoute(String args) {
    super(args);
  }
  
  public Time getDuration() {
    if (this.timeunit != null) {
      Integer durationInt = new Integer(this.timeunit);
      return new Time(durationInt);
    } else {
      return new Time(0);
    }
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
