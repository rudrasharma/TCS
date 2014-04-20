package simulation;

public enum Event
{	
	OPEN_ROUTE("OpenRoute"), 
	CLOSE_ROUTE("CloseRoute"), 
	OPEN_STATION("OpenStation"), 
	CLOSE_STATION("CloseStation"), 
	SUBMIT_JOURNEY("SubmitJourney"), 
	RESTART("Restart"), 
	STOP("Stop"), 
	CLOCK_TICK("end");

	String name;

	Event(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	public Event get(){
		return this;
	}
	
}
