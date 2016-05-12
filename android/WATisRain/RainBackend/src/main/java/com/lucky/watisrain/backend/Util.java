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
	
	
	/**
	 * HACK: This is a global variable to make the pathing weight adjustable from
	 * outside the backend module.
	 * 
	 * This number represents the "unwillingness" ratio of going outside. For example,
	 * 3.0 means that travelling 1m outside is equal to travelling 3m outside.
	 */
	public static double GLOBAL_PATHING_WEIGHT = 3.0;
	
	
	/**
	 * Given two vectors a and b, return the unit vector that goes in the opposite direction
	 * of them. For example, if a = (1,0) and b = (0,1), the return (-sqrt 2, -sqrt 2)
	 */
	public static double[] findOppositeVector(double a1, double a2, double b1, double b2){
		
		// Fudge stuff, makes it less likely for the case that they are exactly the
		// opposite direction (which results in a division by zero)
		a1 += 0.0001;
		a2 += 0.0001;
		
		// Normalize a and b
		double length_a = Math.sqrt(a1*a1 + a2*a2);
		double length_b = Math.sqrt(b1*b1 + b2*b2);
		a1 /= length_a;
		a2 /= length_a;
		b1 /= length_b;
		b2 /= length_b;
		
		// Take negative of sum
		double c1 = -a1-b1;
		double c2 = -a2-b2;
		
		// Normalize c
		double length_c = Math.sqrt(c1*c1 + c2*c2);
		c1 /= length_c;
		c2 /= length_c;
		
		return new double[]{c1,c2};
	}

}
