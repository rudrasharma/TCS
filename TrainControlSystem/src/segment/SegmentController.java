package segment;

import java.util.List;

import common.TCSException;

public class SegmentController {
	private List<Segment> outGoing;
	private Segment segment;
	/**
	 * @param outGoing
	 * @param segment
	 */
	protected SegmentController(Segment segment, List<Segment> outGoing) {
		this.outGoing = outGoing;
		this.segment = segment;
	}
	
	protected boolean traverse(Segment enteringSegment) throws TCSException{
		if (!outGoing.contains(enteringSegment)) {
			throw new TCSException(
					"Error entering segment is not registered as an outgoing segment");
		}
		boolean success = false;
		if(segment.canExit() && enteringSegment.canEnter()){
			segment.setHoldingTrain(false);
			enteringSegment.setHoldingTrain(true);
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


}
