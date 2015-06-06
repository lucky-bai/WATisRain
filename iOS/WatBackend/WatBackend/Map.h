
#ifndef WatBackend_Map_h
#define WatBackend_Map_h
#import "Building.h"
#import "Location.h"
#import "Path.h"

@interface Map : NSObject

@property NSMutableArray *buildings;
@property NSMutableArray *locations;
@property NSMutableArray *paths;

- (Map*) init;
- (void) addBuilding: (Building*) building;
- (void) addPassiveLocation: (Location*) location;
- (void) addPath: (Path*) path;

- (Building*) getBuildingByID: (NSString*) name;
- (Location*) getLocationByID: (NSString*) name;
- (Path*) retrievePathFrom: (Location*)a To:(Location*)b;

@end

#endif
