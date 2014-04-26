package org.csu.cs517.tmcs.train;

import java.util.List;

import org.csu.cs517.tmcs.map.Route;
import org.csu.cs517.tmcs.map.Station;
import org.csu.cs517.tmcs.simulation.Time;

public class Journey {
  // Number of time units after journey approval time of when a train is
  // allowed to proceed on this journey.
  private Time waitTimeTillStart;

  // Time when journey was approved.
  private Time approvalTime;

  // This journey starts at a station.
  private Station startStation;

  // This journey ends at a station.
  private Station endStation;

  // Planned stations where the train will stop.
  private List<Station> stops;

  // Routes on journey
  private List<Route> routes;

  public Journey(Time waitTimeTillStart, Time approvalTime,
      Station startStation, Station endStation, List<Station> stops,
      List<Route> routes) {
    if (waitTimeTillStart.getUnits() < 1) {
      throw new IllegalArgumentException(
          "Argument waitTimeTillStart is less than 1.");
    }
    this.waitTimeTillStart = waitTimeTillStart;
    this.approvalTime = approvalTime;
    this.startStation = startStation;
    this.endStation = endStation;
    this.stops = stops;
    this.routes = routes;
  }

  public Time getWaitTimeTillStart() {
    return waitTimeTillStart;
  }

  public Time getApprovalTime() {
    return approvalTime;
  }

  public Station getStartStation() {
    return startStation;
  }

  public Station getEndStation() {
    return endStation;
  }

  public List<Station> getStops() {
    return stops;
  }

  public List<Route> getRoutes() {
    return this.routes;
  }
}
