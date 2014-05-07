package com.lucky.watisrain;

import java.util.List;

import com.lucky.watisrain.backend.Util;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Route;
import com.lucky.watisrain.backend.data.RouteStep;

import android.util.Log;

public class Global {
	
	public static final float MAP_WIDTH = 3175.0f;
	public static final float MAP_HEIGHT = 2416.0f;

	public static void println(Object s){
		Log.d("DEBUG_MSG", s.toString());
	}
	
	
	/**
	 * Given a calculated route, convert it to human readable directions
	 * @return a string, with each step of route corresponding to one line
	 */
	public static String generateHumanReadableDirections(Route route){
		
		StringBuilder sb = new StringBuilder();
		
		String overall_building1 = Util.getBuilding(route.getStart().getName());
		int overall_floor1 = Util.getFloor(route.getStart().getName());
		String overall_building2 = Util.getBuilding(route.getEnd().getName());
		int overall_floor2 = Util.getFloor(route.getEnd().getName());
		sb.append("Route found: " + overall_building1 + " -> " + overall_building2);
		
		sb.append("\n\n");
		
		// zeroth step is where you start
		sb.append("Start at " + overall_building1 + " (floor " + overall_floor1 + ")");
		sb.append("\n\n");
		
		List<RouteStep> steps = route.getRouteSteps();
		for(int i = 0; i < steps.size(); i++){
			
			RouteStep step = steps.get(i);
			
			//String build1 = Util.getBuilding(step.getStart().getName());
			int floor1 = Util.getFloor(step.getStart().getName());
			String build2 = Util.getBuilding(step.getEnd().getName());
			int floor2 = Util.getFloor(step.getEnd().getName());
			
			String instr = "";
			if(step.getPathType() == Path.TYPE_OUTSIDE){
				instr = "Take a walk outside to " + build2;
			}
			
			else if(step.getPathType() == Path.TYPE_INDOOR_TUNNEL){
				instr = "Take the indoor tunnel to " + build2;
			}
			
			else if(step.getPathType() == Path.TYPE_UNDERGROUND_TUNNEL){
				instr = "Take the underground tunnel to " + build2;
			}
			
			else if(step.getPathType() == Path.TYPE_BRIEFLY_OUTSIDE){
				instr = "Step briefly outside to " + build2;
			}
			
			else if(step.getPathType() == Path.TYPE_INSIDE){
				instr = "Go straight through to " + build2;
			}
			
			else if(step.getPathType() == Path.TYPE_STAIR){
				
				// Customize the grammar for stairs
				String up_or_down = "";
				int difference = 0;
				if(floor2 < floor1){
					up_or_down = "down";
					difference = floor1 - floor2;
				}else{
					up_or_down = "up";
					difference = floor2 - floor1;
				}
				
				String s_if_plural = "";
				if(difference > 1)
					s_if_plural = "s";
				
				instr = "Climb " + up_or_down + " " + difference + " floor" + s_if_plural
						+ " to " + build2 + " (floor " + floor2 + ")";
			}else{
				throw new RuntimeException("Bad path type");
			}
			
			sb.append("  " + (i+1) + ". " + instr);
			sb.append("\n");
		}
		
		sb.append("\n");
		sb.append("Arrive at " + overall_building2 + " (floor " + overall_floor2 + ")");
		
		return sb.toString();
	}
	
}
