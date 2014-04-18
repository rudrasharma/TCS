package route;

import java.util.HashMap;
import java.util.Map;

import common.TCSException;

/**
 * 
 */
public class RouteManager {
	private Map<Integer, Route> routes;
	private static RouteManager instance;
	
	public static RouteManager getInstance(){
		if(instance == null){
			instance = new RouteManager();
		}
		return instance;
	}
	private RouteManager(){
		routes = new HashMap<Integer, Route>();
	}
	public void add(Route route){
		routes.put(route.getRouteId(), route);
	}
	public Route get(Integer routeId) throws TCSException{
		if(!contains(routeId)){
			throw new TCSException("route",routeId);
		}
		return routes.get(routeId);

	}
	public boolean contains(Integer routeId){
		return routes.containsKey(routeId);
	}
	

}
