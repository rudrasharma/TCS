package core;

public class JourneyStation {
	
	private StationType stationType;
	private Station station;
	
	public JourneyStation(StationType stationType, Station station){
		this.stationType = stationType;
		this.setStation(station);
		
	}

	/**
	 * @return the stationType
	 */
	public StationType getStationType() {
		return stationType;
	}

	/**
	 * @param stationType the stationType to set
	 */
	public void setStationType(StationType stationType) {
		this.stationType = stationType;
	}

	/**
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @param station the station to set
	 */
	public void setStation(Station station) {
		this.station = station;
	}

}
