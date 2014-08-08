package com.lucky.watisrain.map;

import java.util.List;

import com.lucky.watisrain.R;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Route;
import com.lucky.watisrain.backend.data.RouteStep;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * The widget that displays the directions in a list
 */
public class DirectionsView extends LinearLayout implements OnClickListener {
	
	// Directions backed by an internal TextView
	TextView textview;
	
	
	// HTML strings for directions.
	// Collapsed directions only show start and end location.
	CharSequence directions_long = "";
	CharSequence directions_collapsed = "";
	
	
	// What is the textview showing?
	static final int STATE_NONE = 1;		// not showing a route
	static final int STATE_COLLAPSED = 2;	// collapsed form
	static final int STATE_LONG = 3;		// long form
	int current_state = STATE_NONE;
	

	public DirectionsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		textview = new TextView(context);
		textview.setText(R.string.select_start_instruction);
		addView(textview);
		setOnClickListener(this);
	}

	public void selectDestination(String destinationName) {
		String selectionHtml = "Selected: <b>" + destinationName + "</b>";
		textview.setText(Html.fromHtml(selectionHtml +
				"<br>" + getResources().getString(R.string.select_destination_instruction)));
		current_state = STATE_NONE;
	}

	public void unselectDestination() {
		textview.setText(getResources().getString(R.string.select_start_instruction));
		current_state = STATE_NONE;
	}
	
	
	@Override
	public void onClick(View v) {
		
		// Flip between long and collapsed directions
		if(current_state == STATE_COLLAPSED){
			textview.setText(directions_long);
			current_state = STATE_LONG;
		}
		else if(current_state == STATE_LONG){
			textview.setText(directions_collapsed);
			current_state = STATE_COLLAPSED;
		}
		
	}
	
	
	/**
	 * Given a calculated route, generate human readable directions and set
	 * the internal TextView to display it
	 */
	public void generateDirectionsFromRoute(Route route){
		
		StringBuilder sb = new StringBuilder();
		
		String overall_building1 = route.getStart().getBuildingName();
		int overall_floor1 = route.getStart().getFloorNumber();
		String overall_building2 = route.getEnd().getBuildingName();
		int overall_floor2 = route.getEnd().getFloorNumber();
		sb.append("Route found: <b>");
		sb.append(overall_building1);
		sb.append("</b> to <b>");
		sb.append(overall_building2);
		sb.append("</b>");
		
		
		// Cutoff point for collapsed directions
		directions_collapsed = Html.fromHtml("[<tt>+</tt>] " + sb.toString());
		
		
		sb.append("<br><br>");
		
		// Start
		sb.append("Start at <b>");
		sb.append(overall_building1);
		sb.append(" (floor ");
		sb.append(overall_floor1);
		sb.append(")</b>");
		sb.append("<br><br>");
		
		List<RouteStep> steps = route.getRouteSteps();
		for(int i = 0; i < steps.size(); i++){
			
			RouteStep step = steps.get(i);
			
			int floor1 = step.getStart().getFloorNumber();
			String build2 = step.getEnd().getBuildingName();
			String build2_formatted = "<b>" + build2 + "</b>";
			int floor2 = step.getEnd().getFloorNumber();
			
			String instr;
			if(step.getPathType() == Path.TYPE_OUTSIDE){
				instr = "Take a walk outside to " + build2_formatted;
			}
			
			else if(step.getPathType() == Path.TYPE_INDOOR_TUNNEL){
				instr = "Take the indoor tunnel to " + build2_formatted;
			}
			
			else if(step.getPathType() == Path.TYPE_UNDERGROUND_TUNNEL){
				instr = "Take the underground tunnel to " + build2_formatted;
			}
			
			else if(step.getPathType() == Path.TYPE_BRIEFLY_OUTSIDE){
				instr = "Step briefly outside to " + build2_formatted;
			}
			
			else if(step.getPathType() == Path.TYPE_INSIDE){
				instr = "Go straight through to " + build2_formatted;
			}
			
			else if(step.getPathType() == Path.TYPE_STAIR){
				
				// Customize the grammar for stairs
				String up_or_down;
				int difference;
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
						+ " to " + build2_formatted + " <b>(floor " + floor2 + ")</b>";
			}else{
				throw new RuntimeException("Bad path type");
			}
			
			sb.append("  ");
			sb.append(i+1);
			sb.append(". ");
			sb.append(instr);
			sb.append("<br>");
		}
		
		sb.append("<br>");
		sb.append("Arrive at <b>");
		sb.append(overall_building2);
		sb.append(" (floor ");
		sb.append(overall_floor2);
		sb.append(")</b>");
		
		// Long directions
		directions_long = Html.fromHtml("[<tt>-</tt>] " + sb.toString());
		textview.setText(directions_long);
		current_state = STATE_LONG;
	}
	
}
