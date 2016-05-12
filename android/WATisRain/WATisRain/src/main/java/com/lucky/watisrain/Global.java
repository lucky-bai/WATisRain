package com.lucky.watisrain;

import com.lucky.watisrain.backend.Util;
import com.lucky.watisrain.map.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
	
	
	
	static String[] pathing_choices = new String[]{"None","Within reason","At any cost"};
	static int pathing_selected = 1;
	
	/**
	 * Show the settings dialog (to select pathing mode)
	 * Automatically recalculate the route when settings change.
	 */
	public static void showSettings(Context context, final MapView mapview){
		
		AlertDialog.Builder db = new AlertDialog.Builder(context);
		db.setTitle("Prefer indoors:");
		db.setSingleChoiceItems(pathing_choices, pathing_selected, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				// no change
				if(which == pathing_selected) return;
				
				pathing_selected = which;
				
				switch(which){
					case 0:
						Util.GLOBAL_PATHING_WEIGHT = 1.0;
						break;
					case 1:
						Util.GLOBAL_PATHING_WEIGHT = 3.0;
						break;
					case 2:
						Util.GLOBAL_PATHING_WEIGHT = 100.0;
						break;
				}
				
				mapview.recalculateRoute();
			}
		});
		
		db.setPositiveButton("OK", null);
		db.create().show();
	}
}
