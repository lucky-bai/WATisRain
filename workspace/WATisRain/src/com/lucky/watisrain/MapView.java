package com.lucky.watisrain;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapView extends PhotoView {
	
	// Needed because PhotoView.getDisplayRect doesn't actually work
	PhotoViewAttacher attacher;

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		RectF rct = attacher.getDisplayRect();
		float offset_x = -rct.left;
		float offset_y = -rct.top;
		float scale = (rct.right-rct.left)/3175;
		
		// draw something centered at (2397,1317)
		float adjust_x = 2397 * scale - offset_x;
		float adjust_y = 1317 * scale - offset_y;
		
		canvas.drawCircle(adjust_x, adjust_y, 100*scale, new Paint());
		
	}

}
