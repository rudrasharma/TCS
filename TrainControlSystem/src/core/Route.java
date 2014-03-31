package core;
import java.util.ArrayList;
import java.util.List;


public class Route {
	private int routeId;
	private Status status;
	private TimeUnit closeTime;
	private RouteStation startStation;
	private RouteStation endStation;
	private List<Segment> segments;

	public Route(Station start, Station end, Segment... segments){
		this.startStation = new RouteStation(StationType.START, start);
		this.endStation = new RouteStation(StationType.END, end);
		this.segments = new ArrayList<>();
		for(Segment segment: segments){
			this.segments.add(segment);
		}
		
	}
	/**
	 * @return the id
	 */
	public int getRouteId() {
		return routeId;
	}

	/**
	 * @param id the id to set
	 */
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the closeTime
	 */
	public TimeUnit getCloseTime() {
		return closeTime;
	}

	/**
	 * @param closeTime the closeTime to set
	 */
	public void setCloseTime(TimeUnit closeTime) {
		this.closeTime = closeTime;
	}
	/**
	 * @return the startStation
	 */
	public RouteStation getStartStation() {
		return startStation;
	}
	/**
	 * @param startStation the startStation to set
	 */
	public void setStartStation(RouteStation startStation) {
		this.startStation = startStation;
	}
	/**
	 * @return the endStation
	 */
	public RouteStation getEndStation() {
		return endStation;
	}
	/**
	 * @param endStation the endStation to set
	 */
	public void setEndStation(RouteStation endStation) {
		this.endStation = endStation;
	}
	/**
	 * @return the segments
	 */
	public List<Segment> getSegments() {
		return segments;
	}
	/**
	 * @param segments the segments to set
	 */
	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}



}
