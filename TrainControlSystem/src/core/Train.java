package core;

public class Train {
	private int trainId;
	private Journey journey;
	private Segment onSegment;
	
	/**
	 * @param trainId
	 * @param onSegment
	 */
	public Train(int trainId, Segment onSegment) {
		this.trainId = trainId;
		this.onSegment = onSegment;
	}


	/**
	 * @return the trainId
	 */
	public int getTrainId() {
		return trainId;
	}

	/**
	 * @param trainId the trainId to set
	 */
	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	/**
	 * @return the journey
	 */
	public Journey getJourney() {
		return journey;
	}

	/**
	 * @param journey the journey to set
	 */
	public void setJourney(Journey journey) {
		this.journey = journey;
	}

	/**
	 * @return the onSegment
	 */
	public Segment getOnSegment() {
		return onSegment;
	}

	/**
	 * @param onSegment the onSegment to set
	 */
	public void setOnSegment(Segment onSegment) {
		this.onSegment = onSegment;
	}
}
