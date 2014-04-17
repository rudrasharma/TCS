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
		if(!contains(routeId)){
			throw new TCSException("route",routeId);
		}
		return routes.get(routeId);

	}
	public boolean contains(Integer routeId){
		return routes.containsKey(routeId);
	}
	

}
