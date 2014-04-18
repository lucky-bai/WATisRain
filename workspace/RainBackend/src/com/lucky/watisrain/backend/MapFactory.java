package com.lucky.watisrain.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.lucky.watisrain.backend.data.Building;
import com.lucky.watisrain.backend.data.Location;
import com.lucky.watisrain.backend.data.Map;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Waypoint;

/**
 * MapFactory generates a Map object given external input data.
 */
public class MapFactory {

	/**
	 * Create a Map object from a specified File
	 */
	public static Map readMapFromFile(File infile)
			throws FileNotFoundException{
		
		Scanner scanner = new Scanner(infile);
		Map map = new Map();
		
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			
			// Empty line, comment
			line = line.trim();
			if(line.isEmpty() || line.charAt(0) == '#')
				continue;
			
			Scanner lineSc = new Scanner(line);
			String command = lineSc.next();
			
			if(command.equals("location")){
				
				// location is actually a Building
				
				String name = lineSc.next();
				int pos_x = lineSc.nextInt();
				int pos_y = lineSc.nextInt();
				
				Building building = new Building(name);
				building.addFloor(new Location(new Waypoint(pos_x, pos_y), name));
				
				map.addBuilding(building);
			}
			
			else if(command.equals("passive_location")){
				
				// Passive locations
				
				String name = lineSc.next();
				int pos_x = lineSc.nextInt();
				int pos_y = lineSc.nextInt();
				map.addPassiveLocation(new Location(name,pos_x,pos_y));
			}
			
			else if(command.equals("path")){
				
				// Paths
				
				String name1 = lineSc.next();
				String name2 = lineSc.next();
				
				Location loc1 = map.getLocationByID(name1);
				Location loc2 = map.getLocationByID(name2);
				
				Path path = new Path(loc1,loc2);
				map.addPath(path);
				
			}
			lineSc.close();
		}
		
		scanner.close();
		
		return map;
	}
	
	
}
