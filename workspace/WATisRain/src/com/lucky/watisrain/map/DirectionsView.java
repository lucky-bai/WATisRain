package com.lucky.watisrain.map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * The widget that displays the directions in a list
 */
public class DirectionsView extends LinearLayout {
	
	TextView textview;

	public DirectionsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		textview = new TextView(context);
		textview.setText("Touch the map to select a destination");
		addView(textview);
	}
	
	public void setText(CharSequence text){
		textview.setText(text);
	}
	
}
