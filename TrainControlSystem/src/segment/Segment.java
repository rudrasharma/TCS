package segment;

import core.Signal;

public class Segment {
	private int segmentId;
	private Signal light;
	private boolean holdingTrain;
	
	protected boolean canEnter(){
		boolean allow = false;
		if(!holdingTrain){
			allow = true;
		}
		return allow;
	}
	
	protected boolean canExit(){
		boolean allow = false;
		if(light == Signal.GREEN){
			allow = true;
		}
		return allow;
	}

	/**
	 * @return the light
	 */
	public Signal getLight() {
		return light;
	}

	/**
	 * @param light the light to set
	 */
	public void setLight(Signal light) {
		this.light = light;
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

}
