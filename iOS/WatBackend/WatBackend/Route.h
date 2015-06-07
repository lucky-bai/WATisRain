
#ifndef WatBackend_Route_h
#define WatBackend_Route_h
#import "RouteStep.h"

@interface Route : NSObject

@property NSMutableArray *routeSteps;

- (Route*) init;
- (double) getTotalCost;
- (void) addStep:(RouteStep*) step;

- (Route*) getContractedRoute;

@end

#endif
