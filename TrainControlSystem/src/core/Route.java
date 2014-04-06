package core;

import java.util.List;

import segment.Segment;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import common.TCSException;

public class Route {
	
	private int routeId;
	private Status status;
	private Time closeTime;
	private Station start;
	private Station end;
	private BiMap<Segment, Integer> segmentOrder;
	
	public Route(int routeId, Status status, Time closeTime, Station start, Station end, 
			List<Segment> orderedSegments) throws TCSException {
		if(orderedSegments == null || orderedSegments.size()<=2) {
			throw new TCSException("Number of segments should be greater than 2");
		}
		this.routeId = routeId;
		this.status = status;
		this.closeTime = closeTime;
		this.start = start;
		this.end = end;
		createSegmentOrder(orderedSegments);
	}
	/**
	 * @param orderedSegments
	 */
	private void createSegmentOrder(List<Segment> orderedSegments) {
		segmentOrder = HashBiMap.create();
		int orderedSegSize = orderedSegments.size();
		for (int i = 0; i < orderedSegSize - 1; i++) {
			segmentOrder.put(orderedSegments.get(i), i + 1);
		}
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


}
