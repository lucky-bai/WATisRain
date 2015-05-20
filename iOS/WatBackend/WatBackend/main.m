
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
        [MapFactory readMapFromPath:@"/Users/bai-personal/Documents/WATisRain/deprecated/locations.txt"];
    }
    return 0;
}
