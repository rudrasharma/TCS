package org.csu.cs517.tmcs.simulation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
  
  private static Map<String, Train> trains;
  private static RailroadSystemMap railroadSystemMap;
  private static EventQueue eventQueue;
  //private static Logger logger = Logger.getLogger(Tmcs.class);
  private static List<InputEvent> inputEventsProcessed = new ArrayList<>();
  private static List<ChangeLight> outputChangeLightEvents = new ArrayList<>();
  private static JourneyValidator journeyValidator;
  
  public static void main(String[] args) {
    try {
      Tmcs.init(args);


      runSimulationLoop();
    } catch (Exception e) {
      //logger.error(e); // This is complaining that log4j is not configured.
      e.printStackTrace();
    }
    System.err.flush();
    System.out.println("Exiting TMCS Simulation");
    System.out.flush();
  }
  
  private static void init(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    boolean noMenu = false;
    if (args.length > 0 && args[0].equals("-nm")) {
      noMenu = true;
    }
    RailroadSystemMapParser railroadSystemMapParser = null;
    MainMenu mm = new MainMenu();
    if (!noMenu) {
      mm.printMainMenu();
      Integer mainMenuSelection = mm.getNumericalInput(2);
      if (mainMenuSelection.equals(2)) {
        String systemDefFile = mm.getValidFile("System definition map");
        railroadSystemMapParser = new RailroadSystemMapParser(systemDefFile);
      } else {
        railroadSystemMapParser = new RailroadSystemMapParser();
      }
    } else {
      railroadSystemMapParser = new RailroadSystemMapParser();
    }

    Tmcs.railroadSystemMap = railroadSystemMapParser.getMap();
    // Print the system map
    System.out.println(Tmcs.railroadSystemMap + Strings.NL);

    EventParser eventParser;
    if (!noMenu) {
      mm.printEventMenu();
      Integer eventMenuSelection = mm.getNumericalInput(2);
      if (eventMenuSelection.equals(2)) {
        String eventFile = mm.getValidFile("Event file");
        // Load in Events
        eventParser = new EventParser(eventFile);
      } else {
        eventParser = new EventParser();
      }
    } else {
      eventParser = new EventParser();
    }
    IChangeLightListener cl = new IChangeLightListener() {
      @Override
      public void lightChange(ChangeLight changeLight) {
        Tmcs.outputChangeLightEvents.add(changeLight);
      }
    };
    SystemEventGenerator.getInstance().addChangeLightListener(cl);
    Tmcs.trains = new TreeMap<>();
    Tmcs.journeyValidator = new JourneyValidator(trains, railroadSystemMap);
    eventQueue = eventParser.getEventQueue();
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
        Tmcs.inputEventsProcessed.clear();
        Tmcs.outputChangeLightEvents.clear();
      }
      Tmcs.inputEventsProcessed.add(currentEvent);
      currentEvent = eventQueue.getNextEvent();
    }
    Tmcs.printSystemState(true);
  }
  
  private static void submitJourney(SubmitJourney submitJourney) {
    Journey journey = Tmcs.journeyValidator.submitForJourney(submitJourney);
    String validationMessage = Tmcs.journeyValidator.getValidationMessage();
    submitJourney.setValidationMessage(validationMessage);
    boolean trainExists = Tmcs.journeyValidator.trainExists();
    if(!trainExists && journey != null) {
      // A train with this ID doesn't exist, so create it in the system.
      // Generate a new train at start station of Journey
      createTrainInSystem(submitJourney, journey);
    }
  }

  private static void createTrainInSystem(
      SubmitJourney submitJourney, Journey journey) {
    Station startStation = journey.getStartStation();
    Segment platform = startStation.getPlatform();
    String trainId = submitJourney.getTrainId();
    Train train = new Train(trainId, platform);
    train.setJourney(journey);
    Tmcs.trains.put(trainId, train);
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
  	buildPrintSegments(sb);
    sb.append(Strings.NL);
    buildPrintStationAndRouteAvailabilities(sb);
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
  
  private static void buildPrintSegments(StringBuilder sb) {
    sb.append("Segments Traffic Light Color:" + Strings.NL);
    sb.append("Station/                " + Strings.NL);
    sb.append(" Route    Seg. #   Color" + Strings.NL);
    sb.append("-------   ------   -----" + Strings.NL);
    Map<String, Station> stations = Tmcs.railroadSystemMap.getStations();
    for (Entry<String, Station> stationEntry : stations.entrySet()) {
      Station station = stationEntry.getValue();
      Segment platform = station.getPlatform();
      String formattedStr = String.format(
          "%7s%9s%8s", station, platform, platform.getTrafficLightColor());
      sb.append(formattedStr + Strings.NL);
    }
    Map<String, Route> routes = Tmcs.railroadSystemMap.getRoutes();
    for (Entry<String, Route> routeEntry : routes.entrySet()) {
      Route route = routeEntry.getValue();
      for (Segment segment : route.getSegments()) {
        String formattedStr = 
            String.format("%7s%9s%8s", route, segment, segment.getTrafficLightColor());
        sb.append(formattedStr + Strings.NL);
      }
    }
  }

  private static void buildPrintStationAndRouteAvailabilities(StringBuilder sb) {
    sb.append("Station/Route Availabilities:" + Strings.NL);
    sb.append("Station/   Open/" + Strings.NL);
    sb.append(" Route     Close" + Strings.NL);
    sb.append("--------   -----" + Strings.NL);
    Map<String, Station> stations = Tmcs.railroadSystemMap.getStations();
    Availability availability = null;
    for (Entry<String, Station> stationEntry : stations.entrySet()) {
      Station station = stationEntry.getValue();
      availability = station.getAvailability();
      String formattedStr = String.format("%8s%8s", station, availability);
      sb.append(formattedStr + Strings.NL);
    }
    Map<String, Route> routes = Tmcs.railroadSystemMap.getRoutes();
    for (Entry<String, Route> routeEntry : routes.entrySet()) {
      Route route = routeEntry.getValue();
      availability = route.getAvailability();
      String formattedStr = String.format("%8s%8s", route, availability);
      sb.append(formattedStr + Strings.NL);
    }
  }
}
