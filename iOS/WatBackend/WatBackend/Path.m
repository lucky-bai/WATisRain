
#import <Foundation/Foundation.h>
#import "Path.h"
#import "Util.h"

@implementation Path

- (Path*) initWithLocationA:(Location *)pointA withLocationB:(Location *)pointB{
    _pointA = pointA;
    _pointB = pointB;
    
    _waypoints = [[NSMutableArray alloc] init];
    [_waypoints addObject:[_pointA position]];
    [_waypoints addObject:[_pointB position]];
    
    _pathType = TYPE_OUTSIDE;
    return self;
}

- (void) setWaypointsInfo:(NSMutableArray *)waypoints{
    assert([[waypoints objectAtIndex:0] isEqual:[_pointA position]]);
    assert([[waypoints objectAtIndex:[waypoints count]-1] isEqual:[_pointB position]]);
    _waypoints = waypoints;
}

- (BOOL) isIndoors{
    switch (_pathType) {
        case TYPE_OUTSIDE:
        case TYPE_BRIEFLY_OUTSIDE:
            return false;
        case TYPE_INSIDE:
        case TYPE_STAIR:
        case TYPE_INDOOR_TUNNEL:
        case TYPE_UNDERGROUND_TUNNEL:
            return true;
        default:
            assert(false);
    }
}

- (double) getCost{
    if(_pathType == TYPE_STAIR){
        int floor1 = [_pointA getFloorNumber];
        int floor2 = [_pointB getFloorNumber];
        int floor_diff = floor1 - floor2;
        if(floor_diff < 0) floor_diff = -floor_diff;
        return floor_diff * 25 + 50;
    }
    
    double distance = 0;
    for(int i=0; i<[_waypoints count]-1; i++){
        distance += [[_waypoints objectAtIndex:i] distanceTo:[_waypoints objectAtIndex:i+1]];
    }
    
    if(![self isIndoors]) return GLOBAL_PATHING_WEIGHT * distance;
    else return distance;
}

- (NSString*) description{
    return [NSString stringWithFormat:@"Path: A=%@; B=%@; d=%0.2f", _pointA, _pointB, [self getCost]];
}

@end
