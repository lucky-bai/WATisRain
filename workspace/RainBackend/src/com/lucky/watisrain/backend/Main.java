package com.lucky.watisrain.backend;

import java.io.File;
import java.util.HashMap;

import com.lucky.watisrain.backend.data.*;

public class Main {

	public static void main(String... args) throws Throwable{
		
		File file = new File("C:/Users/Bai/Desktop/dev/watisrain/deprecated/locations.txt");
		Map map = MapFactory.readMapFromFile(file);
		
		//map.printDataToStdout();
		
		RouteFinder routefinder = new RouteFinder(map);
		Route route = routefinder.findRoute(map.getLocationByID("MC"), map.getLocationByID("DWE"));
		
		route.printRouteToStdout();
		
	}
	
}
