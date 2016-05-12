package com.lucky.watisrain.backend.data;

import com.lucky.watisrain.backend.Util;

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
	
	// A passive location can only be gone through, you cannot start or stop
	// here. For example, the space between SLC and MC.
	boolean active;
	
	/**
	 * Create a Location given a Waypoint and name
	 */
	public Location(String name, Waypoint position, boolean active){
		this.position = position;
		this.name = name;
		this.active = active;
	}
	
	/**
	 * Create a Location given name and coordinates
	 */
	public Location(String name, int x, int y, boolean active){
		this(name,new Waypoint(x,y),active);
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the building part of location, for example, "DC:2" gives "DC"
	 */
	public String getBuildingName(){
		return Util.getBuilding(name);
	}
	
	/**
	 * Returns the floor part of location, for example, "DC:2" gives 2
	 */
	public int getFloorNumber(){
		return Util.getFloor(name);
	}
	
	public Waypoint getPostion(){
		return position;
	}
	
	public boolean isPassive(){
		return !active;
	}
	
	public String toString(){
		return name + position.toString();
	}
	
	@Override
	public boolean equals(Object other){
		// Assume names are unique
		Location other_ = (Location) other;
		return name.equals(other_.getName());
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}

}
