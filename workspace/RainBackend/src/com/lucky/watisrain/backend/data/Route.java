package com.lucky.watisrain.backend.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A Route is a journey from point A to point B. It is composed of a list of
 * RouteSteps, each of which represents a step of the journey.
 */
public class Route {

	private List<RouteStep> routeSteps;
	
	public Route(){
		routeSteps = new ArrayList<>();
	}
	
	public void addStep(RouteStep step){
		routeSteps.add(step);
	}
	
	public Location getStart(){
		return routeSteps.get(0).getStart();
	}
	
	public Location getEnd(){
		return routeSteps.get(routeSteps.size()-1).getEnd();
	}
	
	public List<RouteStep> getRouteSteps(){
		return routeSteps;
	}
	
	public void printRouteToStdout(){
		double cumDistance = 0;
		for(RouteStep step : routeSteps){
			cumDistance += step.getCost();
			System.out.println(step + ". Total = " + (int)cumDistance);
		}
	}
	
}
