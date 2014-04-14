package com.lucky.watisrain.backend.data;

/**
 * A Location is a vertex on the graph. Locations are suitable start and end
 * points for a route. Typically a Location is a building on campus.
 */
public class Location {
	
	private String longName;
	private String shortName;
	
	private Waypoint position;
	
	/**
	 * Create a Location given a Waypoint and names
	 */
	public Location(Waypoint position, String longName, String shortName){
		this.position = position;
		this.longName = longName;
		this.shortName = shortName;
	}

}
