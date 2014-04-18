package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A Map contains a set of Locations and Paths. It represents the graph data.
 */
public class Map {

	private List<Building> buildings;
	
	// all locations, active and passive
	private List<Location> locations;
	
	private List<Path> paths;
	
	/**
	 * Initialize empty Map object
	 */
	public Map(){
		buildings = new ArrayList<>();
		locations = new ArrayList<>();
		paths = new ArrayList<>();
	}
	
	public void addBuilding(Building building){
		buildings.add(building);
		locations.add(building.getMainFloor());
	}
	
	public void addPath(Path path){
		paths.add(path);
	}
	
	public void addPassiveLocation(Location loc){
		locations.add(loc);
	}
	
	
	/**
	 * Given the unique identifier of a building, return the Building object
	 */
	public Building getBuildingByID(String name){
		for(Building building : buildings){
			if(building.getName().equals(name))
				return building;
		}
		return null;
	}
	
	
	/**
	 * If the name represents a building, get its main floor. Otherwise, if it represents
	 * a passive location, return that.
	 */
	public Location getLocationByID(String name){
		
		Building building = getBuildingByID(name);
		if(building == null){
			
			for(Location loc : locations){
				if(loc.getName().equals(name))
					return loc;
			}
			
		}else{
			return building.getMainFloor();
		}
		
		return null;
	}
	
	
	/**
	 * Debugging purposes
	 */
	public void printDataToStdout(){
		
		System.out.println("Buildings:");
		for(Building b : buildings){
			System.out.println(b);
		}
		System.out.println();
		
		System.out.println("Locations:");
		for(Location l : locations){
			System.out.println(l);
		}
		System.out.println();
		
		System.out.println("Paths:");
		for(Path p : paths){
			System.out.println(p);
		}
		System.out.println();
		
	}
	
}
