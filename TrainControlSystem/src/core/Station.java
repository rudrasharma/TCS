package core;

public class Station {
	private int stationId;
	private Status status;
	private Time closeTime;
	/**
	 * @param stationId
	 * @param status
	 * @param closeTime
	 */
	public Station(int stationId, Status status, Time closeTime) {
		this.stationId = stationId;
		this.status = status;
		this.closeTime = closeTime;
	}

	/**
	 * @return the stationId
	 */
	public int getStationId() {
		return stationId;
	}
	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(int stationId) {
		this.stationId = stationId;
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
}
