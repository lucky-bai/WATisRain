package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.List;

import com.lucky.watisrain.backend.Util;

/**
 * A Building is a wrapper for a list of Location objects, each of which
 * represents a floor of a building.
 */
public class Building {

	// Floors of this building
	private List<Location> floors;
	
	// Stairs to get around within this building
	private List<Path> stairs;
	
	// Short name, like "DC"
	private String name;
	
	// Location of this building
	private Waypoint position;
	
	// How many floors does this building have?
	private int num_floors;
	
	// Which of the floors is the main floor? By default, 1
	private int main_floor;
	
	// Show an icon on the map to select it?
	private boolean selectable;
	
	
	/**
	 * Constructor. Assumes all floors have this same position, main_floor <= num_floors
	 * 
	 * @param name name of the building
	 * @param position position of the building (assume all floors have same position)
	 * @param num_floors number of floors, including basement if there is one
	 * @param main_floor the floor which you are in when you walk into the building
	 * @param zero_indexed start counting floors from 0 instead of 1
	 * @param selectable show selection icon on the map for building
	 */
	public Building(String name, Waypoint position, int num_floors, int main_floor,
					boolean zero_indexed, boolean selectable){
		
		this.name = name;
		this.position = position;
		this.main_floor = main_floor;
		this.num_floors = num_floors;
		this.selectable = selectable;
		
		// Populate list of floors
		floors = new ArrayList<>();
		for(int i=1; i<=num_floors; i++){
			int floor_num = i;
			if(zero_indexed) floor_num--;
			String floor_id = Util.makeBuildingAndFloor(name, floor_num);
			Location this_floor = new Location(floor_id,position,true);
			floors.add(this_floor);
		}
		
		// Populate list of stairs
		stairs = new ArrayList<>();
		for(int i=1; i<num_floors; i++){
			Location lower_floor = floors.get(i-1);
			Location upper_floor = floors.get(i);
			Path this_stair = new Path(lower_floor,upper_floor);
			this_stair.setPathType(Path.TYPE_STAIR);
			stairs.add(this_stair);
		}
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Return the main floor
	 */
	public Location getMainFloor(){
		// do linear search, because the zero-indexing option introduces possible edge case
		for(Location floor : floors){
			if(floor.getFloorNumber() == main_floor)
				return floor;
		}
		return null;
	}
	
	/**
	 * Numerical value of main floor
	 */
	public int getMainFloorNumber(){
		return main_floor;
	}
	
	public List<Location> getAllFloors(){
		return floors;
	}
	
	public List<Path> getAllStairs(){
		return stairs;
	}
	
	public Waypoint getPosition(){
		return position;
	}
	
	public int getNumberOfFloors(){
		return num_floors;
	}
	
	public boolean isSelectable(){
		return selectable;
	}
	
	public String toString(){
		return floors.toString();
	}
	
}
