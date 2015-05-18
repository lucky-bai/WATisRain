
#import <Foundation/Foundation.h>
#import "Waypoint.h"

@implementation Waypoint

- initWithX:(int)xx withY:(int)yy{
    _x = xx;
    _y = yy;
    return self;
}

- (NSString *)description{
    return [NSString stringWithFormat:@"(%d,%d)" ,_x ,_y];
}

- (BOOL)isEqual:(id)other{
    return _x == [other x] && _y == [other y];
}

- (double)distanceTo:(id)other{
    double xx = _x-[other x];
    double yy = _y-[other y];
    return sqrt(xx*xx+yy*yy);
}

@end
