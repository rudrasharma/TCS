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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stationId;
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
		Station other = (Station) obj;
		if (stationId != other.stationId)
			return false;
		return true;
	}
	
}
