package org.csu.cs517.tmcs.train;

import java.util.List;

import org.csu.cs517.tmcs.map.Route;
import org.csu.cs517.tmcs.map.Segment;
import org.csu.cs517.tmcs.map.Station;
import org.csu.cs517.tmcs.map.TrafficLightColor;
import org.csu.cs517.tmcs.simulation.CurrentTmcsTime;
import org.csu.cs517.tmcs.simulation.Time;

public class Train {
  private final String id;
  private Segment onSegment;

  // The train travels on a journey
  private Journey journey;
  
  // True if the train is stopped by the system.
  private boolean isStopped;
  
  private int routeIdx;
  
  public Train(String id, Segment onSegment) {
    this.id = id;
    this.onSegment = onSegment;
    this.onSegment.trainEnter(this);
  }

  /**
   * Moves the train forward to the next segment if the train has a valid
   * journey and the current segment's traffic light is green.
   * 
   * @return True if the train was moved forward, otherwise false.
   */
  public boolean moveForward() {
    // Preconditions:
    // Don't move if the train is currently stopped by the system.
    if (this.isStopped) {
      return false;
    }
    // Check if the train has a journey
    if (this.journey == null) {
      return false;
    }
    // Has the journey been approved
    Time approvalTime = this.journey.getApprovalTime();
    if (approvalTime == null) {
      return false;
    }
    // The train wait time is less than the current time - approved time
    Time waitTime = this.journey.getWaitTimeTillStart();
    Time currentTime = CurrentTmcsTime.get();
    if (waitTime.greater(currentTime.sub(approvalTime))) {
      return false;
    }
    // Make sure the train is not at the end of its journey.
    if (this.atEndOfJourney()) {
      return false;
    }
    // This segment's traffic light is green
    if (this.onSegment.getTrafficLightColor() == TrafficLightColor.RED) {
      return false;
    }
    // End Preconditions

    // All preconditions have been met, so now lets move the train along to the
    // next segment.
    // First determine if this train is on a route or platform segment
    Route route = this.onSegment.getRoute();
    if (route != null) {
      // Since the train is still on a route, just move it to the next linked
      // segment
      this.moveTrainToNextSegment(this.onSegment.getNext(0));
    } else {
      // Else the train is in a station, so select the segment of the next
      // route from the journey list of routes.
      List<Route> journeyRoutes = this.journey.getRoutes();
      if (this.routeIdx < journeyRoutes.size()) {
        Route nextRoute = journeyRoutes.get(this.routeIdx);
        Segment firstSegmentOfRoute = nextRoute.getSegments().get(0);
        this.moveTrainToNextSegment(firstSegmentOfRoute);
        routeIdx++; // Train is now on the next route.
      }
    }
    return true;
  }

  public Journey getJourney() {
    return this.journey;
  }
  
  public void setJourney(Journey journey) {
    this.journey = journey;
  }

  public Segment getOnSegment() {
    return this.onSegment;
  }
  
  public boolean isStopped() {
    return this.isStopped;
  }
  
  public void stop() {
    this.isStopped = true;
  }
  
  public void restart() {
    this.isStopped = false;
  }

  public String getId() {
    return this.id;
  }
  
  public boolean atEndOfJourney() {
    Station endStation = this.journey.getEndStation();
    if (endStation.getPlatform() == this.onSegment) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public String toString() {
    return id;
  }
  
  /**
   * @return The next route on the journey, or null if the train is on its
   * last journey.
   */
  public Route getNextRoute() {
    if (routeIdx < this.journey.getRoutes().size() - 1) {
      return this.journey.getRoutes().get(routeIdx + 1);
    }
    return null;
  }
  
  private void moveTrainToNextSegment(Segment nextSegment) {
    if (!this.onSegment.getNext().contains(nextSegment)) {
      throw new IllegalArgumentException(
          "Cannot move train because argument nextSegment " + nextSegment + 
          " is not connected to the current segment " + this.onSegment +
          " that the train " + this + " is on.");
    }
    this.onSegment.trainExit(this);
    nextSegment.trainEnter(this);
    this.onSegment = nextSegment;

  }
}
