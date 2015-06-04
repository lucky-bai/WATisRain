
#import <Foundation/Foundation.h>
#import "Path.h"

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
    assert([[waypoints objectAtIndex:0] isEqualTo:[_pointA position]]);
    assert([[waypoints objectAtIndex:[waypoints count]-1] isEqualTo:[_pointB position]]);
    _waypoints = waypoints;
}

- (BOOL) isIndoors{
    switch (_pathType) {
        case TYPE_OUTSIDE:
        case TYPE_BRIEFLY_OUTSIDE:
            return true;
        case TYPE_INSIDE:
        case TYPE_STAIR:
        case TYPE_INDOOR_TUNNEL:
        case TYPE_UNDERGROUND_TUNNEL:
            return false;
        default:
            assert(false);
    }
}

- (NSString*) description{
    return [NSString stringWithFormat:@"Path: A=%@; B=%@", _pointA, _pointB];
}

@end
