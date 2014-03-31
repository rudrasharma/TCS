package core;

public class Segment {
	private Signal light;
	private boolean holdingTrain;

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

}
