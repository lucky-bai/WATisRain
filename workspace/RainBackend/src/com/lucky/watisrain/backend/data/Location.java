package com.lucky.watisrain.backend.data;

/**
 * A Location is a vertex on the graph. It represents the most basic point that it makes
 * sense to say a user is at.
 * 
 * Locations are suitable start and end points for a route. Typically it is a floor
 * of a building on campus (not the whole building)
 */
public class Location {
	
	private String name;
	private Waypoint position;
	
	/**
	 * Create a Location given a Waypoint and name
	 */
	public Location(Waypoint position, String name){
		this.position = position;
		this.name = name;
	}
	
	/**
	 * Create a Location given name and coordinates
	 */
	public Location(String name, int x, int y){
		this(new Waypoint(x,y),name);
	}
	
	public String getName(){
		return name;
	}
	
	public Waypoint getPostion(){
		return position;
	}
	
	public String toString(){
		return name + position.toString();
	}
	
	public boolean equals(Location other){
		// Assume names are unique
		return name.equals(other.getName());
	}

}
