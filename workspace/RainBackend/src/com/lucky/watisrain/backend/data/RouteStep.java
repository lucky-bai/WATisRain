package com.lucky.watisrain.backend.data;

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
	
	public Location getStart(){
		return start;
	}
	
	public Location getEnd(){
		return end;
	}
	
	public double getCost(){
		return path.getCost();
	}
	
	public String toString(){
		return start + "->" + end;
	}
}
