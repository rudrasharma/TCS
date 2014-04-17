package core;

import java.util.HashMap;
import java.util.Map;

public class StationManager {
	private Map<String, Station> stations;
	private static StationManager instance;
	
	public static StationManager getInstance(){
		if(instance == null){
			instance = new StationManager();
		}
		return instance;
	}
	public StationManager(){
		stations = new HashMap<>();
	}
	public void addStation(Station station){
		stations.put(station.getStationId(), station);
	}
	
	public Station getStation(String stationId){
		Station station = null;
		if(stations.containsKey(stationId)){
			station = stations.get(stationId);
		}
		return station;
	}
	public boolean stationExists(String stationId){
		return stations.containsKey(stationId);
	}
	
}
