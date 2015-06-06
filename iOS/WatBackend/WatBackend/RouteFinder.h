
#ifndef WatBackend_RouteFinder_h
#define WatBackend_RouteFinder_h
#import "Map.h"
#import "Route.h"

@interface RouteFinder : NSObject

@property Map* map;

- (RouteFinder*) initWithMap: (Map*) map;
- (Route*) findRouteFrom: (Building*) build1 To:(Building*) build2;

- (Route*) findRouteFromLoc: (Location*) loc1 To:(Location*) loc2;
- (Route*) dijkstraFrom: (Location*) loc1 To:(Location*) loc2;
- (NSArray*) getAdjacentLocations: (Location*) loc;

@end

#endif
