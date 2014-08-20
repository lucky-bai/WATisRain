package com.lucky.watisrain.map;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.lucky.watisrain.Global;
import com.lucky.watisrain.R;
import com.lucky.watisrain.backend.MapFactory;
import com.lucky.watisrain.backend.RouteFinder;
import com.lucky.watisrain.backend.Util;
import com.lucky.watisrain.backend.data.Building;
import com.lucky.watisrain.backend.data.Map;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Route;
import com.lucky.watisrain.backend.data.RouteStep;
import com.lucky.watisrain.backend.data.Waypoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Class that draws the map. It handles reading and storing the Map object.
 */
public class MapView extends PhotoView {
	
	// Needed because PhotoView.getDisplayRect doesn't actually work
	public PhotoViewAttacher attacher;
	
	public HashMap<String, Bitmap> imgs;
	
	// Contains a TextView that displays directions
	public DirectionsView directionsView;
	
	public MenuItem clearBtn;
	
	public Map map;
	public RouteFinder routefinder;
	
	// Current route state
	String selectedBuilding1 = null;
	String selectedBuilding2 = null;
	Route route = null;

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);

		AssetManager assetManager = context.getAssets();

		if(assetManager != null) {
			// Handle reading map file
			try {
				InputStream in = assetManager.open("locations.txt");
				map = MapFactory.readMapFromStream(in);
			} catch (IOException e) {
				Global.println(e);
			}
		} else {
			map = new Map();
		}
		
		routefinder = new RouteFinder(map);
		
		// Read bitmaps
		imgs = new HashMap<String, Bitmap>();
		imgs.put("default_location.png", BitmapFactory.decodeResource(getResources(), R.drawable.default_location));
		imgs.put("active_location.png", BitmapFactory.decodeResource(getResources(), R.drawable.active_location));
		imgs.put("stairs_up.png", BitmapFactory.decodeResource(getResources(), R.drawable.stairs_up));
		imgs.put("stairs_down.png", BitmapFactory.decodeResource(getResources(), R.drawable.stairs_down));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();

		RectF displayRect = new RectF();

		if(attacher != null) {
			displayRect = attacher.getDisplayRect();
		}

		MapDraw mapdraw = new MapDraw(canvas, displayRect);
		
		// Draw all locations
		for(Building building : map.getBuildings()){
			
			if(!building.isSelectable()) continue;
			
			Waypoint pos = building.getMainFloor().getPostion();
			mapdraw.drawImageOnMap(imgs.get("default_location.png"),pos.getX(),pos.getY(),120);
		}
		
		// draw route
		if(route != null){
			
			List<RouteStep> all_steps = route.getRouteSteps();
			for(RouteStep step : all_steps){
				paint.setColor(Color.parseColor("#0070cf"));
				paint.setStrokeCap(Paint.Cap.ROUND);
				mapdraw.drawPathOnMap(step.getPath(), 8.0f, paint);
			}
		}
		
		// Draw active locations
		for(Building building : map.getBuildings()){
			Waypoint pos = building.getMainFloor().getPostion();
			
			if(building.getName().equals(selectedBuilding1) ||
			   building.getName().equals(selectedBuilding2)){
				
				mapdraw.drawImageOnMap(imgs.get("active_location.png"),pos.getX(),pos.getY(),120);
			}
		}
		
		
		
		// The following code handles drawing stairs icons.
		if(route != null){
			
			List<RouteStep> all_steps = route.getRouteSteps();
			
			// Get list of buildings we go through
			ArrayList<String> throughBuildings = new ArrayList<String>();
			throughBuildings.add(all_steps.get(0).getStart().getBuildingName());
			for(RouteStep step : all_steps) {
				String next_build = step.getEnd().getBuildingName();
				if(!throughBuildings.contains(next_build))
					throughBuildings.add(next_build);
			}
			
			// All waypoints that we go through
			ArrayList<Waypoint> throughWaypoints = new ArrayList<Waypoint>();
			for(RouteStep step : all_steps){
				throughWaypoints.addAll(step.getWaypoints());
			}
			// Filter duplicates
			throughWaypoints = new ArrayList<Waypoint>(new LinkedHashSet<Waypoint>(throughWaypoints));
			
			for(String buildingName : throughBuildings){
				int ix = throughWaypoints.indexOf(map.getBuildingByID(buildingName).getPosition());
				
				// Generate 3 waypoints to get our vectors from
				Waypoint wp_cur = throughWaypoints.get(ix);
				Waypoint wp_before;
				if(ix==0){
					wp_before = throughWaypoints.get(1);
				}else{
					wp_before = throughWaypoints.get(ix-1);
				}
				Waypoint wp_after;
				if(ix==throughWaypoints.size()-1){
					wp_after = throughWaypoints.get(throughWaypoints.size()-2);
				}else{
					wp_after = throughWaypoints.get(ix+1);
				}
				
				// Compute opposite vector
				double[] vec_c = Util.findOppositeVector(
						wp_before.getX()-wp_cur.getX(),
						wp_before.getY()-wp_cur.getY(),
						wp_after.getX()-wp_cur.getX(),
						wp_after.getY()-wp_cur.getY());
				
				
				// Determine whether we actually draw a stairs here
				boolean stairs_down = false;
				boolean stairs_up = false;
				for(RouteStep step : all_steps){
					if(step.getStart().getBuildingName().equals(buildingName)
							&& step.getPathType() == Path.TYPE_STAIR){
						if(step.getStart().getFloorNumber() > step.getEnd().getFloorNumber()){
							stairs_down = true;
						}else{
							stairs_up = true;
						}
					}
				}
				
				// Draw stair icons if needed
				if(stairs_down){
					mapdraw.drawImageOnMap(imgs.get("stairs_down.png"), (float)vec_c[0]*25+wp_cur.getX(), (float)vec_c[1]*25+wp_cur.getY(), 35);
				}
				if(stairs_up){
					mapdraw.drawImageOnMap(imgs.get("stairs_up.png"), (float)vec_c[0]*25+wp_cur.getX(), (float)vec_c[1]*25+wp_cur.getY(), 35);
				}
				
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
		
		if(clearBtn != null)
			clearBtn.setVisible(true);
		
		// Convert to map units
		// On map_full, it is (x * width, y * height). Then we perform scaling.
		float map_x = (x * Global.MAP_WIDTH / Global.MAP_ADJUST_SCALING) + Global.MAP_ADJUST_X;
		float map_y = (y * Global.MAP_HEIGHT / Global.MAP_ADJUST_SCALING) + Global.MAP_ADJUST_Y;
		
		Building closestBuilding = determineBuildingFromPosition(map_x, map_y, 70);
		
		// Reset route if user clicks one of existing endpoints, or something outside
		if(closestBuilding == null ||
				closestBuilding.getName().equals(selectedBuilding1) ||
				closestBuilding.getName().equals(selectedBuilding2)){
			clearRoute();
		}
		else if(selectedBuilding1 == null){
			selectedBuilding1 = closestBuilding.getName();
			directionsView.selectDestination(selectedBuilding1);
		}else{
			selectedBuilding2 = closestBuilding.getName();
			
			updateRoute();
		}
		
	}
	
	
	/**
	 * Clear any route or selected buildings, as well as text view
	 */
	public void clearRoute(){
		selectedBuilding1 = null;
		selectedBuilding2 = null;
		route = null;
		directionsView.unselectDestination();
		if(clearBtn != null)
			clearBtn.setVisible(false);
		invalidate();
	}
	
	
	// Assumes selectedBuilding[1,2] are the correct ones.
	private void updateRoute(){
		
		route = routefinder.findRoute(map.getBuildingByID(selectedBuilding1),
									  map.getBuildingByID(selectedBuilding2)).getContractedRoute();
		
		directionsView.generateDirectionsFromRoute(route);
	}
	
	
	/**
	 * Recalculate the route if applicable, possibly with a different global pathing
	 * value. If no route is selected, do nothing.
	 */
	public void recalculateRoute(){
		if(selectedBuilding1 == null || selectedBuilding2 == null) return;
		
		updateRoute();
		invalidate();
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
			
			if(!building.isSelectable()) continue;
			
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

}
