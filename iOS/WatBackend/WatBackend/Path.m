
#import <Foundation/Foundation.h>
#import "Path.h"

@implementation Path

- (Path*) initWithLocationA:(Location *)pointA withLocationB:(Location *)pointB{
    _pointA = pointA;
    _pointB = pointB;
    return self;
}

- (NSString*) description{
    return [NSString stringWithFormat:@"Path: A=%@; B=%@", _pointA, _pointB];
}

@end
