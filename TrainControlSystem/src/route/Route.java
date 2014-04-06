package route;

import java.util.List;

import segment.SegmentManager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import common.TCSException;
import core.Station;
import core.Status;
import core.Time;

public class Route {
	
	private int routeId;
	private Status status;
	private Time closeTime;
	private Station start;
	private Station end;
	private BiMap<Integer, Integer> segmentOrder;
	SegmentManager segmentManager;
	public Route(int routeId, Status status, Time closeTime, Station start, Station end, 
			List<Integer> orderedSegmentIds) throws TCSException {
		if(orderedSegmentIds == null || orderedSegmentIds.size()<=2) {
			throw new TCSException("Number of segments should be greater than 2");
		}
		this.routeId = routeId;
		this.status = status;
		this.closeTime = closeTime;
		this.start = start;
		this.end = end;
		this.segmentManager = SegmentManager.getInstance();
		createSegmentSequence(orderedSegmentIds);
	}
	/**
	 * @param orderedSegments
	 * @throws TCSException 
	 */
	private void createSegmentSequence(List<Integer> orderedSegmentIds) throws TCSException  {
		segmentOrder = HashBiMap.create();
		
		int orderedSegSize = orderedSegmentIds.size();
		
		//do the first segment without any incoming validation
		Integer firstSegId = orderedSegmentIds.get(0);
		segmentManager.validateExisting(firstSegId);//throws TCSException if invalid
		putSegment(firstSegId, 0);
		
		for (int i = 1; i < orderedSegSize; i++) {
			//before we can add it to the map we make sure that the previous segment has the next segment
			//as part of its outgoing
			Integer currentSegId = orderedSegmentIds.get(i);
			Integer previousSegId = segmentOrder.inverse().get(i-1);
			segmentManager.validateOutgoing(previousSegId, currentSegId);//throws TCSException if invalid
			
			putSegment(orderedSegmentIds.get(i), i);
		}
	}
	private void putSegment(Integer segmentId, Integer sequence){
		segmentOrder.put(segmentId, sequence);
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
