/**
 * 
 */
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
	//segment id, and sequence Id
	private BiMap<Integer, Integer> segmentOrder;
	private SegmentManager segmentManager;
	
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
	
	public Route(int routeId){
		this.routeId = routeId;
	}
	
	public Route(String data){
		String[] tokens = data.split("[()]");
		this.routeId = new Integer(tokens[1]).intValue();
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
	
	public Integer getNextSegementId(Integer routeId, Integer currentSequence) throws TCSException{
		Integer next = null;
		if(!segmentOrder.containsKey(routeId)){
			throw new TCSException("Route id", routeId);
		}
		if(!segmentOrder.inverse().containsKey(currentSequence)){
			throw new TCSException("Current sequence", currentSequence);
		}
		if(currentSequence < segmentOrder.size()){
			next = segmentOrder.inverse().get(currentSequence+1);
		}//return null if the current sequence is the last sequence
		return next;
		

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
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	protected void setStatus(Status status) {
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


}
