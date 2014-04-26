package segment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.TCSException;

public class SegmentManager {
	private Map<Segment, SegmentController> segmentControls;
	
	public SegmentManager(){
		segmentControls = new HashMap<>();
	}

	public void addSegment(Segment segment, List<Segment> outGoing)
			throws TCSException {
		if (segmentControls.containsKey(segment)) {
			throw new TCSException(	"This segment is already added to the system");
		}
		SegmentController controller = new SegmentController(segment, outGoing);
		segmentControls.put(segment, controller);

	}
	public List<Segment> getOutgoing(Segment segment) throws TCSException{
		return getController(segment).getOutGoing();		
	}
	public SegmentController getController(Segment segment) throws TCSException{
		if (!segmentControls.containsKey(segment)) {
			throw new TCSException(	"This segment not a part fo this system");
		}
		return segmentControls.get(segment);
		
	}

	public boolean validate(){
		//TODO Implement function
		return false;
	}
}
