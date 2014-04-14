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
	
	public String toString(){
		return "(" + x + "," + y + ")";
	}

}
