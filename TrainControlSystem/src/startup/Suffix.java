package startup;

public enum Suffix {
	COMMENT("#", "Comment"),
	STATION("ST", "Station"),
	ROUTE("RT", "Route"),
	TRAIN("TR", "Train");
	private final String value;
	private final String description;
	Suffix(String value, String description){
		this.value = value;
		this.description = description;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
