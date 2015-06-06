
#import <Foundation/Foundation.h>
#import "Route.h"

@implementation Route

- (Route*) init{
    _routeSteps = [[NSMutableArray alloc] init];
    return self;
}

- (double) getTotalCost{
    return 0;
}

- (void) addStep:(RouteStep *)step{
    [_routeSteps addObject:step];
}

@end
