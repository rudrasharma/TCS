package core;

public class Time {
	/**
	 * @param unit
	 */
	public Time(int unit) {
		this.unit = unit;
	}

	private int unit;

	/**
	 * @return the unit
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(int unit) {
		this.unit = unit;
	}

}
