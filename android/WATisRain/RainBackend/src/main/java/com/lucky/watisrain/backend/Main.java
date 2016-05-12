package com.lucky.watisrain.backend;

import java.io.File;

import com.lucky.watisrain.backend.data.*;

/**
 * This is merely a test harness for RainBackend
 */
public class Main {

	public static void main(String... args) throws Throwable{
		
		File file = new File("C:/Users/Bai/Desktop/dev/watisrain/deprecated/locations.txt");
		Map map = MapFactory.readMapFromFile(file);
		
		//map.printDataToStdout();
		
		RouteFinder routefinder = new RouteFinder(map);
		Route route = routefinder.findRoute(map.getLocationByID("MC:6"), map.getLocationByID("UWP"));
		
		route.printRouteToStdout();
		System.out.println();
		route.getContractedRoute().printRouteToStdout();
		
	}
	
}
