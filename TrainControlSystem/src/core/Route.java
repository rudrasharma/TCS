package core;

import java.util.List;

public class Route {
	
	private int routeId;
	private Status status;
	private Time closeTime;
	private Station start;
	private Station end;
	private List<Segment> segments;
	
	public Route(int routeId, Status status, Time closeTime, Station start, Station end, 
			List<Segment> segments) throws TCSException {
		if(segments == null || segments.size()<=1) {
			throw new TCSException("The number of segments should be greater than 1");
		}
		this.routeId = routeId;
		this.status = status;
		this.closeTime = closeTime;
		this.start = start;
		this.end = end;
		this.setSegments(segments);
	}
	/**
	 * @return the routeId
	 */
	public int getRouteId() {
		return routeId;
	}
	/**
	 * @param routeId the routeId to set
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
	public Time getCloseTime() {
		return closeTime;
	}
	/**
	 * @param closeTime the closeTime to set
	 */
	public void setCloseTime(Time closeTime) {
		this.closeTime = closeTime;
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
