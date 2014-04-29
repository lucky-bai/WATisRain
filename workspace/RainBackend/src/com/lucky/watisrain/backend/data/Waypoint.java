package com.lucky.watisrain.backend.data;

/**
 * A waypoint is a primitive point without a name.
 */
public class Waypoint {
	
	private int x;
	private int y;
	
	/**
	 * Default constructor
	 */
	public Waypoint(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	
	/**
	 * Calculate Euclidean distance
	 */
	public double distanceTo(Waypoint other){
		double xx = this.x - other.x;
		double yy = this.y - other.y;
		return Math.sqrt(xx*xx + yy*yy);
	}
	
	@Override
	public String toString(){
		return "(" + x + "," + y + ")";
	}
	
	@Override
	public boolean equals(Object o){
		Waypoint other = (Waypoint)o;
		return this.x == other.x && this.y == other.y;
	}

}
