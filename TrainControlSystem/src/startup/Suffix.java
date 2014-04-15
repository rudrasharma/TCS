package startup;

public enum Suffix {
	COMMENT("#"),
	STATION("S"),
	ROUTE("RT"),
	TRAIN("TR");
	private final String value;
	Suffix(String value){
		this.value = value;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
