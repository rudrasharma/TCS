package common;

public class TCSException extends Exception{

	public TCSException(String string) {
		super(string);
	}
	
	public TCSException(String key, String value){
		super("The " + key + " : " + value + " is not a member of this system");
	}
	public TCSException(String key, Integer value){
		this("The " + key + " : " + value.toString() + " is not a member of this system");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
