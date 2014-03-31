package core;
import java.util.ArrayList;
import java.util.List;


public class Journey {
	private TimeUnit startTime;
	private JourneyStation startStation;
	private JourneyStation endStation;
	private List<JourneyStation> stopStations;

	public Journey(JourneyStation start, JourneyStation end,JourneyStation... stops){
		this.startStation = start;
		this.endStation = end;
		stopStations = new ArrayList<>();
		for(JourneyStation station: stops){
			stopStations.add(station);
		}
	}
	/**
	 * @return the startTime
	 */
	public TimeUnit getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(TimeUnit startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the startStation
	 */
	public JourneyStation getStartStation() {
		return startStation;
	}

	/**
	 * @param startStation the startStation to set
	 */
	public void setStartStation(JourneyStation startStation) {
		this.startStation = startStation;
	}

	/**
	 * @return the endStation
	 */
	public JourneyStation getEndStation() {
		return endStation;
	}

	/**
	 * @param endStation the endStation to set
	 */
	public void setEndStation(JourneyStation endStation) {
		this.endStation = endStation;
	}
}
