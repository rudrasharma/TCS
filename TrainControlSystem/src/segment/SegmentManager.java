package segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.TCSException;

public class SegmentManager {
	private Map<Integer, SegmentController> segmentControls;
	private static SegmentManager instance;
	
	public static SegmentManager getInstance(){
		if(instance == null){
			instance = new SegmentManager();
		}
		return instance;
	}
	public SegmentManager(){
		segmentControls = new HashMap<>();
	}

	public void addSegment(Segment segment) throws TCSException {
		if (segmentControls.containsKey(segment)) {
			throw new TCSException(	"This segment is already added to the system");
		}
		SegmentController controller = new SegmentController(segment);
		segmentControls.put(segment.getSegmentId(), controller);

	}
	public void addOutgoing(Integer segId, List<Integer> outgoing) throws TCSException{
		validateExisting(segId);
		validateExisting(outgoing);
		List<Segment> outGoing = new ArrayList<>();
		for(Integer outgoingId: outgoing){
			outGoing.add(segmentControls.get(outgoingId).getSegment());
		}
		segmentControls.get(segId).setOutGoing(outGoing);
	}
	private void validateExisting(List<Integer> segIds) throws TCSException{
		for(Integer segId: segIds){
			validateExisting(segId);
		}
	}
	private void validateExisting(Integer segId) throws TCSException{
		if (!segmentControls.containsKey(segId)) {
			throw new TCSException("segment id", segId);
		}
		
	}
	public List<Segment> getOutgoing(Integer segId) throws TCSException{
		return getController(segId).getOutGoing();		
	}
	public SegmentController getController(Integer segId) throws TCSException{
		validateExisting(segId);
		return segmentControls.get(segId);
		
	}
	public boolean traverse(Integer sourceSegId, Integer destSegId) throws TCSException{
		validateExisting(sourceSegId);
		validateExisting(destSegId);
		segmentControls.get(sourceSegId).setNext(segmentControls.get(destSegId).getSegment());
		return segmentControls.get(sourceSegId).traverse();
	}
	public boolean validate(){
		//TODO Implement function
		return false;
	}
}
