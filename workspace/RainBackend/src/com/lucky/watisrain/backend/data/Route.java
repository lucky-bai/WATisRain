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
		
		Route contractedRoute = new Route();
		
		int step = 0;
		Location lastNoteworthyNode = routeSteps.get(0).getStart();
		RouteStep routeToMerge = null;
		
		while(step < routeSteps.size()){
			
			// Precondition:
			// routeToMerge is empty, lastWorthyNode at active location
			
			while(step < routeSteps.size()){
				RouteStep cur_step = routeSteps.get(step);
				
				boolean is_different_building = !cur_step.getEnd().getPostion().equals(lastNoteworthyNode.getPostion());
				if(cur_step.getEnd().isPassive()) is_different_building = false;
				
				// If it's the first step, always merge, or we have a null route
				if(routeToMerge == null){
					routeToMerge = cur_step;
					step++;
				}
				
				// if start is passive but we're entering a building, add it and break
				else if(cur_step.getStart().isPassive() && is_different_building){
					routeToMerge = routeToMerge.mergeWith(cur_step);
					step++;
					break;
				}
				
				// if we're just entering a new building, don't add, just break
				else if(is_different_building){
					break;
				}
				
				// Add it and don't break
				else{
					routeToMerge = routeToMerge.mergeWith(cur_step);
					step++;
				}
			}
			
			contractedRoute.addStep(routeToMerge);
			lastNoteworthyNode = routeToMerge.getEnd();
			routeToMerge = null;

		}
		
		return contractedRoute;
	}
	
	
	public void printRouteToStdout(){
		double cumDistance = 0;
		for(RouteStep step : routeSteps){
			cumDistance += step.getCost();
			System.out.println(step + ". Total = " + (int)cumDistance);
		}
	}
	
}
