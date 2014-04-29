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
	
	private List<Waypoint> waypoints;
	
	// Start and destination in some order
	private Location pointA;
	private Location pointB;
	
	private boolean indoors;
	
	private int pathType;
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_STAIR = 2;
	
	/**
	 * Construct a path object from a list of Waypoints. This list should contain
	 * the points A and B as its first and last elements.
	 */
	public Path(Location pointA, Location pointB){
		
		waypoints = new ArrayList<>();
		waypoints.add(pointA.getPostion());
		waypoints.add(pointB.getPostion());
		
		this.pointA = pointA;
		this.pointB = pointB;
		
		indoors = false;
		pathType = Path.TYPE_NORMAL;
	}
	
	
	/**
	 * Set list of waypoints. It is required that the first point is pointA, and the
	 * last point is pointB.
	 */
	public void setWaypoints(List<Waypoint> waypoints){
		if(!waypoints.get(0).equals(pointA.getPostion()))
			throw new RuntimeException();
		if(!waypoints.get(waypoints.size()-1).equals(pointB.getPostion()))
			throw new RuntimeException();
		this.waypoints = waypoints;
	}
	
	
	public List<Waypoint> getWaypoints(){
		return waypoints;
	}
	
	
	public Location getPointA(){
		return pointA;
	}
	
	public Location getPointB(){
		return pointB;
	}
	
	public void setIndoors(boolean indoors){
		this.indoors = indoors;
	}
	
	public boolean isIndoors(){
		return indoors;
	}
	
	
	public void setPathType(int type){
		this.pathType = type;
	}
	
	public int getPathType(){
		return pathType;
	}
	
	
	/**
	 * Cost of going on taking this path
	 * Give a large penalty if it involves going outside
	 */
	public double getCost(){
		
		// Stairs cost a constant amount
		if(pathType == Path.TYPE_STAIR){
			return 60;
		}
		
		// Sum up waypoints
		double distance = 0;
		for(int i=0; i<waypoints.size()-1; i++){
			// between i and i+1
			distance += waypoints.get(i).distanceTo(waypoints.get(i+1));
		}
		
		if(!indoors) return 3 * distance;
		else return distance;
	}
	
	
	public String toString(){
		return "Path: A=" + pointA + "; B=" + pointB + "; Cost=" + (int)getCost();
	}

}
