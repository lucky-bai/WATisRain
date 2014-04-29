package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A Building is a wrapper for a list of Location objects, each of which
 * represents a floor of a building.
 */
public class Building {

	private List<Location> floors;
	private String name;
	
	public Building(String name, int num_floors, int main_floor){
		this.name = name;
		floors = new ArrayList<>();
	}
	
	public void addFloor(Location floor){
		floors.add(floor);
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Return the one and only floor (for now)
	 */
	public Location getMainFloor(){
		return floors.get(0);
	}
	
	public String toString(){
		return floors.toString();
	}
	
}
