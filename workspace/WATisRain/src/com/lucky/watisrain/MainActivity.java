package com.lucky.watisrain;

import com.example.watisrain.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

import uk.co.senab.photoview.*;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class MainActivity extends Activity {
	
	
	PhotoViewAttacher attacher;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        PhotoView mapImageView = (PhotoView) findViewById(R.id.mapImageView);
        attacher = new PhotoViewAttacher(mapImageView);
        attacher.setMaximumScale(6);
        

        
        // Listener called when it's tapped
        attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				
				// X and Y positions relative to image. For example, middle of image
				// is 0.5, 0.5
				
				Global.println(arg1 + "," + arg2);
				
			}
		});

    }
    
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	// Make the zoom reasonable
        attacher.setScale(2f, 1558f, 796f, true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
