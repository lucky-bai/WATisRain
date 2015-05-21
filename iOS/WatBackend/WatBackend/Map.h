
#ifndef WatBackend_Map_h
#define WatBackend_Map_h
#import "Building.h"
#import "Location.h"
#import "Path.h"

@interface Map : NSObject

@property NSArray *buildings;
@property NSArray *locations;
@property NSArray *paths;

- (void) addBuilding: (Building*) building;
- (void) addPassiveLocation: (Location*) location;
- (void) addPath: (Path*) path;

@end

#endif
