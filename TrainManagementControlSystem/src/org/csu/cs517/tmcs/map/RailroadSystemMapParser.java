package org.csu.cs517.tmcs.map;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class RailroadSystemMapParser {

  private static String DEFAULT_FILE = "railroadSystemMapDefinition.txt";
  private static Logger logger = Logger.getLogger(RailroadSystemMapParser.class);
 
  // Map<railroad system name, RailroadSysteMap>
  private Map<String, RailroadSystemMap> railRoadSystemMap = new HashMap<>();
  
  private ParseState parseState = ParseState.SYSTEM;
  private StringBuilder tempParseString = new StringBuilder();
  private RouteParameters tempRouteParameters = new RouteParameters();
  private RailroadSystemMap currentRailroadSystemMap;

  public RailroadSystemMapParser(String file) throws IOException {
	  try {
		parseMapFile(file);
	} catch (IOException e) {
		logger.error("IOException in Railroad system parser");
		throw e;
	}
  }
  public RailroadSystemMapParser() throws IOException{
	  this(null);
  }
  

  public void parseMapFile(String file) throws IOException {
	  InputStream inputStream = null;
	if(file==null){
		inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_FILE);
	}else{
		inputStream = new FileInputStream(file);
	}
    BufferedReader in = new BufferedReader(
    		new InputStreamReader(inputStream));
    String line = in.readLine();
    while (line != null) {
      this.processLine(line);
      line = in.readLine();
    }
    in.close();
  }
  
  /**
   * Only supports return one system for now.
   * @return A {@link RailroadSystemMap} instance.
   */
  public RailroadSystemMap getMap() {
    RailroadSystemMap railroadSystemMap = null;
    for (Entry<String, RailroadSystemMap> entry : 
          this.railRoadSystemMap.entrySet()) {
      railroadSystemMap = entry.getValue();
    }
    return railroadSystemMap;
  }

  private void processLine(String line) {
    char[] charArray = line.toCharArray();

    boolean isCommentLine = false;
    for (char c : charArray) {
      if (isCommentLine) {
        break;
      }
      switch (c) {
        case '#':
        case '/':
          isCommentLine = true;
          break;
        case ' ':
        case '\t':
          // Ignore whitespace
          break;
        case '[':
        case '{':
          if (this.parseState == ParseState.SYSTEM) {
            // Done parsing system name, start parsing route.
            String systemName = this.tempParseString.toString();
            this.currentRailroadSystemMap = new RailroadSystemMap(systemName);
          }
          this.parseState = ParseState.ROUTE;
          this.tempParseString.setLength(0);
          break;
        case ':':
          if (this.parseState == ParseState.END_STATION) {
            // Done parsing end station, now parse segments length
            this.parseState = ParseState.SEGMENTS;
            this.tempRouteParameters.endStationId = this.tempParseString.toString();
          } else {
            // Done parsing route, now parse start station
            this.parseState = ParseState.START_STATION;
            this.tempRouteParameters.routeId = this.tempParseString.toString();
          }
          this.tempParseString.setLength(0);
          break;
        case ';':
          // Done parsing start station, now parse end station
          this.parseState = ParseState.END_STATION;
          this.tempRouteParameters.startStationId = this.tempParseString.toString();
          this.tempParseString.setLength(0);
          break;
        case '}':
          // Done parsing segments length and entire route, now parse next route
          this.parseState = ParseState.ROUTE;
          this.tempRouteParameters.numSegments = this.tempParseString.toString();
          this.tempParseString.setLength(0);
          // Save the route
          this.currentRailroadSystemMap.addRoute(
                this.tempRouteParameters.routeId, 
                this.tempRouteParameters.startStationId, 
                this.tempRouteParameters.endStationId, 
                this.tempRouteParameters.numSegments);
          this.tempRouteParameters = new RouteParameters();
          break;
        case ']':
          // Done parsing a system map, now set to parse next system
          this.parseState = ParseState.SYSTEM;
          this.railRoadSystemMap.put(
              this.currentRailroadSystemMap.getSystemName(), 
              this.currentRailroadSystemMap);
          break;
        default:
          this.tempParseString.append(c);
      }
    }
  }
  
  private class RouteParameters {
    public String routeId;
    public String startStationId;
    public String endStationId;
    public String numSegments;
  }
  
  private enum ParseState {
    SYSTEM,
    ROUTE,
    START_STATION,
    END_STATION,
    SEGMENTS
  }
}
