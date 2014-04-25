package org.csu.cs517.tmcs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.csu.cs517.tmcs.common.Strings;
import org.csu.cs517.tmcs.input.event.CloseRoute;
import org.csu.cs517.tmcs.input.event.CloseStation;
import org.csu.cs517.tmcs.input.event.End;
import org.csu.cs517.tmcs.input.event.EventParser;
import org.csu.cs517.tmcs.input.event.EventQueue;
import org.csu.cs517.tmcs.input.event.InputEvent;
import org.csu.cs517.tmcs.input.event.OpenRoute;
import org.csu.cs517.tmcs.input.event.OpenStation;
import org.csu.cs517.tmcs.input.event.Restart;
import org.csu.cs517.tmcs.input.event.Stop;
import org.csu.cs517.tmcs.input.event.SubmitJourney;
import org.csu.cs517.tmcs.map.Availability;
import org.csu.cs517.tmcs.map.RailroadSystemMap;
import org.csu.cs517.tmcs.map.RailroadSystemMapParser;
import org.csu.cs517.tmcs.map.Route;
import org.csu.cs517.tmcs.map.Segment;
import org.csu.cs517.tmcs.map.Station;
import org.csu.cs517.tmcs.map.TrafficLightColor;
import org.csu.cs517.tmcs.system.event.ChangeLight;
import org.csu.cs517.tmcs.system.event.IChangeLightListener;
import org.csu.cs517.tmcs.system.event.SystemEventGenerator;
import org.csu.cs517.tmcs.train.Journey;
import org.csu.cs517.tmcs.train.Train;

/**
 * The train management and control system (TMCS). Simulates the TMCS.
 */
public class Tmcs {
  
  private static Map<String, Train> trains = new TreeMap<>();
  private static RailroadSystemMap railroadSystemMap;
  private static EventQueue eventQueue;
  private static Logger logger = Logger.getLogger(Tmcs.class);
  private static List<InputEvent> inputEventsProcessed = new ArrayList<>();
  private static List<ChangeLight> outputChangeLightEvents = new ArrayList<>();
  
  static {
    @SuppressWarnings("unused")
    IChangeLightListener cl = new IChangeLightListener() {
      @Override
      public void lightChange(ChangeLight changeLight) {
        Tmcs.outputChangeLightEvents.add(changeLight);
      }
    };
    SystemEventGenerator.getInstance().addChangeLightListener(cl);
  }

  public static void main(String[] args) {
    try {
      RailroadSystemMapParser railroadSystemMapParser = null;
      MainMenu mm = new MainMenu();
      mm.printMainMenu();
      Integer mainMenuSelection = mm.getNumericalInput(2);
      if (mainMenuSelection.equals(2)) {
        String systemDefFile = mm.getValidFile("System definition map");
        railroadSystemMapParser = new RailroadSystemMapParser(systemDefFile);
      } else {
        railroadSystemMapParser = new RailroadSystemMapParser();
      }

      Tmcs.railroadSystemMap = railroadSystemMapParser.getMap();
      // Print the system map
      System.out.println(Tmcs.railroadSystemMap + Strings.NL);

      EventParser eventParser = null;
      mm.printEventMenu();
      Integer eventMenuSelection = mm.getNumericalInput(2);
      if (eventMenuSelection.equals(2)) {
        String eventFile = mm.getValidFile("Event file");
        // Load in Events
        eventParser = new EventParser(eventFile);
      } else {
        eventParser = new EventParser();
      }

      eventQueue = eventParser.getEventQueue();

      runSimulationLoop();
    } catch (Exception e) {
      //logger.error(e); // This is complaining that log4j is not configured.
      e.printStackTrace();
    }
    System.out.println("Exiting TMCS Simulation");
  }

  private static void runSimulationLoop() {
    Tmcs.printSystemState(false);
    InputEvent currentEvent = eventQueue.getNextEvent();
    while (currentEvent != null) {
      if (currentEvent instanceof SubmitJourney) {
        SubmitJourney submitJourney = (SubmitJourney)currentEvent;
        Tmcs.submitJourney(submitJourney);
      } else if (currentEvent instanceof CloseRoute) {
        CloseRoute closeRoute = (CloseRoute)currentEvent;
        Tmcs.railroadSystemMap.closeRoute(closeRoute);
      } else if (currentEvent instanceof OpenRoute) {
        OpenRoute openRoute = (OpenRoute)currentEvent;
        Tmcs.railroadSystemMap.openRoute(openRoute);
      } else if (currentEvent instanceof CloseStation) {
        CloseStation closeStation = (CloseStation)currentEvent;
        Tmcs.railroadSystemMap.closeStation(closeStation);
      } else if (currentEvent instanceof OpenStation) {
        OpenStation openStation = (OpenStation)currentEvent;
        Tmcs.railroadSystemMap.openStation(openStation);        
      } else if (currentEvent instanceof Stop) {
        Stop stop = (Stop)currentEvent;
        Tmcs.stopTrain(stop);
      } else if (currentEvent instanceof Restart) {
        Restart restart = (Restart)currentEvent;
        Tmcs.restartTrain(restart);
      } else if (currentEvent instanceof End) {
        Tmcs.moveTrains();
        SimulationTime.increment();
        Tmcs.printSystemState(false);
        
      }
      Tmcs.inputEventsProcessed.add(currentEvent);
      currentEvent = eventQueue.getNextEvent();
    }
    Tmcs.printSystemState(true);
  }
  
  /**
   * If the journey is valid, it creates a train on the start station into
   * the system and the train has an approved journey.
   * @param submitJourney Journey event to be submitted
   */
  private static void submitJourney(SubmitJourney submitJourney) {
    String startTimeStr = submitJourney.getStartTime();
    int startTimeInt = new Integer(startTimeStr);
    String trainId = submitJourney.getTrainId();
    List<String> routeIds = submitJourney.getRouteIds();
    List<String> stopStationIds = submitJourney.getStopStationIds();
    // Get start and end stations
    String firstRouteId = routeIds.get(0);
    String lastRouteId = routeIds.get(routeIds.size() - 1);
    Route firstRoute = Tmcs.railroadSystemMap.getRoute(firstRouteId);
    Route lastRoute = Tmcs.railroadSystemMap.getRoute(lastRouteId);
    
    // Get journey parameters from submit journey event
    Time startTime = new Time(startTimeInt);
    Station startStation = firstRoute.getStartStation();
    Train train = Tmcs.trains.get(trainId);
    if (train == null) {
      // Introduce a new train into the system.
      train = new Train(trainId, startStation.getPlatform());
    }
    Station endStation = lastRoute.getEndStation();
    List<Station> stops = new ArrayList<>();
    for (String stationId : stopStationIds) {
      Station stop = Tmcs.railroadSystemMap.getStation(stationId);
      stops.add(stop);
    }
    List<Route> routes = new ArrayList<>();
    for (String routeId : routeIds) {
      Route route = Tmcs.railroadSystemMap.getRoute(routeId);
      routes.add(route);
    }
    
    Journey journey = 
        Tmcs.validateJourney(
            startTime, train, startStation, endStation, routes, stops);
    if (journey != null) {
      train.setJourney(journey);
      Tmcs.trains.put(train.getId(), train);
    }    
  }
  
  /**
   * Validate a journey by ensuring the following: 
   *  - No routes are closed 
   *  - No stations are closed 
   *  - All stop stations are in journey routes.
   * 
   * @param startTime
   *          Time units after approval time of journey before train starts to
   *          traverse journey.
   * @param train
   *          The train submitting the journey.
   * @param startStation
   *          Start station of the journey.
   * @param endStation
   *          End station of the journey.
   * @param routes
   *          Routes to be traversed by the train.
   * @param stops
   *          Stops stations that the train will be stopping at.
   * @return A valid Journey object with an approved time, otherwise null if the
   *         journey cannot be validated.
   */
  private static Journey validateJourney(Time startTime, Train train,
      Station startStation, Station endStation, List<Route> routes,
      List<Station> stops) {
    // Validate no other journey with same 
    
    // Validate no closed routes
    for (Route route : routes) {
      if (route.getAvailability() == Availability.CLOSED) {
    	  logger.error("Unable to validate journey because route " + route
            + " of submitted journey is closed.");
        return null;
      }
    }
    // Validate no closed stations and that each stop is in this list of routes.
    for (Station station : stops) {
      boolean foundInRoute = false;
      if (station.getAvailability() == Availability.CLOSED) {
    	  logger.error("Unable to validate journey because station "
            + station + " of submitted journey is closed.");
        return null;
      }
      int routeIdx = 0;
      int lastRouteIdx = routes.size() - 1;
      for (Route route : routes) {
        Station startStationRoute = route.getStartStation();
        Station endStationRoute = route.getEndStation();
        if (routeIdx == 0) {
          // Ignore start station of first route
          if (station == endStationRoute) {
            foundInRoute = true;
            break;
          }
        } else if (routeIdx == lastRouteIdx) {
          // Ignore end station of last route
          if (station == startStationRoute) {
            foundInRoute = true;
            break;
          }
        } else {
          if (station == startStationRoute || station == endStationRoute) {
            foundInRoute = true;
            break;
          }
        }
        routeIdx++;
      }
      if (!foundInRoute) {
    	  logger.error("Station " + station
            + " is not in list of submitted journey routes.");
        return null;
      } else {
        Time approvedTime = CurrentTmcsTime.get();
        Journey journey = new Journey(startTime, approvedTime, startStation, endStation, stops, routes);
        return journey;
      }
    }
    return null;
  }

  private static void restartTrain(Restart restart) {
    String trainId = restart.getTrainId();
    Train train = Tmcs.trains.get(trainId);
    train.restart();
  }

  private static void stopTrain(Stop stop) {
    String trainId = stop.getTrainId();
    Train train = Tmcs.trains.get(trainId);
    train.stop();
  }

  private static void moveTrains() {
    for (Entry<String, Train> tEntry : Tmcs.trains.entrySet()) {
      Train t = tEntry.getValue();
      Segment previous = t.getOnSegment();
      if (t.moveForward()) {
        Segment current = t.getOnSegment();
        Tmcs.switchTrafficLights(previous, current);
      }
    }
  }
  
  private static void switchTrafficLights(Segment previous, Segment current) {
    List<Segment> previousSegments = Tmcs.railroadSystemMap
        .getPrevious(current);
    // Turn the previous segments red
    for (Segment pSegment : previousSegments) {
      ChangeLight cl = new ChangeLight(pSegment, TrafficLightColor.RED);
      SystemEventGenerator.getInstance().generateChangeLightEvent(cl);
    }

    // Turn the previous of the previous lights to green unless the segment
    // is the platform of a station and there is at least one train on the
    // the first segment of an outgoing route.
    List<Segment> previousPreviousSegments = Tmcs.railroadSystemMap
        .getPrevious(previous);
    for (Segment previousPreviousSeg : previousPreviousSegments) {
      for (Segment nextPreviousPreviousSeg : previousPreviousSeg.getNext()) {
        if (nextPreviousPreviousSeg.getOccupiedBy() != null) {
          ChangeLight cl = new ChangeLight(
              previousPreviousSeg, TrafficLightColor.RED);
          SystemEventGenerator.getInstance().generateChangeLightEvent(cl);
          return;
        }
      }
    }
    // No trains occupy next segment, so set to green
    for (Segment previousPreviousSeg : previousPreviousSegments) {
      ChangeLight cl = new ChangeLight(
          previousPreviousSeg, TrafficLightColor.GREEN);
      SystemEventGenerator.getInstance().generateChangeLightEvent(cl);
    }
  }
  
  private static void printSystemState(boolean isSimulationEnd) {
  	StringBuilder sb = new StringBuilder();
  	if (!isSimulationEnd) {
  		sb.append("Start of simulation loop" + Strings.NL);
  	} else {
  		sb.append("End of Simulation" + Strings.NL);
  	}
  	sb.append("System Time: " + CurrentTmcsTime.get() + Strings.NL);
  	buildInputEventsHandledPrintString(sb);
  	sb.append(Strings.NL);
  	buildSystemOutputEventsGeneratedString(sb);
  	sb.append(Strings.NL);
  	buildPrintTrainsString(sb);
    sb.append(Strings.NL);
  	buildPrintRouteSegments(sb);
    sb.append(Strings.NL);
  	System.out.println(sb);
  }

  private static void buildInputEventsHandledPrintString(StringBuilder sb) {
    sb.append("Processed Input Events:" + Strings.NL);
    for (InputEvent ie : Tmcs.inputEventsProcessed) {
      sb.append("   " + ie + Strings.NL);
    }
  }
  
  private static void buildSystemOutputEventsGeneratedString(StringBuilder sb) {
    sb.append("System Generated Output Events:" + Strings.NL);
    for (ChangeLight cl : Tmcs.outputChangeLightEvents) {
      sb.append("   " + cl + Strings.NL);
    }
  }

  private static void buildPrintTrainsString(StringBuilder sb) {
    sb.append("Trains:" + Strings.NL);
  	sb.append("                                         Journey  Journey   Journey  Journey" + Strings.NL);
  	sb.append("         Is      On      On       At      Wait    Approved   Start     End" + Strings.NL);
  	sb.append("Train  Stopped  Route  Segment  Station   Time     Time     Station  Station" + Strings.NL);
  	sb.append("-----  -------  -----  -------  -------  -------  --------  -------  -------" + Strings.NL);
  	for (Entry<String, Train> tEntry : Tmcs.trains.entrySet()) {
  		Train train = tEntry.getValue();
  		boolean isStopped = train.isStopped();
  		Segment onSegment = train.getOnSegment();
  		Route onRoute = onSegment.getRoute();
  		String onRouteStr = Strings.EMPTY;
  		if (onRoute != null) {
  		  onRouteStr = onRoute.toString();
  		}
  		Station atStation = onSegment.getStation();
  		String atStationStr = Strings.EMPTY;
  		if (atStation != null) {
  		  atStationStr = atStation.toString();
  		}
  		Journey journey = train.getJourney();
  		Time journeyWaitTime = journey.getWaitTimeTillStart();
  		Time jorneyApprovedTime = journey.getApprovalTime();
  		Station jStartStation = journey.getStartStation();
  		Station jEndStation = journey.getEndStation();
  		List<Route> jRoutes = journey.getRoutes();
  		List<Station> jStops = journey.getStops();
  		String formattedStr = 
  		    String.format(
  				"%5s%9s%7s%9s%9s%9s%10s%9s%9s", 
  				train, isStopped, onRouteStr, onSegment, atStationStr, journeyWaitTime, 
  				jorneyApprovedTime, jStartStation, jEndStation);
  		sb.append(formattedStr);
  		sb.append("  Journey Routes:");
  		for (Route r : jRoutes) {
  		  sb.append(Strings.SP + r);
  		}
  		sb.append("; Journey Stops:");
  		for (Station s : jStops) {
  		  sb.append(Strings.SP + s);
  		}
  		sb.append(Strings.NL);
  	}
  }
  
  private static void buildPrintRouteSegments(StringBuilder sb) {
    sb.append("Station / Route Segments Traffic Light Color:" + Strings.NL);
    sb.append("Station   Route   Seg. #   Color" + Strings.NL);
    sb.append("-------   -----   ------   -----" + Strings.NL);
    Map<String, Station> stations = Tmcs.railroadSystemMap.getStations();
    for (Entry<String, Station> stationEntry : stations.entrySet()) {
      Station station = stationEntry.getValue();
      Segment platform = station.getPlatform();
      String formattedStr = String.format(
          "%7s%17s%8s", station, platform, platform.getTrafficLightColor());
      sb.append(formattedStr + Strings.NL);
    }
    Map<String, Route> routes = Tmcs.railroadSystemMap.getRoutes();
    for (Entry<String, Route> routeEntry : routes.entrySet()) {
      Route route = routeEntry.getValue();
      for (Segment segment : route.getSegments()) {
        String formattedStr = 
            String.format("%15s%9s%8s", route, segment, segment.getTrafficLightColor());
        sb.append(formattedStr + Strings.NL);
      }
    }

  }
}
