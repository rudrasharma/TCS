package org.csu.cs517.tmcs.system.event;

import org.csu.cs517.tmcs.map.Route;
import org.csu.cs517.tmcs.map.Segment;
import org.csu.cs517.tmcs.map.Station;
import org.csu.cs517.tmcs.map.TrafficLightColor;

public class ChangeLight {
  
  Route route;
  Station station;
  Segment segment;
  TrafficLightColor color;
  
  public ChangeLight(Segment segment, TrafficLightColor color) {
    this.route = segment.getRoute();
    this.station = segment.getStation();
    this.segment = segment;
    this.color = color;
  }

  public Route getRoute() {
    return route;
  }

  public Segment getSegmentId() {
    return segment;
  }

  public TrafficLightColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    if (this.route != null) {
      return this.getClass().getSimpleName() + "(" + route + "." + segment + ", "
          + color + ")";
    } else {
      return this.getClass().getSimpleName() + "(" + station + "." + segment + ", "
          + color + ")";
    }
  }

  public Station getStation() {
    return this.station;
  }
}
