
#import <Foundation/Foundation.h>
#import "Waypoint.h"
#import "Util.h"
#import "MapFactory.h"
#import "RouteFinder.h"
#import "Map.h"
#import "Location.h"
#import "Route.h"
#import "Path.h"
#import "RouteStep.h"


int main(int argc, const char * argv[]) {
    @autoreleasepool {
        Map *map = [MapFactory readMapFromPath:@"/Users/bai-personal/Documents/WATisRain/deprecated/locations.txt"];
        
        RouteFinder *rf = [[RouteFinder alloc] initWithMap:map];
        Route *rt = [rf findRouteFrom:[map getBuildingByID:@"MC"] To:[map getBuildingByID:@"SCH"]];
        
        for(RouteStep *rts in [rt routeSteps]){
            NSLog(@"%@ -> %@", [rts start], [rts end]);
        }
    }
    return 0;
}
