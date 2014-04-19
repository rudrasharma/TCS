package core;

import common.TCSException;

import segment.Segment;

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
	
	public Train(String data) {
		String[] tokens = data.split("[()]");
		this.trainId = new Integer(tokens[1]).intValue();
	}
	public boolean move() throws TCSException{
		if(onSegment.getNext() == null){
			throw new TCSException("The next segment is undefined");
		}
		boolean success = false;
		if(onSegment.getNext().getEntrySignal() == Signal.GREEN){
			onSegment.setHoldingTrain(false);
			onSegment.getNext().setHoldingTrain(true);
			success = true;
		}
		return success;
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

	@Override
	public String toString()
	{
		return "Train [trainId=" + trainId + ", journey=" + journey
				+ ", onSegment=" + onSegment + "]";
	}
	
}
