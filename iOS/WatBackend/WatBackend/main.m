
#import <Foundation/Foundation.h>
#import "Waypoint.h"

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        Waypoint *wp1 = [[Waypoint alloc] initWithX:40 withY:20];
        Waypoint *wp2 = [[Waypoint alloc] initWithX:10 withY:20];
        Waypoint *wp3 = [[Waypoint alloc] initWithX:40 withY:20];
        NSLog(@"%@ %@ %@\n", wp1, wp2, wp3);
        NSLog(@"%d %d %d\n", [wp1 isEqual:wp1], [wp1 isEqual:wp2], [wp1 isEqual:wp3]);
        NSLog(@"%f %f\n", [wp1 distanceTo:wp2], [wp1 distanceTo:wp3]);
    }
    return 0;
}
