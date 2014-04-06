package segment;

import common.TCSException;

import core.Signal;

public class Segment {
	private int segmentId;
	private boolean holdingTrain;
	
	public Segment(int segmentId){
		this.segmentId = segmentId;
		holdingTrain = false;
	}
	
	protected boolean canEnter(){
		boolean allow = false;
		if(!holdingTrain){
			allow = true;
		}
		return allow;
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

}
