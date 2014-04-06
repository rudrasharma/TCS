package segment;

import java.util.List;

import common.TCSException;
import core.Signal;

public class SegmentController {
	private List<Segment> outGoing;

	private Segment segment;
	private Segment next;

	protected SegmentController(Segment segment){
		this.outGoing = null;
		this.segment = segment;
		this.next = null;
	}
	protected boolean traverse() throws TCSException{
		if(next == null){
			throw new TCSException("The next segment is undefined");
		}
		boolean success = false;
		if(next.getEntrySignal() == Signal.GREEN){
			segment.setHoldingTrain(false);
			next.setHoldingTrain(true);
			success = true;
		}
		return success;
		
	}
	/**
	 * @return the outGoing
	 */
	public List<Segment> getOutGoing() {
		return outGoing;
	}
	/**
	 * @param outGoing the outGoing to set
	 */
	public void setOutGoing(List<Segment> outGoing) {
		this.outGoing = outGoing;
	}

	/**
	 * @return the next
	 */
	public Segment getNext() {
		return next;
	}
	public Segment getSegment(){
		return segment;
	}

	/**
	 * @param next the next to set
	 * @throws TCSException 
	 */
	public void setNext(Segment next) throws TCSException {
		if (!outGoing.contains(next)) {
			throw new TCSException(
					"Error entering segment is not registered as an outgoing segment");
		}

		this.next = next;
	}
}
