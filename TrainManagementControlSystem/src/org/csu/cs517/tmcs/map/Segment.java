package org.csu.cs517.tmcs.map;

import java.util.ArrayList;
import java.util.List;

import org.csu.cs517.tmcs.train.Train;

/**
 * Represents a segment of a route.
 */
public class Segment {
  private final int id;

// Segments connect to each other to form a route
  private List<Segment> next = new ArrayList<>();

  private Train occupiedBy;
  private Route segmentOfRoute;
  private Station platformOfStation;
  private TrafficLight trafficLight;

  public Segment(int id, Route segmentOfRoute) {
    this.id = id;
    this.segmentOfRoute = segmentOfRoute;
    this.next = new ArrayList<Segment>();
    this.trafficLight = new TrafficLight(this);
  }

  public Segment(int id, Station platformOfStation) {
    this.id = id;
    this.platformOfStation = platformOfStation;
    this.trafficLight = new TrafficLight(this);
  }

  public Route getRoute() {
    return segmentOfRoute;
  }

  public Station getStation() {
    return platformOfStation;
  }
  
  /**
   * @return {@link #getTrafficLightColor()}
   */
  public TrafficLightColor getTrafficLightColor() {
    return this.trafficLight.getColor();
  }

  /**
   * @return List of the next segments that this segment connects to.
   */
  public List<Segment> getNext() {
    return this.next;
  }
  
  public Segment getNext(int nextSegmentsIdx) {
    return this.getNext().get(nextSegmentsIdx);
  }

  public Train getOccupiedBy() {
    return occupiedBy;
  }

  public void trainExit(Train train) {
    if (this.occupiedBy != train) {
      throw new IllegalStateException("The train " + train
          + " exiting is not the same train " + this.occupiedBy
          + " that occupies this segment.");
    }
    this.occupiedBy = null;
  }
  
  public void trainEnter(Train train) {
    if (this.occupiedBy != null) {
      throw new IllegalStateException("Train " + train
          + " cannot enter segment " + this + " because train "
          + this.occupiedBy + " already occupies this segment.");
    }
    this.occupiedBy = train;
  }

  public void addNext(Segment next) {
    this.next.add(next);
  }
  
  @Override
  public String toString() {
    return Integer.toString(id);
  }

  public int getId() {
    return this.id;
  }
}
