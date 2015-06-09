
#import <Foundation/Foundation.h>
#import "RouteStep.h"

@implementation RouteStep

- (RouteStep*) initWithStart:(Location *)start withEnd:(Location *)end withPath:(Path *)path{
    _start = start;
    _end = end;
    _path = path;
    return self;
}

- (NSArray*) getWaypoints{
    NSMutableArray *ret = [[NSMutableArray alloc] init];
    [ret addObjectsFromArray:[_path waypoints]];
    
    if([[ret objectAtIndex:0] isEqual:[_start position]])
        return ret;
    
    ret = [[ret reverseObjectEnumerator] allObjects];
    return ret;
}

- (RouteStep*) mergeWith: (RouteStep*) other{
    
    NSMutableArray *waypoints = [[NSMutableArray alloc] init];
    [waypoints addObjectsFromArray:[self getWaypoints]];
    [waypoints removeLastObject];
    [waypoints addObjectsFromArray:[other getWaypoints]];
    
    Path *newpath = [[Path alloc] initWithLocationA:_start withLocationB:[other end]];
    [newpath setPathType:[_path pathType]];
    [newpath setWaypointsInfo:waypoints];
    
    return [[RouteStep alloc] initWithStart:_start withEnd:[other end] withPath:newpath];
}

- (double) getCost{
    return [_path getCost];
}

@end
