package com.lucky.watisrain.map;

import java.util.List;

import com.lucky.watisrain.Global;
import com.lucky.watisrain.backend.data.Path;
import com.lucky.watisrain.backend.data.Waypoint;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * This class handles drawing things to map-specific coordinates. This is necessary because
 * by default, we can only draw to screen coordinates, which is not useful when we want
 * an overlay over the map of the campus.
 */
public class MapDraw {

	private Canvas canvas;
	private RectF rct;
	
	private float offset_x;
	private float offset_y;
	private float scale;
	
	/**
	 * Constructs a MapDraw. This is specific to one rendering of the screen.
	 * @param canvas the canvas object to draw to
	 * @param displayRectangle the current field of view
	 */
	public MapDraw(Canvas canvas, RectF displayRectangle){
		this.canvas = canvas;
		rct = displayRectangle;
		
		// Compute offsets given display rectangle
		scale = (rct.right-rct.left) / Global.MAP_WIDTH * Global.MAP_ADJUST_SCALING;
		offset_x = -rct.left + Global.MAP_ADJUST_X*scale;
		offset_y = -rct.top + Global.MAP_ADJUST_Y*scale;
	}
	
	
	/**
	 * Draws a Path object, including all waypoints.
	 * @param linewidth the width of the path (in map relative pixels)
	 */
	public void drawPathOnMap(Path path, float linewidth, Paint paint){
		
		List<Waypoint> wps = path.getWaypoints();
		
		for(int i=0; i<wps.size()-1; i++){
			drawLineOnMap(wps.get(i).getX(), wps.get(i).getY(), wps.get(i+1).getX(), wps.get(i+1).getY(), linewidth, paint);
		}
		
	}
	
	
	/*
	 * More primitive routines here
	 */
	
	/**
	 * Draw a circle in map relative units
	 */
	public void drawCircleOnMap(float x, float y, float radius, Paint paint){
		
		float adjust_x = x * scale - offset_x;
		float adjust_y = y * scale - offset_y;
		
		canvas.drawCircle(adjust_x, adjust_y, radius*scale, paint);
	}
	
	/**
	 * Draw a line in map relative units. The line goes from the point
	 * (x1,y1) to the point (x2,y2).
	 * @param linewidth how wide is the line (map relative pixels)
	 */
	public void drawLineOnMap(float x1, float y1, float x2, float y2, float linewidth, Paint paint){
		
		// Don't draw a line from a point to itself. Doing so causes a
		// problem on some devices.
		if(x1 == x2 && y1 == y2)
			return;
		
		float adjust_x1 = x1 * scale - offset_x;
		float adjust_y1 = y1 * scale - offset_y;
		float adjust_x2 = x2 * scale - offset_x;
		float adjust_y2 = y2 * scale - offset_y;
		
		paint.setStrokeWidth(linewidth*scale);
		
		canvas.drawLine(adjust_x1, adjust_y1, adjust_x2, adjust_y2, paint);
	}
	
	
	/**
	 * Draws a bitmap in map relative units. Assumes the image is square.
	 */
	public void drawImageOnMap(Bitmap bitmap, float x, float y, float width){
		
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
