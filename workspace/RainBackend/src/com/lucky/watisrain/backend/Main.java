package com.lucky.watisrain.backend;

import java.io.File;
import java.util.Arrays;

import com.lucky.watisrain.backend.data.*;

public class Main {

	public static void main(String... args) throws Throwable{
		
		File file = new File("C:/Users/Bai/Desktop/dev/watisrain/locations.txt");
		Map map = MapFactory.readMapFromFile(file);
		
		map.printDataToStdout();
		
	}
	
}
