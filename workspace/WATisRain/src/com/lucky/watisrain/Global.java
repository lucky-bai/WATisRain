package com.lucky.watisrain;

import android.util.Log;

public class Global {
	
	/*
	 * There are two maps in the system: map_full and map_downsized. To convert from
	 * map_full to map_downsized:
	 *   (x,y) -> ((x-611)*0.8156, (y-773)*0.8156)
	 * 
	 * To convert from map_downsized to map_full:
	 *   (x,y) -> ((x/0.8156)+611, (y/0.8156)+773)
	 */
	public static final float MAP_WIDTH = 2048.0f;
	public static final float MAP_HEIGHT = 964.0f;
	public static final float MAP_ADJUST_X = 611f;
	public static final float MAP_ADJUST_Y = 773f;
	public static final float MAP_ADJUST_SCALING = 0.8156f;

	public static void println(Object s){
		Log.d("DEBUG_MSG", s.toString());
	}
	
}
