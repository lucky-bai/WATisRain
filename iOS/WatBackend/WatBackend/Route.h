
#ifndef WatBackend_Route_h
#define WatBackend_Route_h
#import "RouteStep.h"

@interface Route : NSObject

@property NSArray *routeSteps;

- (double) getTotalCost;
- (void) addStep:(RouteStep*) step;

@end

#endif
