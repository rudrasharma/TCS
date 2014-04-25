package org.csu.cs517.tmcs.map;

import java.util.List;

/**
 * Represent a one directional train track. Each route has a start station and
 * an end station.
 */
public class Route {
  private final String id;
  // A route starts at a station and ends at a station
  private Station startStation;
  private Station endStation;

  // A route is composed of segments
  private List<Segment> segments;

  private Availability availability = Availability.OPEN;

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
  
  public void setAvailability(Availability availability) {
    this.availability = availability;
  }
  
  @Override
  public String toString() {
    return this.id;
  }

  public String getId() {
    return this.id;
  }
}
