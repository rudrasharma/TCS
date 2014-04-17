package startup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import route.Route;
import common.TCSException;


public class Startup {
	private static final String DEFAULT_STARTUP_FILE = "Startup.txt";
	private static final String NAME_DELIMITOR = ">";
	private static final String PARAM_DELIMITOR = ",";
	public static void main(String[] args) throws IOException {
		String location = DEFAULT_STARTUP_FILE;
		if(args != null && args.length > 0){
			location = args[0];
		}
		List<String> fileLines = Files.readAllLines(Paths.get(location), Charset.defaultCharset());
		List<Integer> stations = new ArrayList<>();
		List<Integer> routes = new ArrayList<>();
		for(String line: fileLines){
			line = line.trim();
			if(!line.startsWith(Suffix.COMMENT.getValue())){
				if(line.startsWith(Suffix.STATION.getValue())){
					try{
						Integer stationId = extractId(Suffix.STATION, line);
						stations.add(stationId);
						System.out.println(stationId);
					}catch(TCSException e){
						System.out.println(e.getMessage());
						System.out.println("Ignoring folling line from input:"+line);
					}
				}else if(line.startsWith(Suffix.ROUTE.getValue())){
				try{	
					processRoute(stations, routes, line);
				}catch(TCSException e){
					System.out.println(e.getMessage());
					System.out.println("Ignoring folling line from input:"+line);
				}

				}else if(line.startsWith(Suffix.TRAIN.getValue())){
					System.out.println(line);
					String[] tokens = line.split(NAME_DELIMITOR);
					try {
						Integer trainId = extractId(Suffix.TRAIN, tokens[0]);
						String trainParam = tokens[1];
						String[] paramTokens = trainParam.split(PARAM_DELIMITOR);
						Integer routeId = extractId(Suffix.ROUTE, paramTokens[0]);
						if(!routes.contains(routeId)){
							throw new TCSException("Invalid route id :"+routeId+" for train: "+trainId);							
						}
						Integer segmentId = Integer.parseInt(paramTokens[1]);
						System.out.println("Train id: "+trainId+" route id: "+routeId+" segment id: "+segmentId);

						

					} catch (TCSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					System.out.println("Unrecognized input:"+line);
					System.exit(1);
				}
			}
		}
		
	}
	/**
	 * @param stations
	 * @param routes
	 * @param line
	 */
	/**
	 * @param line
	 * @return
	 */
	private static Integer extractId(Suffix suffix, String token) throws TCSException{
		String idStr = token.replace(suffix.getValue(), "");
		try {
			Integer id = Integer.parseInt(idStr);
			return id;
		}catch(NumberFormatException e){
			throw new TCSException("Error parsing token:"+token+"for :"+suffix.getDescription());
		}
	}
	private static void processRoute(List<Integer> stations,
			List<Integer> routes, String line) throws TCSException {
		if(stations.size()<=0){
			System.out.println("Canont input a route before stations are input");
		}
		String[] tokens = line.split(NAME_DELIMITOR);
		String routeName = tokens[0];
		Integer routeNum = extractId(Suffix.ROUTE, routeName);
		routes.add(routeNum);
		String routeParam = tokens[1];
		String[] paramTokens = routeParam.split(PARAM_DELIMITOR);
		Integer startStationId = extractId(Suffix.STATION, paramTokens[0]);
		if(!stations.contains(startStationId)){
			throw new TCSException("Invalid start station id :"+startStationId+" for route: "+routeNum);
		}
		Integer stopStationId = extractId(Suffix.STATION, paramTokens[1]);
		if(!stations.contains(stopStationId)){
			throw new TCSException("Invalid stop station id: "+stopStationId+" for route: "+routeNum);
		}
		Integer numSegments = Integer.parseInt(paramTokens[2]);
		System.out.println("route number:"+routeNum+"Start station id:"+startStationId+" stopStationId: "+stopStationId+" number of segments: "+numSegments);
		Route route = new Route
	}
}
