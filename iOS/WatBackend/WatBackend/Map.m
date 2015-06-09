
#import <Foundation/Foundation.h>
#import "Map.h"
#import "Util.h"

@implementation Map

- (Map*) init{
    _buildings = [[NSMutableArray alloc] init];
    _locations = [[NSMutableArray alloc] init];
    _paths = [[NSMutableArray alloc] init];
    return self;
}

- (void) addBuilding:(Building *)building{
    [self.buildings addObject:building];
    [self.locations addObjectsFromArray:[building getAllFloors]];
    [self.paths addObjectsFromArray:[building getAllStairs]];
}

- (void) addPassiveLocation:(id)location{
    [self.locations addObject:location];
}

- (void) addPath:(Path *)path{
    [self.paths addObject:path];
}

- (Building*) getBuildingByID:(NSString*) name{
    for(Building *building in _buildings){
        if([[building name] isEqual:name])
            return building;
    }
    return nil;
}

- (Location*) getLocationByID:(NSString*) name{
    Building *building = [self getBuildingByID:name];
    if(building != nil)
        return [building getMainFloor];
    
    for(Location *loc in _locations){
        if([[loc name] isEqual:name])
            return loc;
    }
    
    NSString *partBuilding = getBuilding(name);
    for(Location *loc in _locations){
        if([[loc name] isEqual:partBuilding])
            return loc;
    }
    
    return nil;
}

- (Path*) retrievePathFrom:(Location *)a To:(Location *)b{
    for(Path *path in _paths){
        if([[path pointA] isEqual:a] && [[path pointB] isEqual:b])
            return path;
        if([[path pointB] isEqual:a] && [[path pointA] isEqual:b])
            return path;
    }
    return nil;
}

@end
