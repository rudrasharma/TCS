package simulation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import common.InvalidJourneyException;
import common.TCSException;
import core.Journey;
import core.Station;
import core.StationManager;
import core.Status;
import core.Time;
import core.Train;
import core.TrainManager;
import route.Route;
import route.RouteManager;
import startup.ConfigLoader;


public class TCSSimulator {
	private static RouteManager rm = RouteManager.getInstance();
	private static StationManager sm = StationManager.getInstance();
	private static TrainManager tm = TrainManager.getInstance();
	private static List<Entry<Event, String>> generateList(String location)
			throws IOException {
		List<Entry<Event, String>> eventList = new ArrayList<>();
		List<String> fileLines = Files.readAllLines(Paths.get(location), Charset.defaultCharset());
		for(String line: fileLines){
				eventList.add(getEvent(line));

		}

		return eventList;
	}
	private static Entry<Event, String> getEvent(String line){
		Event event = null;
		String eventData = null;
		if (line != null && !line.isEmpty()) {
			if (line.startsWith(Event.SUBMIT_JOURNEY.getName())) {
				event = Event.SUBMIT_JOURNEY;
			} else if (line.startsWith(Event.OPEN_ROUTE.getName())) {
				event = Event.OPEN_ROUTE;
			} else if (line.startsWith(Event.CLOSE_ROUTE.getName())) {
				event = Event.CLOSE_ROUTE;
			} else if (line.startsWith(Event.RESTART.getName())) {
				event = Event.RESTART;
			} else if (line.startsWith(Event.STOP.getName())) {
				event = Event.STOP;
			} else if (line.startsWith(Event.CLOCK_TICK.getName())) {
				event = Event.CLOCK_TICK;
			}
		}
		if(event != null){
			eventData = line.substring(event.getName().length());
		}
		Entry<Event, String> eventEntry = new SimpleEntry<Event, String>(event, eventData);
		return eventEntry;
		
	}

	public static void main(String[] args) throws TCSException, InvalidJourneyException {
		ConfigLoader configLoader = new ConfigLoader();
		try {
			configLoader.load();
			String fileLocation = null;
			if (args != null && args.length > 0) {
				fileLocation = args[0];
			} else {
				System.out
						.println("Please provide the input simulation file location");
				System.exit(1);
			}

			List<Entry<Event, String>> eventList = null;

			try {
				eventList = generateList(fileLocation);
				processEvents(eventList);
				//System.out.println(eventList.toString());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {


		}
		}
	private static void processEvents(List<Entry<Event, String>> eventList) throws TCSException, InvalidJourneyException   {
		for(Entry<Event,String> entry: eventList){
			Event event = entry.getKey();
			String eventData = entry.getValue();
			int id;
			switch(event){
				case OPEN_ROUTE: 
					id = extractId(eventData);
				try {
					rm.get(id).setStatus(Status.OPEN);
				} catch (TCSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					System.out.println("event:"+event.getName()+" id:"+id);
					break;
				case CLOSE_ROUTE: 
					id = extractId(eventData);
					try {
						rm.get(id).setStatus(Status.CLOSED);
					} catch (TCSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("event:"+event.getName()+" id:"+id);
					break;

				case RESTART:
					id = extractId(eventData);
					System.out.println("event:"+event.getName()+" id:"+id);
					break;
				 case STOP:
						id = extractId(eventData);
						System.out.println("event:"+event.getName()+" id:"+id);
						break;
					 
				case SUBMIT_JOURNEY:
					processJourney(eventData);
				default:
					break;
			}
		}
		
	}
	private static void processJourney(String eventData) throws TCSException {
		String[] tokens = eventData.split("[,()]");
		
		int trainId =new Integer(tokens[1]).intValue();
		Train train = tm.get(trainId);
		
		int startTime = new Integer(tokens[2]).intValue();
		Time start = new Time(startTime);
		
		//Start index for routes from the split tokens
		int i=4;
		
		List<Route> routes = new ArrayList<>();
		int routeId = 0;
		for (;i<tokens.length && !tokens[i].trim().isEmpty() && i< tokens.length; i++)
		{
			routeId = new Integer(tokens[i]).intValue();
			routes.add(rm.get(routeId));
		}
		
		//Go two indexes to get station id's
		i = i+2;
		
		List<Station> stops = new ArrayList<Station>();
		for (;i<tokens.length && !tokens[i].trim().isEmpty(); i++)
		{
			System.out.print(" Stop station:"+tokens[i]);
			stops.add(sm.get(tokens[i]));
		}
		System.out.println();
		try {
			Journey journey = new Journey(start,
					routes.get(0).getStart(),
					routes.get(routes.size()-1).getEnd(),
					stops,
					routes,
					train);
		} catch (InvalidJourneyException e) {
			System.out.println("The following journey cound not be processed:"+eventData+" due to the following issue: \n"+e.getMessage());
		}
		
	}
	private static int extractId(String eventData) {
		int id;
		String[] tokens = eventData.split("[()]");
		id = new Integer(tokens[1]).intValue();
		return id;
	}

}
