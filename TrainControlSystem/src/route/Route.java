/**
 * 
 */
package route;

import java.util.HashMap;
import java.util.Map;

import segment.Segment;
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
	private Map<Integer, Segment> segments;
	public Route(int routeId, Status status, Time closeTime, Station start, Station end, 
			int numberOfSegments) throws TCSException {
		if(numberOfSegments<=2) {
			throw new TCSException("Number of segments should be greater than 2");
		}
		this.routeId = routeId;
		this.status = status;
		this.closeTime = closeTime;
		this.start = start;
		this.end = end;
		segments = new HashMap<>();
		int segmentOrder = 0;
		segments.put(segmentOrder, start.getPlatform());
		for(segmentOrder=1;segmentOrder<=numberOfSegments;segmentOrder++){
			Segment segment = new Segment(segmentOrder);
			segments.put(segmentOrder, segment);
			segments.get(segmentOrder-1).setNext(segment);
		}
		segments.put(segmentOrder, end.getPlatform());
		segments.get(segmentOrder-1).setNext(end.getPlatform());
		
	}
	
	public Route(Integer routeId){
		this.routeId = routeId;
	}
	
	public Route(String data){
		String[] tokens = data.split("[()]");
		this.routeId = new Integer(tokens[1]).intValue();
	}
	
	

	
	
	/**
	 * @return the routeId
	 */
	public int getRouteId() {
		return routeId;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
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
	 * @return the start
	 */
	public Station getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public Station getEnd() {
		return end;
	}
	public Segment getSegment(Integer sequence){
		Segment segment = null;
		if(containsSegment(sequence)){
			segment = segments.get(sequence);
		}
		return segment;
	}
	public boolean containsSegment(Integer sequence){
		return segments.containsKey(sequence);
	}
	public Integer getNextSegementId(Integer currentSegment) {
		Integer nextSegId = null;
		if(!(currentSegment>segments.size())){
			nextSegId = currentSegment+1;
		}
		return nextSegId;
	}


}
