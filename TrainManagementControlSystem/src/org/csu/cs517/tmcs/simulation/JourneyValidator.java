package org.csu.cs517.tmcs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.csu.cs517.tmcs.input.event.SubmitJourney;
import org.csu.cs517.tmcs.map.Availability;
import org.csu.cs517.tmcs.map.RailroadSystemMap;
import org.csu.cs517.tmcs.map.Route;
import org.csu.cs517.tmcs.map.Station;
import org.csu.cs517.tmcs.train.Journey;
import org.csu.cs517.tmcs.train.Train;

public class JourneyValidator {

  private final Map<String, Train> tmcsTrains;
  private final RailroadSystemMap railroadSystemMap;
  private String postValidationMessage;
  private boolean trainExists;

  public JourneyValidator(Map<String, Train> tmcsTrains,
      RailroadSystemMap railroadSystemMap) {
    this.tmcsTrains = tmcsTrains;
    this.railroadSystemMap = railroadSystemMap;
  }

  /**
   * @param submitJourney
   * @return A valid journey if and only if
   *         <ul>
   *         <li>The start station of the journey is the first station of the
   *         first route. The train must already be at the start station, or the
   *         train is not yet registered in the system and there is no train
   *         already at the start station.
   *         <li>No routes are closed
   *         <li>No stations are closed
   *         <li>All stop stations are in journey routes.
   *         <ul/>
   *         Otherwise, returns null if the journey could not be validated.
   */
  public Journey submitForJourney(SubmitJourney submitJourney) {
    String startTimeStr = submitJourney.getStartTime();
    int startTimeInt = new Integer(startTimeStr);
    String trainId = submitJourney.getTrainId();
    List<String> routeIds = submitJourney.getRouteIds();
    List<String> stopStationIds = submitJourney.getStopStationIds();
    // Get start and end stations
    String firstRouteId = routeIds.get(0);
    String lastRouteId = routeIds.get(routeIds.size() - 1);
    Route firstRoute = railroadSystemMap.getRoute(firstRouteId);
    Route lastRoute = railroadSystemMap.getRoute(lastRouteId);

    // Get journey parameters from submit journey event
    Time startTime = new Time(startTimeInt);
    Station startStation = firstRoute.getStartStation();
    Train train = this.tmcsTrains.get(trainId);
    if (train == null) {
      // Introduce a new train into the system.
      train = new Train(trainId, startStation.getPlatform());
    }
    Station endStation = lastRoute.getEndStation();
    List<Station> stops = new ArrayList<>();
    for (String stationId : stopStationIds) {
      Station stop = this.railroadSystemMap.getStation(stationId);
      stops.add(stop);
    }
    List<Route> routes = new ArrayList<>();
    for (String routeId : routeIds) {
      Route route = this.railroadSystemMap.getRoute(routeId);
      routes.add(route);
    }

    boolean validJourney = this.validate(startTime, train, startStation,
        endStation, routes, stops);
    if (validJourney) {
      Journey j = new Journey(startTime, CurrentTmcsTime.get(), startStation,
          endStation, stops, routes);
      return j;
    }
    return null;
  }

  public boolean trainExists() {
    return this.trainExists;
  }

  public String getValidationMessage() {
    return postValidationMessage;
  }

  private boolean validate(Time startTime, Train train, Station startStation,
      Station endStation, List<Route> routes, List<Station> stops) {
    this.postValidationMessage = null;
    if (!this.validateTrainAtStartStation(train, startStation)) {
      return false;
    }
    if (!this.validateNoClosedRoutes(train, routes)) {
      return false;
    }
    if (!this.validateNoClosedStations(train, stops)) {
      return false;
    }
    if (!this.validateEachStopInRoutes(train, routes, stops)) {
      return false;
    }
    return true;
  }

  private boolean validateTrainAtStartStation(Train train, Station startStation) {
    Train tmcsTrain = this.tmcsTrains.get(train.getId());
    Train trainInStartStation = startStation.getPlatform().getOccupiedBy();
    if (tmcsTrain == null) {
      this.trainExists = false;
    } else {
      this.trainExists = true;
    }
    if (!trainExists && null == trainInStartStation) {
      // This train does not exist in the system and there is no train in the
      // start station.
      return true;
    } else if (trainInStartStation == tmcsTrain) {
      // Else the train in the start station is the same as the train submitting
      // the journey
      return true;
    } else {
      // Else there is a train in the start station that is not the train
      // submitting the journey.
      this.postValidationMessage = "There is already a train " + train
          + " at start station " + startStation + ", which is not train "
          + train + "that submitted the journey.";
      return false;
    }
  }

  private boolean validateNoClosedRoutes(Train train, List<Route> routes) {
    for (Route route : routes) {
      if (route.getAvailability() == Availability.CLOSED) {
        this.postValidationMessage = "Train " + train
            + " is submitting a journey with route " + route
            + " that is currently closed.";
        return false;
      }
    }
    return true;
  }

  private boolean validateNoClosedStations(Train train, List<Station> stations) {
    for (Station s : stations) {
      if (s.getAvailability() == Availability.CLOSED) {
        this.postValidationMessage = "Train " + train
            + " is submitting a journey where stop station " + s
            + " is currently closed.";
        return false;
      }
    }
    return true;
  }

  private boolean validateEachStopInRoutes(Train train, List<Route> routes,
      List<Station> stations) {
    for (Station s : stations) {
      Station stationNotInRoutes = null;
      for (Route r : routes) {
        if (r.getStartStation() == s) {
          stationNotInRoutes = s;
          break;
        }
      }
      if (stationNotInRoutes != null) {
        this.postValidationMessage = "Stop station "
            + s
            + " is not a station in the list of journey routes submitted by train "
            + train + ".";
        return false;
      }
    }
    return true;
  }
}
