
#import <Foundation/Foundation.h>
#import "RouteFinder.h"
#import "Path.h"
#import "Location.h"
#import "Map.h"

@interface LocationAndDistance : NSObject

@property Location* location;
@property double distance;

- (LocationAndDistance*) initWithLocation:(Location*) loc andDistance:(double)dist;

@end

@implementation LocationAndDistance

- (LocationAndDistance*) initWithLocation:(Location *)loc andDistance:(double)dist{
    _location = loc;
    _distance = dist;
    return self;
}

- (NSString*) description{
    return [NSString stringWithFormat:@"%0.1f:%@", _distance, _location];
}

@end

@implementation RouteFinder

- (RouteFinder*) initWithMap:(Map *)map{
    _map = map;
    return self;
}

- (Route*) findRouteFrom:(Building *)build1 To:(Building *)build2{
    double min_cost = DBL_MAX;
    Route *min_route = nil;
    Location *start_location = [build1 getMainFloor];
    
    for(Location *end_location in [build2 getAllFloors]){
        Route *cur_route = [self findRouteFromLoc:start_location To:end_location];
        double cur_cost = [cur_route getTotalCost];
        
        if(cur_cost < min_cost){
            min_cost = cur_cost;
            min_route = cur_route;
        }
    }
    
    return min_route;
}

- (Route*) findRouteFromLoc:(Location *)loc1 To:(Location *)loc2{
    NSArray *routelist = [self dijkstraFrom:loc1 To:loc2];
    
    Route *route = [[Route alloc] init];
    Location *cur = loc1;
    
    for(Location *loc in routelist){
        if([cur isEqual:loc]) continue;
        [route addStep:[[RouteStep alloc] initWithStart:cur withEnd:loc withPath:[_map retrievePathFrom:cur To:loc]]];
        cur = loc;
    }
    
    return route;
}

- (Route*) dijkstraFrom: (Location*) loc1 To:(Location*) loc2{
    NSMutableArray *queue = [[NSMutableArray alloc] init];
    NSMutableArray *visited = [[NSMutableArray alloc] init];
    
    NSMutableDictionary *distTable = [[NSMutableDictionary alloc] init];
    for(Location *loc in [_map locations]){
        distTable[loc] = [NSNumber numberWithDouble:DBL_MAX];
    }
    distTable[loc1] = [NSNumber numberWithDouble:0];
    
    [queue addObject:loc1];
    
    while([queue count] > 0){
        double min_cost = DBL_MAX;
        int min_index = 0;
        for(int i=0; i<[queue count]; i++){
            if([[distTable objectForKey:[queue objectAtIndex:i]] doubleValue] < min_cost){
                min_cost = [[distTable objectForKey:[queue objectAtIndex:i]] doubleValue];
                min_index = i;
            }
        }
        Location *cur = [queue objectAtIndex:min_index];
        [queue removeObjectAtIndex:min_index];
        
        NSArray *nextlist = [self getAdjacentLocations:cur];
        for(LocationAndDistance *next in nextlist){
            
            if(![visited containsObject:[next location]]){
                double alt_dist = [[distTable objectForKey:cur] doubleValue] + [next distance];
                double existing_dist = [[distTable objectForKey:[next location]] doubleValue];
                if(alt_dist < existing_dist){
                    distTable[[next location]] = [NSNumber numberWithDouble:alt_dist];
                    [queue addObject:[next location]];
                }
            }
            
        }
        
        [visited addObject:cur];
    }
    
    
    NSMutableArray *calcPath = [[NSMutableArray alloc] init];
    [calcPath addObject:loc2];
    
    Location *backtrack = loc2;
    while(![backtrack isEqual:loc1]){
        
        NSMutableArray *prevlist = [self getAdjacentLocations:backtrack];
        for(LocationAndDistance *prev in prevlist){
            if(fabs([prev distance] + [[distTable objectForKey:[prev location]] doubleValue] - [[distTable objectForKey:backtrack] doubleValue]) < 0.0001){
                [calcPath addObject:[prev location]];
                backtrack = [prev location];
            }
        }
        
    }
    
    calcPath = [[calcPath reverseObjectEnumerator] allObjects];
    
    return calcPath;
}

- (NSArray*) getAdjacentLocations: (Location*) loc{
    NSMutableArray *adjacents = [[NSMutableArray alloc] init];
    
    for(Path *path in [_map paths]){
        if([[path pointA] isEqual:loc]){
            double dist = [path getCost];
            [adjacents addObject:[[LocationAndDistance alloc] initWithLocation:[path pointB] andDistance:dist]];
        }
        if([[path pointB] isEqual:loc]){
            double dist = [path getCost];
            [adjacents addObject:[[LocationAndDistance alloc] initWithLocation:[path pointA] andDistance:dist]];
        }
    }
    
    return adjacents;
}

@end