package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A RouteStep is like a path, but with a start and end defined.
 */
public class RouteStep {

	private Location start;
	private Location end;
	private Path path;
	
	public RouteStep(Location start, Location end, Path path){
		this.start = start;
		this.end = end;
		this.path = path;
	}
	
	/**
	 * Return a RouteStep from this.start to other.end
	 */
	public RouteStep mergeWith(RouteStep other){
		
		if(!this.end.equals(other.start))
			throw new RuntimeException("Error: routes don't connect!");
		
		ArrayList<Waypoint> waypoints = new ArrayList<>();
		waypoints.addAll(getWaypoints());
		waypoints.remove(waypoints.size()-1);
		waypoints.addAll(other.getWaypoints());
		
		Path newpath = new Path(this.start, other.end);
		newpath.setPathType(this.path.getPathType());
		newpath.setWaypoints(waypoints);
		
		if(this.path.getPathType() != other.path.getPathType())
			throw new RuntimeException("Error: routes not the same type!");
		if(this.path.isIndoors() != other.path.isIndoors())
			throw new RuntimeException("Error: routes not the same type!");
		
		return new RouteStep(this.start, other.end, newpath);
	}
	
	public Location getStart(){
		return start;
	}
	
	public Location getEnd(){
		return end;
	}
	
	public Path getPath(){
		return path;
	}
	
	public int getPathType(){
		return path.getPathType();
	}
	
	
	/**
	 * Returns list of waypoints, reversed if necessary
	 */
	public List<Waypoint> getWaypoints(){
		
		List<Waypoint> ret = new ArrayList<Waypoint>();
		ret.addAll(path.getWaypoints());
		
		if(ret.get(0).equals(this.getStart().getPostion()))
			return ret;
		
		Collections.reverse(ret);
		return ret;
	}
	
	public double getCost(){
		return path.getCost();
	}
	
	public String toString(){
		return start + "->" + end;
	}
}
