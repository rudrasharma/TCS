package common;

public class TCSException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7207366075014040458L;
	public TCSException(String string) {
		super(string);
	}
	
	public TCSException(String key, String value){
		super("The " + key + " : " + value + " is not a member of this system");
	}
	public TCSException(String key, Integer value){
		this(key, value.toString());
	}

}
