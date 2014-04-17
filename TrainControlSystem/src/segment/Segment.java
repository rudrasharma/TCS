package segment;

import common.TCSException;
import core.Signal;
/**
 * 
 */
public class Segment {
	private int segmentId;
	private Segment next;
	private boolean holdingTrain;
	
	public Segment(int segmentId){
		this.segmentId = segmentId;
		holdingTrain = false;
	}
	protected boolean traverse() throws TCSException{
		if(next == null){
			throw new TCSException("The next segment is undefined");
		}
		boolean success = false;
		if(next.getEntrySignal() == Signal.GREEN){
			this.holdingTrain = false;
			next.setHoldingTrain(true);
			success = true;
		}
		return success;
		
	}
	
	protected Signal getEntrySignal() throws TCSException{
		if(!holdingTrain){
			return Signal.GREEN;
		}
		return Signal.RED;
	}
	
	/**
	 * @return the holdingTrain
	 */
	public boolean isHoldingTrain() {
		return holdingTrain;
	}

	/**
	 * @param holdingTrain the holdingTrain to set
	 */
	public void setHoldingTrain(boolean holdingTrain) {
		this.holdingTrain = holdingTrain;
	}

	/**
	 * @return the segmentId
	 */
	public int getSegmentId() {
		return segmentId;
	}

	/**
	 * @param segmentId the segmentId to set
	 */
	public void setSegmentId(int segmentId) {
		this.segmentId = segmentId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + segmentId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Segment other = (Segment) obj;
		if (segmentId != other.segmentId)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Segment [segmentId=" + segmentId + ", holdingTrain="
				+ holdingTrain + "]";
	}

	/**
	 * @return the next
	 */
	public Segment getNext() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(Segment next) {
		this.next = next;
	}

}
