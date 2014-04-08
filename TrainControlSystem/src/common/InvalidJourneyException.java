package common;

public class InvalidJourneyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3261887649605319437L;
	public InvalidJourneyException(String string) {
		super(string);
	}
	public InvalidJourneyException(String key, String value){
		this("Invalid Journey: "+key+": "+value+" is closed");
	}
	public InvalidJourneyException(String key, Integer value){
		this(key, value.toString());
	}

}
