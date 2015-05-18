
#import <Foundation/Foundation.h>
#import "Waypoint.h"
#import "Util.h"

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSString *r = makeBuildingAndFloor(@"M3", 17);
        //r = @"HH";
        NSLog(@"%@\n", r);
        NSString *p1 = getBuilding(r);
        int p2 = getFloor(r);
        NSLog(@"%@ %d", p1, p2);
    }
    return 0;
}
