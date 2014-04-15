package route;

import java.util.HashMap;
import java.util.Map;

import segment.SegmentManager;
import common.TCSException;
import core.Status;
//commenting
public class RouteManager {
	private Map<Integer, Route> routes;
	private SegmentManager segmentManager = SegmentManager.getInstance();
	private static RouteManager instance;
	
	public static RouteManager getinstance(){
		if(instance == null){
			instance = new RouteManager();
		}
		return instance;
	}
	public RouteManager(){
		routes = new HashMap<Integer, Route>();
	}
	public void addRoute(Route route){
		routes.put(route.getRouteId(), route);
	}
	public Route getRoute(Integer routeId) throws TCSException{
		validateExisting(routeId);
		return routes.get(routeId);
	}
	public boolean traverse(Integer routeId, Integer currentSegmentId) throws TCSException{
		validateExisting(routeId);
		if (getRoute(routeId).getStatus() != Status.OPEN){
			return false;
		}
		Integer nextSegmentId = getNextSegmentId(routeId, currentSegmentId);
		return segmentManager.traverse(currentSegmentId, nextSegmentId);
	}
	private Integer getNextSegmentId(Integer routeId, Integer currentSegment) throws TCSException{
		validateExisting(routeId);
		return routes.get(routeId).getNextSegementId(routeId, currentSegment);
	
	}
	private void validateExisting(Integer routeId) throws TCSException{
		if(!routes.containsKey(routeId)){
			throw new TCSException("route Id", routeId);
		}
		
	}

}
