package com.lucky.watisrain;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.lucky.watisrain.backend.MapFactory;
import com.lucky.watisrain.backend.RouteFinder;
import com.lucky.watisrain.backend.data.Building;
import com.lucky.watisrain.backend.data.Location;
import com.lucky.watisrain.backend.data.Map;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Route;
import com.lucky.watisrain.backend.data.RouteStep;
import com.lucky.watisrain.backend.data.Waypoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Class that draws the map. It handles reading and storing the Map object.
 */
public class MapView extends PhotoView {
	
	// Needed because PhotoView.getDisplayRect doesn't actually work
	PhotoViewAttacher attacher;
	Map map;
	RouteFinder routefinder;
	
	// Current route state
	String selectedBuilding1 = null;
	String selectedBuilding2 = null;
	Route route = null;

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Handle reading map file
		try {
			InputStream in = context.getAssets().open("locations.txt");
			map = MapFactory.readMapFromStream(in);
		} catch (IOException e) {
			Global.println(e);
		}
		
		routefinder = new RouteFinder(map);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		
		for(Building building : map.getBuildings()){
			Waypoint pos = building.getMainFloor().getPostion();
			paint.setColor(Color.BLACK);
			
			if(building.getName().equals(selectedBuilding1) ||
			   building.getName().equals(selectedBuilding2))
				paint.setColor(Color.RED);
			
			drawCircleOnMap(canvas, pos.getX(), pos.getY(), 16, paint);
		}
		
		for(Location passive_loc : map.getLocations()){
			if(passive_loc.isPassive()){
				Waypoint pos = passive_loc.getPostion();
				paint.setColor(Color.BLACK);
				
				drawCircleOnMap(canvas, pos.getX(), pos.getY(), 7, paint);
			}
		}
		
		for(Path path : map.getPaths()){
			Waypoint pos1 = path.getPointA().getPostion();
			Waypoint pos2 = path.getPointB().getPostion();
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(3);
			drawLineOnMap(canvas, pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), paint);
		}
		
		// draw route
		if(route != null){
			for(RouteStep step : route.getRouteSteps()){
				Waypoint pos1 = step.getStart().getPostion();
				Waypoint pos2 = step.getEnd().getPostion();
				
				paint.setColor(Color.RED);
				paint.setStrokeWidth(8);
				drawLineOnMap(canvas, pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), paint);
			}
		}
		
	}
	
	
	/**
	 * Called when user taps at location x,y on map
	 * 
	 * The units are relative to the image and between 0.0 and 1.0. That is, the center
	 * of the map is (0.5, 0.5)
	 */
	public void handleUserTap(float x, float y){
		
		// Convert to map units
		float map_x = x * Global.MAP_WIDTH;
		float map_y = y * Global.MAP_HEIGHT;
		
		Building closestBuilding = determineBuildingFromPosition(map_x, map_y, 100);
		
		if(closestBuilding == null){
			selectedBuilding1 = null;
			selectedBuilding2 = null;
			route = null;
		}
		else if(selectedBuilding1 == null){
			selectedBuilding1 = closestBuilding.getName();
		}else if(selectedBuilding2 == null){
			selectedBuilding2 = closestBuilding.getName();
			updateRoute();
		}else{
			selectedBuilding1 = null;
			selectedBuilding2 = null;
			route = null;
		}
		
	}
	
	
	// Assumes selectedBuilding[1,2] are the correct ones.
	private void updateRoute(){
		
		route = routefinder.findRoute(map.getLocationByID(selectedBuilding1),
									  map.getLocationByID(selectedBuilding2));
		
	}
	
	
	/**
	 * Given a set of map coordinates (x,y), return the building closest to
	 * it, or null if there is no building close by.
	 * 
	 * @param threshold maximum allowed distance in map-pixels
	 */
	private Building determineBuildingFromPosition(float x, float y, float threshold){
		
		List<Building> buildings = map.getBuildings();
		
		Building closest = null;
		float closest_dist = Float.MAX_VALUE;
		
		for(Building building : buildings){
			float dist = (float)building.getMainFloor().getPostion().distanceTo(new Waypoint((int)x,(int)y));
			if(dist < closest_dist){
				closest = building;
				closest_dist = dist;
			}
		}
		
		if(closest_dist > threshold)
			closest = null;
		
		return closest;
	}
	
	
	/**
	 * Draw a circle in map relative units
	 */
	private void drawCircleOnMap(Canvas canvas, float x, float y, float radius, Paint paint){
		RectF rct = attacher.getDisplayRect();
		float offset_x = -rct.left;
		float offset_y = -rct.top;
		float scale = (rct.right-rct.left) / Global.MAP_WIDTH;
		
		float adjust_x = x * scale - offset_x;
		float adjust_y = y * scale - offset_y;
		
		canvas.drawCircle(adjust_x, adjust_y, radius*scale, paint);
	}
	
	/**
	 * Draw a line in map relative units
	 */
	private void drawLineOnMap(Canvas canvas, float x1, float y1, float x2, float y2, Paint paint){
		RectF rct = attacher.getDisplayRect();
		float offset_x = -rct.left;
		float offset_y = -rct.top;
		float scale = (rct.right-rct.left) / Global.MAP_WIDTH;
		
		float adjust_x1 = x1 * scale - offset_x;
		float adjust_y1 = y1 * scale - offset_y;
		float adjust_x2 = x2 * scale - offset_x;
		float adjust_y2 = y2 * scale - offset_y;
		
		canvas.drawLine(adjust_x1, adjust_y1, adjust_x2, adjust_y2, paint);
	}

}
