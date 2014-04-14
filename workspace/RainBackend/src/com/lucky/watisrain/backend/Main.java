package com.lucky.watisrain.backend;

import java.util.Arrays;

import com.lucky.watisrain.backend.data.*;

public class Main {

	public static void main(String... args){
		
		Waypoint point00 = new Waypoint(0,0);
		Waypoint point10 = new Waypoint(1,0);
		Waypoint point11 = new Waypoint(1,1);
		
		Path path = new Path(Arrays.asList(point00,point10,point11));
		
		System.out.println(path);
		System.out.println(path.getPointA());
		System.out.println(path.getPointB());
	}
	
}
