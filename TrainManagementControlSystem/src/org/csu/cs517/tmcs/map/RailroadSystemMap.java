package org.csu.cs517.tmcs.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.csu.cs517.tmcs.common.Strings;
import org.csu.cs517.tmcs.input.event.CloseRoute;
import org.csu.cs517.tmcs.input.event.CloseStation;
import org.csu.cs517.tmcs.input.event.OpenRoute;
import org.csu.cs517.tmcs.input.event.OpenStation;
import org.csu.cs517.tmcs.simulation.Time;

public class RailroadSystemMap {
  private final String systemName;
  private Map<String, Route> routes = new TreeMap<>();
  private Map<String, Station> stations = new HashMap<>();
  
  public RailroadSystemMap(String systemName) {
    this.systemName = systemName;
  }

  /**
   * 
   * @param routeId
   *          Cannot be null, must be a 4 digit number and the number must be
   *          unique.
   * @param startStationId
   *          Cannot be null, must be a 4 alpha-character string and this
   *          identifier will refer to a unique station object.
   * @param endStationId
   *          Cannot be null, must be a 4 alpha-character string and this identifier
   *          will refer to a unique station object.
   * @param numberOfSegments
   *          Must be greater than zero and specifies the length of the route.
   */
  public void addRoute(String routeId, String startStationId,
      String endStationId, String numberOfSegments) {
    this.validateRouteIdToInteger(routeId);
    this.validateStationId(startStationId);
    this.validateStationId(endStationId);
    Integer numSegments = this.validateNumberOfSegments(numberOfSegments);
    this.establishRouteInSystem(routeId, startStationId, endStationId,
        numSegments);
  }

  private void validateRouteIdToInteger(String routeId)
      throws IllegalArgumentException {
    // routeId must not be null
    if (routeId == null) {
      throw new IllegalArgumentException("Argument routeId is null");
    }
    // routeId must be four digits
    if (routeId.length() != 4) {
      throw new IllegalArgumentException("Argument routeId is null");
    }
    // routeId must be a number.
    try {
      Integer.parseInt(routeId);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Argument routeId is not a number.");
    }
    // routeId must be a unique identifier
    if (this.routes.containsKey(routeId)) {
      throw new IllegalArgumentException("Argument routeId is null");
    }
  }

  private void validateStationId(String stationId)
      throws IllegalArgumentException {
    // routeId must not be null
    if (stationId == null) {
      throw new IllegalArgumentException("Argument station is null.");
    }
    // routeId must be four alpha digits
    if (stationId.length() != 4) {
      throw new IllegalArgumentException(
          "Argument station is not four characters in length.");
    }
    for (int i = 0; i < 4; i++) {
      if (!Character.isAlphabetic(stationId.charAt(i))) {
        throw new IllegalArgumentException(
            "Argument station does not contain only alphabetic characters.");
      }
    }
  }

  private Integer validateNumberOfSegments(String numberOfSegments) {
    Integer numSegments;
    try {
      numSegments = new Integer(numberOfSegments);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Argument numberOfSegments is not a number.");
    }
    if (numSegments < 1) {
      throw new IllegalArgumentException(
          "Argument numberOfSegments is less than 1.");
    }
    return numSegments;
  }

  private void establishRouteInSystem(String routeId,
      String startStationId, String endStationId, int numberOfSegments) {
    // Create and insert new route
    Route route = new Route(routeId);
    this.routes.put(routeId, route);
    
    // Get existing station as start station for this route.
    Station startStation = this.stations.get(startStationId);
    if (startStation == null) {
      // Create the station if does not exist.
      startStation = new Station(this, startStationId);
      this.stations.put(startStationId, startStation);
    }
    // Get existing station as end station for this route.
    Station endStation = this.stations.get(endStationId);
    if (endStation == null) {
      // Create the station if does not exist.
      endStation = new Station(this, endStationId);
      this.stations.put(endStationId, endStation);
    }
    route.setStartStation(startStation);
    route.setEndStation(endStation);
    
    // Create segments for the new route
    List<Segment> segments = new ArrayList<>();
    Segment previousSegment = null;
    for (int sIdx = 0; sIdx < numberOfSegments; sIdx++) {
      Segment segment = new Segment(sIdx + 1, route);
      if (sIdx == 0) {
        // Link the start station platform with this route's first segment.
        startStation.getPlatform().addNext(segment);
      } else if (sIdx == numberOfSegments - 1){
        // Link this segment to the next end station platform segment.
        segment.addNext(endStation.getPlatform());
      }
      if (previousSegment != null) {
        // Link previous segment to this segment.
        previousSegment.addNext(segment);
      }      
      segments.add(segment);
      previousSegment = segment;
    }
    
    // Add new segments into the route.
    route.setSegments(segments);
  }

  public String getSystemName() {
    return systemName;
  }
  
  public void openRoute(OpenRoute openRoute) {
    String routeId = openRoute.getRouteId();
    Route r = this.routes.get(routeId);
    r.setOpen();
  }
  
  public void closeRoute(CloseRoute closeRoute) {
    String routeId = closeRoute.getRouteId();
    Time duration = closeRoute.getDuration();
    Route r = this.routes.get(routeId);
    r.setClose(duration);
  }
  
  public void openStation(OpenStation openStation) {
    String stationId = openStation.getStationId();
    Station station = this.stations.get(stationId);
    station.setOpen();
  }
  
  public void closeStation(CloseStation closeStation) {
    String stationId = closeStation.getStationId();
    Time duration = closeStation.getDuration();
    Station station = this.stations.get(stationId);
    station.setClose(duration);
  }
  
  public Route getRoute(String routeId) {
    return this.routes.get(routeId);
  }
  
  public Station getStation(String stationId) {
    return this.stations.get(stationId);
  }
  
  public Map<String, Route> getRoutes() {
    return this.routes;
  }
  
  public Map<String, Station> getStations() {
    return this.stations;
  }
  
  public List<Segment> getPrevious(Segment segment) {
    List<Segment> previous = new ArrayList<>();
    for (Entry<String, Route> r : this.routes.entrySet()) {
      List<Segment> segments = r.getValue().getSegments();
      for (Segment s : segments) {
        if (s.getNext().contains(segment)) {
          previous.add(s);
        }
      }
    }
    for (Entry<String, Station> s : this.stations.entrySet()) {
      Segment platform = s.getValue().getPlatform();
      if (platform.getNext().contains(segment)) {
        previous.add(platform);
      }
    }
    return previous;
  }
  
  @Override
  public String toString() {
  	StringBuilder sb = new StringBuilder();
  	sb.append("State of Railroad System Map: " + this.systemName + 
  			Strings.NL + Strings.NL);
  	String indent = "   ";
  	sb.append(indent + "       Start    End" + Strings.NL);
  	sb.append(indent + "Route  Station  Station  #Segs" + Strings.NL);
  	sb.append(indent + "-----  -------  -------  -----" + Strings.NL);
  	for (Entry<String, Route> routeEntry : this.routes.entrySet()) {
  		Route route = routeEntry.getValue();
  		Station start = route.getStartStation();
  		Station end = route.getEndStation();
  		int numSegments = route.getSegments().size();
  		sb.append(
  		    indent +
  				route + "   " + start + "     " + end + "     " + numSegments + 
  				Strings.NL);
  	}
  	return sb.toString();
  }
}