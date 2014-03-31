package core;

public class RouteStation {
	private StationType stationType;
	private Station station;

	public RouteStation(StationType stationType, Station station) {
		this.stationType = stationType;
		this.station = station;
	}
	public StationType getStationType() {
		return stationType;
	}
	/**
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}
}
