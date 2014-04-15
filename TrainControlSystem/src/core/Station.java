package core;

public class Station {
	private String stationId;
	private Status status;
	private Time closeTime;
	/**
	 * @param stationId
	 * @param status
	 * @param closeTime
	 */
	public Station(String stationId, Status status, Time closeTime) {
		this.stationId = stationId;
		this.status = status;
		this.closeTime = closeTime;
	}
	
	public Station(String stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the stationId
	 */
	public String getStationId() {
		return stationId;
	}
	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(String stationId) {
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((closeTime == null) ? 0 : closeTime.hashCode());
		result = prime * result
				+ ((stationId == null) ? 0 : stationId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (closeTime == null)
		{
			if (other.closeTime != null)
				return false;
		}
		else if (!closeTime.equals(other.closeTime))
			return false;
		if (stationId == null)
		{
			if (other.stationId != null)
				return false;
		}
		else if (!stationId.equals(other.stationId))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Station [stationId=" + stationId + ", status=" + status
				+ ", closeTime=" + closeTime + "]";
	}
	
}
