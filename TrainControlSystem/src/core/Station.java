package core;

public class Station {
	private int stationId;
	private Status status;
	private TimeUnit closeTime;
	/**
	 * @return the id
	 */
	public int getStationId() {
		return stationId;
	}
	/**
	 * @param id the id to set
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
	public TimeUnit getCloseTime() {
		return closeTime;
	}
	/**
	 * @param closeTime the closeTime to set
	 */
	public void setCloseTime(TimeUnit closeTime) {
		this.closeTime = closeTime;
	}
}
