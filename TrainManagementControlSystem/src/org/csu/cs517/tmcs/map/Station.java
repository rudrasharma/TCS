package org.csu.cs517.tmcs.map;

import org.csu.cs517.tmcs.simulation.CurrentTmcsTime;
import org.csu.cs517.tmcs.simulation.ITimeIncremented;
import org.csu.cs517.tmcs.simulation.Time;

/**
 * Station where trains stops at and where the train leaves to reach other
 * stations. Routes branch from stations
 */
public class Station implements ITimeIncremented {
  private final String id;
  private Segment platform;
  private Availability availability = Availability.OPEN;
  private Time duration;
  private Time startClose;

  public Station(String id) {
    this.id = id;
    this.platform = new Segment(0, this);
  }

  public Segment getPlatform() {
    return platform;
  }

  public Availability getAvailability() {
    return availability;
  }
  
  public void setClose(Time duration) {
    if (duration != null) {
      this.duration = duration;
      this.startClose = CurrentTmcsTime.get();
    }
    this.availability = Availability.CLOSED;
  }
  
  public void setOpen() {
    this.startClose = null;
    this.duration = null;
    this.availability = Availability.OPEN;
  }
  
  public String getId() {
    return this.id;
  }
  
  @Override
  public String toString() {
    return this.id;
  }

  @Override
  public void incremented(Time time) {
    if (this.startClose != null && this.duration != null
        && this.availability == Availability.CLOSED) {
      if (time.sub(this.startClose).greater(this.duration)) {
        this.availability = Availability.OPEN;
      }
    }
  }
}
