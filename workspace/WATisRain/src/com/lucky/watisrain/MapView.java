package com.lucky.watisrain;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.example.watisrain.R;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Class that draws the map. It handles reading and storing the Map object.
 */
public class MapView extends PhotoView {
	
	// Needed because PhotoView.getDisplayRect doesn't actually work
	PhotoViewAttacher attacher;
	
	HashMap<String, Bitmap> imgs;
	
	// Currently a TextView, since I don't know what UI widgets android has.
	// Will investigate better options later.
	TextView directionsView;
	
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
		
		// Read bitmaps
		imgs = new HashMap<String, Bitmap>();
		imgs.put("default_location.png", BitmapFactory.decodeResource(getResources(), R.drawable.default_location));
		imgs.put("active_location.png", BitmapFactory.decodeResource(getResources(), R.drawable.active_location));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		
		/*
		for(Location passive_loc : map.getLocations()){
			if(passive_loc.isPassive()){
				Waypoint pos = passive_loc.getPostion();
				paint.setColor(Color.BLACK);
				
				drawCircleOnMap(canvas, pos.getX(), pos.getY(), 7, paint);
			}
		}
		
		for(Path path : map.getPaths()){
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(2);
			drawPathOnMap(canvas, path, paint);
		}
		*/
		
		// Draw all locations
		for(Building building : map.getBuildings()){
			Waypoint pos = building.getMainFloor().getPostion();
			drawImageOnMap(canvas, imgs.get("default_location.png"),pos.getX(),pos.getY(),120);
		}
		
		// draw route
		if(route != null){
			for(RouteStep step : route.getRouteSteps()){
				paint.setColor(Color.parseColor("#0070cf"));
				paint.setStrokeWidth(12);
				paint.setStrokeCap(Paint.Cap.ROUND);
				drawPathOnMap(canvas, step.getPath(), paint);
			}
		}
		
		// Draw active locations
		for(Building building : map.getBuildings()){
			Waypoint pos = building.getMainFloor().getPostion();
			
			if(building.getName().equals(selectedBuilding1) ||
			   building.getName().equals(selectedBuilding2)){
				
				drawImageOnMap(canvas, imgs.get("active_location.png"),pos.getX(),pos.getY(),120);
			}
		}
		
	}
	
	
	/**
	 * Draws a Path object, including all waypoints
	 */
	private void drawPathOnMap(Canvas canvas, Path path, Paint paint){
		
		List<Waypoint> wps = path.getWaypoints();
		
		for(int i=0; i<wps.size()-1; i++){
			drawLineOnMap(canvas, wps.get(i).getX(), wps.get(i).getY(), wps.get(i+1).getX(), wps.get(i+1).getY(), paint);
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
		String status = "";
		
		if(closestBuilding == null){
			selectedBuilding1 = null;
			selectedBuilding2 = null;
			route = null;
		}
		else if(selectedBuilding1 == null){
			selectedBuilding1 = closestBuilding.getName();
			status = "Selected: " + selectedBuilding1;
		}else if(selectedBuilding2 == null){
			selectedBuilding2 = closestBuilding.getName();
			status = "Route found: " + selectedBuilding1 + " -> " + selectedBuilding2;
			updateRoute();
		}else{
			selectedBuilding1 = null;
			selectedBuilding2 = null;
			route = null;
		}
		
		// Update text
		if(!status.isEmpty()){
			directionsView.setText(status);
		}else{
			directionsView.setText("Touch the map to select a destination");
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
	
	
	/**
	 * Draws a bitmap in map relative units. Assumes the image is square.
	 */
	private void drawImageOnMap(Canvas canvas, Bitmap bitmap, float x, float y, float width){
		
		// Standard map -> screen adjust
		RectF rct = attacher.getDisplayRect();
		float offset_x = -rct.left;
		float offset_y = -rct.top;
		float scale = (rct.right-rct.left) / Global.MAP_WIDTH;
		
		float adjust_x = x * scale - offset_x;
		float adjust_y = y * scale - offset_y;
		float scwidth = scale*width;
		
		// Scale image down
		int img_width = bitmap.getWidth();
		int img_height = bitmap.getHeight();
		
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setRectToRect(
				new RectF(0,0,img_width,img_height),
				new RectF(adjust_x-scwidth/2,adjust_y-scwidth/2,adjust_x+scwidth/2,adjust_y+scwidth/2),
				Matrix.ScaleToFit.CENTER);
		canvas.drawBitmap(bitmap, scaleMatrix, null);
	}

}
