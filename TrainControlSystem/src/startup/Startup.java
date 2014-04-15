package startup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


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
					
				}else if(line.startsWith(Suffix.ROUTE.getValue())){
					if(stations.size()<=0){
						System.out.println("Canont input a route before stations are input");
					}
					String[] tokens = line.split(NAME_DELIMITOR);
					String routeName = tokens[0];
					String routeNum = routeName.replace(Suffix.ROUTE.getValue(), "");
					routes.add(Integer.parseInt(routeNum));
					String routeParam = tokens[1];
					String[] routeParamToken = routeParam.split(PARAM_DELIMITOR);
				}else if(line.startsWith(Suffix.TRAIN.getValue())){
					
				}else{
					System.out.println("Unrecognized input:"+line);
					System.exit(1);
				}
			}
		}
		
	}
}
