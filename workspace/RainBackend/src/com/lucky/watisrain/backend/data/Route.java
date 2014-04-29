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
	
	/*
	 * Sum of costs of all RouteSteps
	 */
	public double getTotalCost(){
		double sum = 0;
		for(RouteStep step : routeSteps){
			sum += step.getCost();
		}
		return sum;
	}
	
	
	/*
	 * A contracted route skips over passive locations, or if you go up multiple
	 * consecutive stairs.
	 */
	public Route getContractedRoute(){
		// TODO
		return null;
	}
	
	
	public void printRouteToStdout(){
		double cumDistance = 0;
		for(RouteStep step : routeSteps){
			cumDistance += step.getCost();
			System.out.println(step + ". Total = " + (int)cumDistance);
		}
	}
	
}
