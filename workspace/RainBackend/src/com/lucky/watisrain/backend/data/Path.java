package com.lucky.watisrain.backend.data;

import java.util.List;

/**
 * A path is an undirected edge in the graph.
 * 
 * It contains:
 *  - Two Locations the vertices that the edge connects
 *  - A list of Waypoints: how the path is to be displayed on the screen
 *  
 * It also contains metadata about its type, length, etc. I will determine
 * the details of this later.
 */
public class Path {
	
	private List<Waypoint> waypoints;
	
	// Start and destination in some order
	private Waypoint pointA;
	private Waypoint pointB;
	
	/**
	 * Construct a path object from a list of Waypoints. This list should contain
	 * the points A and B as its first and last elements.
	 */
	public Path(List<Waypoint> waypoints){
		this.waypoints = waypoints;
		
		// Assign pointA and pointB
		if(waypoints == null || waypoints.size() < 2)
			throw new RuntimeException("Invalid waypoint list!");
		
		pointA = waypoints.get(0);
		pointB = waypoints.get(waypoints.size()-1);
	}
	
	
	public Waypoint getPointA(){
		return pointA;
	}
	
	public Waypoint getPointB(){
		return pointB;
	}
	
	
	public String toString(){
		return "Path: A=" + pointA + "; B=" + pointB + "; " + waypoints;
	}

}
