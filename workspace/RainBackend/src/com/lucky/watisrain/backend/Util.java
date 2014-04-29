package com.lucky.watisrain.backend;

/**
 * Some useful methods
 */
public class Util {
	
	/**
	 * Generates unique ID given building name and floor number like MC:3 
	 */
	public static String makeBuildingAndFloor(String location, int floor){
		return location + ":" + floor;
	}
	
	public static String getBuilding(String combinedID){
		int ix = combinedID.indexOf(':');
		if(ix == -1) return combinedID;
		return combinedID.substring(0, ix);
	}
	
	public static int getFloor(String combinedID){
		int ix = combinedID.indexOf(':');
		if(ix == -1) return 1;
		return Integer.parseInt(combinedID.substring(ix+1));
	}

}
