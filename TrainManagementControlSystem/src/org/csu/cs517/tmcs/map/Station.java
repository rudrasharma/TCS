package org.csu.cs517.tmcs.map;

import java.util.List;

import org.csu.cs517.tmcs.simulation.CurrentTmcsTime;
import org.csu.cs517.tmcs.simulation.ITimeIncremented;
import org.csu.cs517.tmcs.simulation.Time;
import org.csu.cs517.tmcs.system.event.ChangeLight;
import org.csu.cs517.tmcs.system.event.SystemEventGenerator;

/**
 * Station where trains stops at and where the train leaves to reach other
 * stations. Routes branch from stations
 */
public class Station implements ITimeIncremented {
  private final RailroadSystemMap railroadSystemMap;
  private final String id;
  private Segment platform;
  private Availability availability = Availability.OPEN;
  private Time duration;
  private Time startClose;

  public Station(RailroadSystemMap railroadSystemMap, String id) {
    this.railroadSystemMap = railroadSystemMap;
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
    // Change lights of previous segments to RED.
    if (platform.getOccupiedBy() == null) {
      List<Segment> prevSegs = this.railroadSystemMap.getPrevious(this.platform);
      for (Segment s : prevSegs) {
        ChangeLight cl = new ChangeLight(s, TrafficLightColor.RED);
        SystemEventGenerator.getInstance().generateChangeLightEvent(cl);
      }
    }
  }
  
  public void setOpen() {
    this.startClose = null;
    this.duration = null;
    this.availability = Availability.OPEN;
    // Change lights of previous segments to GREEN if there is no train in
    // the station
    if (platform.getOccupiedBy() == null) {
      List<Segment> prevSegs = this.railroadSystemMap.getPrevious(this.platform);
      for (Segment s : prevSegs) {
        ChangeLight cl = new ChangeLight(s, TrafficLightColor.GREEN);
        SystemEventGenerator.getInstance().generateChangeLightEvent(cl);
      }
    }
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
        this.setOpen();
      }
    }
  }
}
