package com.lucky.watisrain;

import com.lucky.watisrain.R;
import com.lucky.watisrain.map.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import uk.co.senab.photoview.*;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class MainActivity extends Activity {


	PhotoViewAttacher attacher;
	MapView mapView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.mapImageView);
		attacher = new PhotoViewAttacher(mapView);
		mapView.attacher = attacher;
		mapView.directionsView = (DirectionsView) findViewById(R.id.directions_view);
		attacher.setMaximumScale(6);

		// Listener called when it's tapped
		attacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {

				// X and Y positions relative to image. For example, middle of image
				// is 0.5, 0.5
				mapView.handleUserTap(arg1, arg2);
				mapView.invalidate();
			}
		});
		
	}


	@Override
	protected void onStart(){
		super.onStart();

		// Make the zoom reasonable
		attacher.setScale(1.6f, 2312f, 400f, true);

	}
	
	
	// Handle action bar
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.action_clear:
				mapView.clearRoute();
				break;
			case R.id.action_settings:
				Global.showSettings(this, mapView);
				break;
		}
		
		return true;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		mapView.clearBtn = menu.findItem(R.id.action_clear);
		if(mapView.clearBtn != null)
			mapView.clearBtn.setVisible(false);
		
		return true;
	}

}
