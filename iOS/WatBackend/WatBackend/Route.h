
#ifndef WatBackend_Route_h
#define WatBackend_Route_h
#import "RouteStep.h"
#import "Location.h"

@interface Route : NSObject

@property NSMutableArray *routeSteps;

- (Route*) init;
- (double) getTotalCost;
- (void) addStep:(RouteStep*) step;
- (Location*) getStart;
- (Location*) getEnd;

- (Route*) getContractedRoute;

@end

#endif
