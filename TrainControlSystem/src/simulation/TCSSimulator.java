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


public class TCSSimulator {
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
	/*private Event getEvent(String line){
		if (line != null && !line.isEmpty()) {
			if (line.startsWith(Event.SUBMIT_JOURNEY.getName())) {
				event = Event.SUBMIT_JOURNEY;
				data = new Journey(line.substring(Event.SUBMIT_JOURNEY
						.getName().length()));
			} else if (line.startsWith(Event.OPEN_ROUTE.getName())) {
				event = Event.OPEN_ROUTE;
				data = new Route(line.substring(Event.OPEN_ROUTE
						.getName().length()));
			} else if (line.startsWith(Event.CLOSE_ROUTE.getName())) {
				event = Event.CLOSE_ROUTE;
				data = new Route(line.substring(Event.CLOSE_ROUTE
						.getName().length()));
			} else if (line.startsWith(Event.RESTART.getName())) {
				event = Event.RESTART;
				data = new Train(line.substring(Event.RESTART
						.getName().length()));
			} else if (line.startsWith(Event.STOP.getName())) {
				event = Event.STOP;
				data = new Train(line.substring(Event.STOP.getName()
						.length()));
			} else if (line.startsWith(Event.CLOCK_TICK.getName())) {
				event = Event.CLOCK_TICK;
			}
		}		
	}*/
	public static void main(String[] args) {
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
			System.out.println(eventList.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static void processEvents(List<Entry<Event, String>> eventList) {
		for(Entry<Event,String> entry: eventList){
			Event event = entry.getKey();
			String eventData = entry.getValue();
			/*Event.CLOCK_TICK
			switch(event.getName()){
			case Event.
			
			}*/
		}
		
	}

}
