package com.lucky.watisrain.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.lucky.watisrain.backend.data.Building;
import com.lucky.watisrain.backend.data.Location;
import com.lucky.watisrain.backend.data.Map;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Route;
import com.lucky.watisrain.backend.data.RouteStep;

/**
 * RouteFinder finds a Route given a Map. Details TBD.
 */
public class RouteFinder {
	
	Map map;
	
	/**
	 * Initialize with a map object
	 */
	public RouteFinder(Map map){
		this.map = map;
	}
	
	
	/**
	 * Find shortest Route object between two locations
	 */
	public Route findRoute(Location loc1, Location loc2){
		
		List<Location> routelist = dijkstra(loc1, loc2);
		
		Route route = new Route();
		Location cur = loc1;
		
		for(Location loc : routelist){
			if(cur.equals(loc)) continue;
			route.addStep(new RouteStep(cur, loc, map.retrievePath(cur, loc)));
			cur = loc;
		}
		
		return route;
	}
	
	
	/**
	 * Find the shortest route between main floor of build1 and ANY floor of build2
	 */
	public Route findRoute(Building build1, Building build2){
		
		double min_cost = Double.MAX_VALUE;
		Route min_route = null;
		Location start_location = build1.getMainFloor();
		
		for(Location end_location : build2.getAllFloors()){
			Route cur_route = findRoute(start_location, end_location);
			double cur_cost = cur_route.getTotalCost();
			
			if(cur_cost < min_cost){
				min_cost = cur_cost;
				min_route = cur_route;
			}
		}
		
		return min_route;
	}
	
	
	/**
	 * Return the shortest path between two locations, in the form of a List of Locations
	 */
	private List<Location> dijkstra(Location loc1, Location loc2){
		
		// Dijkstra's algorithm
		
		ArrayList<Location> queue = new ArrayList<>();
		ArrayList<Location> visited = new ArrayList<>();
		
		HashMap<Location, Double> distTable = new HashMap<>();
		for(Location loc : map.getLocations()){
			distTable.put(loc, Double.MAX_VALUE);
		}
		distTable.put(loc1, 0.0);
		
		queue.add(loc1);
		
		while(!queue.isEmpty()){
			
			// Extract min
			double min_cost = Double.MAX_VALUE;
			int min_index = 0;
			for(int i=0; i<queue.size(); i++){
				if(distTable.get(queue.get(i)) < min_cost){
					min_cost = distTable.get(queue.get(i));
					min_index = i;
				}
			}
			Location cur = queue.remove(min_index);
			
			List<LocationAndDistance> nextlist = getAdjacentLocations(cur);
			for(LocationAndDistance next : nextlist){
				
				if(!visited.contains(next.location)){
					double alt_dist = distTable.get(cur) + next.distance;
					double existing_dist = distTable.get(next.location);
					if(alt_dist < existing_dist){
						distTable.put(next.location, alt_dist);
						queue.add(next.location);
					}
				}
				
			}
			
			visited.add(cur);
		}
		
		
		ArrayList<Location> calcPath = new ArrayList<>();
		calcPath.add(loc2);
		
		// Construct the path backwards from destination
		Location backtrack = loc2;
		while(!backtrack.equals(loc1)){
			
			List<LocationAndDistance> prevlist = getAdjacentLocations(backtrack);
			for(LocationAndDistance prev : prevlist){
				if(Math.abs(prev.distance + distTable.get(prev.location)
						- distTable.get(backtrack)) < 0.0001){
					calcPath.add(prev.location);
					backtrack = prev.location;
				}
			}
			
		}
		
		Collections.reverse(calcPath);
		
		return calcPath;
	}
	
	
	/**
	 * List of Locations that are connected by a single path to a
	 * given Location
	 */
	private List<LocationAndDistance> getAdjacentLocations(Location loc){
		
		List<LocationAndDistance> adjacents = new ArrayList<>();
		
		for(Path path : map.getPaths()){
			if(path.getPointA().equals(loc)){
				double dist = path.getCost();
				adjacents.add(new LocationAndDistance(path.getPointB(), dist));
			}
			if(path.getPointB().equals(loc)){
				double dist = path.getCost();
				adjacents.add(new LocationAndDistance(path.getPointA(), dist));
			}
		}
		
		return adjacents;
	}
	
	
	/**
	 * Wrapper containing a location and distance
	 */
	class LocationAndDistance{
		
		Location location;
		double distance;
		
		public LocationAndDistance(Location loc, double dist){
			location = loc;
			distance = dist;
		}
		
		public String toString(){
			return distance + ":" + location;
		}
		
	}

}
