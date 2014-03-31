package core;

import java.util.List;

public class Journey {
	private Time startTime;
	private Station start;
	private Station end;
	private List<Station> stops;
	private List<Route> routes;
	
	public Journey(Time startTime, Station start, Station end, List<Station> stops, 
			List<Route> routes) throws TCSException{
		if(routes == null || routes.size()<=1) {
			throw new TCSException("The number of routes should be greater than 1");
		}
		
		this.startTime = startTime;
		this.start = start;
		this.end = end;
		this.stops = stops;
		this.routes = routes;
	}
	/**
	 * @return the start
	 */
	public Station getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Station start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public Station getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(Station end) {
		this.end = end;
	}
	/**
	 * @return the stop
	 */
	public List<Station> getStops() {
		return stops;
	}
	/**
	 * @param stop the stop to set
	 */
	public void setStop(List<Station> stops) {
		this.stops = stops;
	}
	/**
	 * @return the startTime
	 */
	public Time getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the routes
	 */
	public List<Route> getRoutes() {
		return routes;
	}
	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
}
