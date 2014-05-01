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
		
		// Mark the indices for which we want to merge route[i] with route[i+1]
		ArrayList<Integer> marked = new ArrayList<>();
		for(int i=0; i<routeSteps.size()-1; i++){
			
			Location p1 = routeSteps.get(i).getStart();
			Location p2 = routeSteps.get(i).getEnd();
			Location p3 = routeSteps.get(i+1).getEnd();
			
			// Two cases in which we combine.
			
			// Case 1: middle node is passive
			if(p2.isPassive()){
				marked.add(i);
			}
			
			// Case 2: A->B->C where everything is in the same building
			if(p1.getPostion().equals(p3.getPostion())){
				marked.add(i);
			}
		}
		
		Route contractedRoute = new Route();
		RouteStep cur_step = null;
		int step = 0;
		while(step < routeSteps.size()){
			
			boolean merge = marked.contains(step);
			
			// If it's null, make it not null
			if(cur_step == null){
				cur_step = routeSteps.get(step);
			}
			
			// If we do merge
			if(merge){
				cur_step = cur_step.mergeWith(routeSteps.get(step+1));
				step++;
			}
			
			// If we don't merge
			else{
				contractedRoute.addStep(cur_step);
				cur_step = null;
				step++;
			}
			
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
