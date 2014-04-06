package route;

import java.util.HashMap;
import java.util.Map;

import common.TCSException;

public class RouteManager {
	Map<Integer, Route> routes;
	
	public RouteManager(){
		routes = new HashMap<>();
	}
	public void addRoute(Route route){
		routes.put(route.getRouteId(), route);
	}
	public Route getRoute(Integer routeId) throws TCSException{
		validateExisting(routeId);
		return routes.get(routeId);
	}
	public Integer getNextSegmentId(Integer routeId, Integer currentSegment) throws TCSException{
		validateExisting(routeId);
		return routes.get(routeId).getNextSegementId(routeId, currentSegment);
	
	}
	private void validateExisting(Integer routeId) throws TCSException{
		if(!routes.containsKey(routeId)){
			throw new TCSException("route Id", routeId);
		}
		
	}

}
