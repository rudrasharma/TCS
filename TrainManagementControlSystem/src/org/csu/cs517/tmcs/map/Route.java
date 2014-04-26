package org.csu.cs517.tmcs.map;

import java.util.List;

import org.csu.cs517.tmcs.simulation.CurrentTmcsTime;
import org.csu.cs517.tmcs.simulation.ITimeIncremented;
import org.csu.cs517.tmcs.simulation.Time;

/**
 * Represent a one directional train track. Each route has a start station and
 * an end station.
 */
public class Route implements ITimeIncremented {
  private final String id;
  // A route starts at a station and ends at a station
  private Station startStation;
  private Station endStation;

  // A route is composed of segments
  private List<Segment> segments;

  private Availability availability = Availability.OPEN;
  private Time duration;
  private Time startClose;

  public Route(String id) {
    this.id = id;
  }

  public Station getStartStation() {
    return startStation;
  }
  
  public void setStartStation(Station startStation) {
    this.startStation = startStation;
  }

  public Station getEndStation() {
    return endStation;
  }
  
  public void setEndStation(Station endStation) {
    this.endStation = endStation;
  }

  public List<Segment> getSegments() {
    return segments;
  }
  
  public void setSegments(List<Segment> segments) {
    this.segments = segments;
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
    this.duration = null;
    this.startClose = null;
    this.availability = Availability.OPEN;
  }
  
  @Override
  public String toString() {
    return this.id;
  }

  public String getId() {
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
