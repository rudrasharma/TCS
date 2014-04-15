package simulation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import route.Route;
import core.Journey;
import core.Train;


public class TCSSimulator {
	private static Map<Event, Object> simulate(String location)
			throws IOException {
		Map<Event, Object> eventMap = new HashMap<Event, Object>();
		BufferedReader br = new BufferedReader(new FileReader(location));

		String line = br.readLine();
		try {
			while (line != null) {
				Event event = null;
				Object data = null;

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

				System.out.println("Event:" + event.getName());
				if (data != null)
					System.out.println("Data{ " + data.toString() + " }");

				eventMap.put(event, data);
				line = br.readLine();
			}
		} finally {
			br.close();
		}

		return eventMap;
	}

	public static void main(String[] args) {
		String fileLocation = null;
		if (args != null && args.length > 0) {
			fileLocation = args[0];
		} else {
			System.out
					.println("Please provide the input simulation file location");
			System.exit(1);
		}

		Map<Event, Object> eventMap = null;

		try {
			eventMap = simulate(fileLocation);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
