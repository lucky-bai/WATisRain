package com.example.watisrain;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import uk.co.senab.photoview.*;

public class MainActivity extends Activity {
	
	
	PhotoViewAttacher attacher;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        PhotoView mapImageView = (PhotoView) findViewById(R.id.mapImageView);
        attacher = new PhotoViewAttacher(mapImageView);
        attacher.setMaximumScale(8);
    }
    
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	// Make the zoom reasonable
        attacher.setScale(attacher.getMediumScale());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
