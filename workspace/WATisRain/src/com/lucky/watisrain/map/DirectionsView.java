package com.lucky.watisrain.map;

import java.util.List;

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
	

	public DirectionsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		textview = new TextView(context);
		textview.setText("Touch the map to select a destination");
		addView(textview);
		setOnClickListener(this);
	}
	
	public void setText(CharSequence text){
		textview.setText(text);
	}
	
	
	@Override
	public void onClick(View v) {
		
		// Flip between long and collapsed directions
		CharSequence cur = textview.getText();
		if(cur.equals(directions_long)){
			textview.setText(directions_collapsed);
		}
		else if(cur.equals(directions_collapsed)){
			textview.setText(directions_long);
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
		sb.append("Route found: <b>" + overall_building1 + "</b> to <b>" + overall_building2 + "</b>");
		
		
		// Cutoff point for collapsed directions
		directions_collapsed = Html.fromHtml("[<tt>+</tt>] " + sb.toString());
		
		
		sb.append("<br><br>");
		
		// Start
		sb.append("Start at <b>" + overall_building1 + " (floor " + overall_floor1 + ")</b>");
		sb.append("<br><br>");
		
		List<RouteStep> steps = route.getRouteSteps();
		for(int i = 0; i < steps.size(); i++){
			
			RouteStep step = steps.get(i);
			
			int floor1 = step.getStart().getFloorNumber();
			String build2 = step.getEnd().getBuildingName();
			String build2_formatted = "<b>" + build2 + "</b>";
			int floor2 = step.getEnd().getFloorNumber();
			
			String instr = "";
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
						+ " to " + build2_formatted + " <b>(floor " + floor2 + ")</b>";
			}else{
				throw new RuntimeException("Bad path type");
			}
			
			sb.append("  " + (i+1) + ". " + instr);
			sb.append("<br>");
		}
		
		sb.append("<br>");
		sb.append("Arrive at <b>" + overall_building2 + " (floor " + overall_floor2 + ")</b>");
		
		
		// Long directions
		directions_long = Html.fromHtml("[<tt>-</tt>] " + sb.toString());
		setText(directions_long);
	}
	
}
