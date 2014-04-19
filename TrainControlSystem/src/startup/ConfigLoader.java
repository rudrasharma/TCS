package startup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import route.Route;
import route.RouteManager;
import segment.Segment;
import common.TCSException;
import core.Station;
import core.StationManager;
import core.Status;
import core.Train;
import core.TrainManager;


public class ConfigLoader {
	private static final String DEFAULT_STARTUP_FILE = "InitialConfig.txt";
	private static final String NAME_DELIMITOR = ">";
	private static final String PARAM_DELIMITOR = ",";
	private static StationManager sm = StationManager.getInstance();
	private static RouteManager rm = RouteManager.getInstance(); 
	private static TrainManager tm = TrainManager.getInstance();
	public void load() throws IOException {
		load(DEFAULT_STARTUP_FILE);
	}
	public void load(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if(path.toFile().exists()){
			filePath = DEFAULT_STARTUP_FILE;
		}
		List<String> fileLines = Files.readAllLines(Paths.get(filePath), Charset.defaultCharset());
		for(String line: fileLines){
			line = line.trim();
			if(!line.startsWith(Suffix.COMMENT.getValue())){//comments are ignored
				if(line.startsWith(Suffix.STATION.getValue())){
					processStation(line);
				}else if(line.startsWith(Suffix.ROUTE.getValue())){
					processRoute(line);
				}else if(line.startsWith(Suffix.TRAIN.getValue())){
					processTrain(line);
					
				}else{
					System.out.println("Unrecognized input:"+line);
					System.exit(1);
				}
			}
		}
		
	}
	private void processTrain(String line) {
		System.out.println(line);
		String[] tokens = line.split(NAME_DELIMITOR);
		try {
			Integer trainId = extractId(Suffix.TRAIN, tokens[0]);
			String trainParam = tokens[1];
			String[] paramTokens = trainParam.split(PARAM_DELIMITOR);
			Integer routeId = extractId(Suffix.ROUTE, paramTokens[0]);
			if(!rm.contains(routeId)){
				throw new TCSException("Invalid route id :"+routeId+" for train: "+trainId);							
			}
			Integer segmentId = Integer.parseInt(paramTokens[1]);
			if(!rm.get(routeId).containsSegment(segmentId)){
				throw new TCSException("Invalid segment :"+segmentId+" for route:"+routeId);
			}
			Segment onSegment = rm.get(routeId).getSegment(segmentId);
			Train train = new Train(trainId, onSegment);
			tm.add(train);
			System.out.println("Train id: "+trainId+" route id: "+routeId+" segment id: "+segmentId);

			

		} catch (TCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processStation(String line) {
			String stationId = removeSuffix(Suffix.STATION, line);
			Station station = new Station(stationId, Status.CLOSED, null);
			sm.add(station);
			System.out.println(stationId);
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
	private  Integer extractId(Suffix suffix, String token) throws TCSException{
		String idStr = removeSuffix(suffix, token);
		try {
			Integer id = Integer.parseInt(idStr);
			return id;
		}catch(NumberFormatException e){
			throw new TCSException("Error parsing token:"+token+"for :"+suffix.getDescription());
		}
	}
	private String removeSuffix(Suffix suffix, String token) {
		String idStr = token.replace(suffix.getValue(), "");
		return idStr;
	}
	private  void processRoute(String line)  {
		try{	
			String[] tokens = line.split(NAME_DELIMITOR);
			String routeName = tokens[0];
			Integer routeNum = extractId(Suffix.ROUTE, routeName);
			String routeParam = tokens[1];
			String[] paramTokens = routeParam.split(PARAM_DELIMITOR);
			Integer startStationId = extractId(Suffix.STATION, paramTokens[0]);
			if(!sm.contains(startStationId.toString())){
				throw new TCSException("Invalid start station id :"+startStationId+" for route: "+routeNum);
			}
			Integer stopStationId = extractId(Suffix.STATION, paramTokens[1]);
			if(!sm.contains(stopStationId.toString())){
				throw new TCSException("Invalid stop station id: "+stopStationId+" for route: "+routeNum);
			}
			Integer numSegments = Integer.parseInt(paramTokens[2]);
			System.out.println("route number:"+routeNum+"Start station id:"+startStationId+" stopStationId: "+stopStationId+" number of segments: "+numSegments);
			Station startStation = sm.get(startStationId.toString());
			Station endStation = sm.get(stopStationId.toString());
			Route route = new Route(routeNum, Status.OPEN, null, startStation, endStation, numSegments);
			rm.add(route);
		}catch(TCSException e){
			System.out.println(e.getMessage());
			System.out.println("Ignoring folling line from input:"+line);
		}		
	}
}
