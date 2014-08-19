package com.lucky.watisrain.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
	 * Read a Map object given a Scanner pointed to the stream
	 */
	public static Map readMapFromScanner(Scanner scanner){
		
		// removes empty lines and comments
		scanner = preprocess(scanner);
		Map map = new Map();
		
		while(scanner.hasNext()){
			
			String command = scanner.next();
			
			// Delegate handling to sub-functions
			switch(command){
				case "location":
					handleCommandLocation(map, scanner);
					break;
				case "passive_location":
					handleCommandPassiveLocation(map, scanner);
					break;
				case "path":
					handleCommandPath(map, scanner);
					break;
				default:
					throw new RuntimeException("Invalid command: " + command);
			}
		}
		
		scanner.close();
		
		return map;
	}
	
	
	/*
	 * Location (actually a building)
	 */
	private static void handleCommandLocation(Map map, Scanner scanner){
		
		String name = scanner.next();
		int pos_x = scanner.nextInt();
		int pos_y = scanner.nextInt();
		int num_floors = 1;
		int main_floor = 1;
		boolean zero_indexed = false;
		boolean selectable = true;
		
		while(true){
			if(!scanner.hasNext()) break;
			String s = scanner.next();
			if(s.equals(";")) break;
			
			if(s.equals("floors")){
				num_floors = scanner.nextInt();
			}
			
			if(s.equals("main_floor")){
				main_floor = scanner.nextInt();
			}
			
			if(s.equals("has_basement")){
				zero_indexed = true;
			}
			
			if(s.equals("unselectable")){
				selectable = false;
			}
		}
		
		Building building = new Building(name, new Waypoint(pos_x, pos_y), num_floors, main_floor, zero_indexed, selectable);
		
		map.addBuilding(building);
	}
	
	
	/*
	 * Passive location. Simply [name] [x] [y].
	 */
	private static void handleCommandPassiveLocation(Map map, Scanner scanner){
		String name = scanner.next();
		int pos_x = scanner.nextInt();
		int pos_y = scanner.nextInt();
		map.addPassiveLocation(new Location(name,pos_x,pos_y, false));
	}
	
	
	/*
	 * Paths
	 */
	private static void handleCommandPath(Map map, Scanner scanner){
		
		String name1 = scanner.next();
		String name2 = scanner.next();
		
		// Figure out where we are, approximately first. This location will have the
		// correct waypoint, but may have the incorrect floor.
		Location roughly_loc1 = map.getLocationByID(name1);
		Location roughly_loc2 = map.getLocationByID(name2);
		
		// We can have 0 or more "connects" commands. Store them in a list.
		ArrayList<Integer> connect_floors1 = new ArrayList<>();
		ArrayList<Integer> connect_floors2 = new ArrayList<>();
		
		int pathType = Path.TYPE_OUTSIDE;
		
		// Read waypoints
		List<Waypoint> waypoints = new ArrayList<>();
		waypoints.add(roughly_loc1.getPostion());
		
		// Read until semicolon
		while(true){
			if(!scanner.hasNext()) break;
			String s = scanner.next();
			if(s.equals(";")) break;
			
			if(s.equals("p")){
				// add waypoint
				int wx = scanner.nextInt();
				int wy = scanner.nextInt();
				
				waypoints.add(new Waypoint(wx,wy));
			}
			
			if(s.equals("type")){
				String type_str = scanner.next();
				switch(type_str){
					case "inside":
						pathType = Path.TYPE_INSIDE;
						break;
					case "indoor_tunnel":
						pathType = Path.TYPE_INDOOR_TUNNEL;
						break;
					case "underground_tunnel":
						pathType = Path.TYPE_UNDERGROUND_TUNNEL;
						break;
					case "briefly_outside":
						pathType = Path.TYPE_BRIEFLY_OUTSIDE;
						break;
				}
			}
			
			if(s.equals("connects")){
				connect_floors1.add(scanner.nextInt());
				connect_floors2.add(scanner.nextInt());
			}
		}
		
		waypoints.add(roughly_loc2.getPostion());
		
		
		// No "connects" specified, then just link main floor to main floor
		if(connect_floors1.isEmpty()){
			int main_floor1 = 1;
			int main_floor2 = 1;
			Building build1 = map.getBuildingByID(name1);
			Building build2 = map.getBuildingByID(name2);
			if(build1 != null) main_floor1 = build1.getMainFloorNumber();
			if(build2 != null) main_floor2 = build2.getMainFloorNumber();
			connect_floors1.add(main_floor1);
			connect_floors2.add(main_floor2);
		}
		
		
		
		// Add a path for each "connects"
		for(int i=0; i<connect_floors1.size(); i++){
			Location loc1 = map.getLocationByID(Util.makeBuildingAndFloor(name1, connect_floors1.get(i)));
			Location loc2 = map.getLocationByID(Util.makeBuildingAndFloor(name2, connect_floors2.get(i)));

			Path path = new Path(loc1,loc2);
			path.setWaypoints(waypoints);
			path.setPathType(pathType);
			
			map.addPath(path);
		}
	}
	
	
	
	// Preprocess to remove blank lines and comment lines
	private static Scanner preprocess(Scanner prescanner){
		
		StringBuilder sb = new StringBuilder();
		while(prescanner.hasNextLine()){
			String line = prescanner.nextLine();
			if(line.trim().isEmpty() || line.charAt(0) == '#') continue;
			sb.append(line + "\n");
		}
		
		// Actual scanner, don't worry about extraneous stuff anymore
		return new Scanner(sb.toString());
	}
	
	
	

	/**
	 * Create a Map object from a specified File
	 */
	public static Map readMapFromFile(File infile)
			throws FileNotFoundException{
		
		return readMapFromScanner(
				new Scanner(
					new FileInputStream(infile)));
		
	}
	
	
	public static Map readMapFromStream(InputStream stream){
		return readMapFromScanner(new Scanner(stream));
	}
	
	
}
