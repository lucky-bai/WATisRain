
#import <Foundation/Foundation.h>
#import "Route.h"
#import "RouteStep.h"

@implementation Route

- (Route*) init{
    _routeSteps = [[NSMutableArray alloc] init];
    return self;
}

- (double) getTotalCost{
    double sum = 0;
    for(RouteStep *step in _routeSteps){
        sum += [step getCost];
    }
    return sum;
}

- (void) addStep:(RouteStep *)step{
    [_routeSteps addObject:step];
}

- (Location*)getStart{
    RouteStep *firstStep = [_routeSteps objectAtIndex:0];
    return [firstStep start];
}

- (Location*)getEnd{
    RouteStep *lastStep = [_routeSteps objectAtIndex:[_routeSteps count]-1];
    return [lastStep end];
}

- (Route*) getContractedRoute{
    NSMutableArray *marked = [[NSMutableArray alloc] init];
    for(int i=0; i<[_routeSteps count]-1; i++){
        
        RouteStep *rs_i = [_routeSteps objectAtIndex:i];
        RouteStep *rs_iplus1 = [_routeSteps objectAtIndex:i+1];
        Location *p1 = [rs_i start];
        Location *p2 = [rs_i end];
        Location *p3 = [rs_iplus1 end];
        
        if([p2 isPassive]){
            [marked addObject:[NSNumber numberWithInt:i]];
        }
        
        if([[p1 position] isEqual:[p3 position]]){
            [marked addObject:[NSNumber numberWithInt:i]];
        }
    }
    
    Route *contractedRoute = [[Route alloc] init];
    RouteStep *cur_step = nil;
    int step = 0;
    while(step < [_routeSteps count]){
        
        BOOL merge = [marked containsObject:[NSNumber numberWithInt:step]];
        
        if(cur_step == nil){
            cur_step = [_routeSteps objectAtIndex:step];
        }
        
        if(merge){
            cur_step = [cur_step mergeWith:[_routeSteps objectAtIndex:step+1]];
            step++;
        }
        else{
            [contractedRoute addStep:cur_step];
            cur_step = nil;
            step++;
        }
        
    }
    
    return contractedRoute;
}

@end
