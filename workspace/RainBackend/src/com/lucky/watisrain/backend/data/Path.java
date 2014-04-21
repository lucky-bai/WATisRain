package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
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
	
	// Unused
	private List<Waypoint> waypoints;
	
	// Start and destination in some order
	private Location pointA;
	private Location pointB;
	
	/**
	 * Construct a path object from a list of Waypoints. This list should contain
	 * the points A and B as its first and last elements.
	 */
	public Path(Location pointA, Location pointB){
		
		waypoints = new ArrayList<>();
		
		this.pointA = pointA;
		this.pointB = pointB;
	}
	
	
	public Location getPointA(){
		return pointA;
	}
	
	public Location getPointB(){
		return pointB;
	}
	
	
	/**
	 * Cost of going on taking this path
	 * Give a large penalty if it involves going outside
	 */
	public double getCost(){
		
		boolean outside = false;
		if(pointA.isPassive()) outside = true;
		if(pointB.isPassive()) outside = true;
		
		double distance = pointA.getPostion().distanceTo(pointB.getPostion());
		
		if(outside) return 5 * distance;
		else return distance;
	}
	
	
	public String toString(){
		return "Path: A=" + pointA + "; B=" + pointB + "; Cost=" + (int)getCost();
	}

}
