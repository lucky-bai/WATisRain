package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.List;

import com.lucky.watisrain.backend.Util;

/**
 * A path is an undirected edge in the graph.
 * 
 * It contains:
 *  - Two Locations the vertices that the edge connects
 *  - A list of Waypoints: how the path is to be displayed on the screen
 *  
 * It also contains metadata about its type, length, etc.
 */
public class Path {
	
	private List<Waypoint> waypoints;
	
	// Start and destination in some order
	private Location pointA;
	private Location pointB;
	
	private int pathType = TYPE_OUTSIDE;
	public static final int TYPE_OUTSIDE = 1;
	public static final int TYPE_STAIR = 2;
	public static final int TYPE_INSIDE = 3;
	public static final int TYPE_INDOOR_TUNNEL = 4;
	public static final int TYPE_UNDERGROUND_TUNNEL = 5;
	public static final int TYPE_BRIEFLY_OUTSIDE = 6;
	
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
		
		pathType = TYPE_OUTSIDE;
	}
	
	
	/**
	 * Set list of waypoints. It is required that the first point is pointA, and the
	 * last point is pointB.
	 */
	public void setWaypoints(List<Waypoint> waypoints){
		if(!waypoints.get(0).equals(pointA.getPostion()))
			throw new RuntimeException("Bad waypoints!");
		if(!waypoints.get(waypoints.size()-1).equals(pointB.getPostion()))
			throw new RuntimeException("Bad waypoints!");
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
	
	public boolean isIndoors(){
		switch(pathType){
			case TYPE_OUTSIDE:
			case TYPE_BRIEFLY_OUTSIDE:
				return false;
			case TYPE_INSIDE:
			case TYPE_STAIR:
			case TYPE_INDOOR_TUNNEL:
			case TYPE_UNDERGROUND_TUNNEL:
				return true;
			default:
				throw new RuntimeException("Bad path type");
		}
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
		
		// The formula for the cost of taking a stair is 50 + 25*(number of floors)
		if(pathType == Path.TYPE_STAIR){
			int floor1 = pointA.getFloorNumber();
			int floor2 = pointB.getFloorNumber();
			int floor_diff = floor1 - floor2;
			if(floor_diff < 0) floor_diff = -floor_diff;
			return floor_diff * 25 + 50;
		}
		
		// Sum up waypoints
		double distance = 0;
		for(int i=0; i<waypoints.size()-1; i++){
			// between i and i+1
			distance += waypoints.get(i).distanceTo(waypoints.get(i+1));
		}
		
		if(!isIndoors()) return Util.GLOBAL_PATHING_WEIGHT * distance;
		else return distance;
	}
	
	
	public String toString(){
		return "Path: A=" + pointA + "; B=" + pointB + "; Cost=" + (int)getCost();
	}

}
