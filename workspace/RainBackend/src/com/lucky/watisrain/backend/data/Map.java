package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.List;

import com.lucky.watisrain.backend.Util;

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
	
	/*
	 * Add all floors of a building, and all its stairs
	 */
	public void addBuilding(Building building){
		buildings.add(building);
		locations.addAll(building.getAllFloors());
		paths.addAll(building.getAllStairs());
	}
	
	public void addPath(Path path){
		paths.add(path);
	}
	
	public void addPassiveLocation(Location loc){
		locations.add(loc);
	}
	
	/**
	 * Return all of the paths in the graph
	 */
	public List<Path> getPaths(){
		return paths;
	}
	
	/**
	 * Fetch the path object between two Locations, if exists.
	 * Otherwise, return null
	 */
	public Path retrievePath(Location a, Location b){
		for(Path path : paths){
			if(path.getPointA().equals(a) && path.getPointB().equals(b))
				return path;
			if(path.getPointA().equals(b) && path.getPointB().equals(a))
				return path;
		}
		return null;
	}
	
	public List<Location> getLocations(){
		return locations;
	}
	
	public List<Building> getBuildings(){
		return buildings;
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
	 * Try to fetch some form of location from a string by trying a variety
	 * of methods
	 */
	public Location getLocationByID(String name){
		
		// Attempt to parse as building
		Building building = getBuildingByID(name);
		if(building != null)
			return building.getMainFloor();
		
		// Attempt to parse as-is (usually combined ID)
		for(Location loc : locations){
			if(loc.getName().equals(name))
				return loc;
		}
		
		// Final attempt: parse only the building half
		String partBuilding = Util.getBuilding(name);
		for(Location loc : locations){
			if(loc.getName().equals(partBuilding))
				return loc;
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
