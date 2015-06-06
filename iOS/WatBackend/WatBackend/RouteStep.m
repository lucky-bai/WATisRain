
#import <Foundation/Foundation.h>
#import "RouteStep.h"

@implementation RouteStep

- (RouteStep*) initWithStart:(Location *)start withEnd:(Location *)end withPath:(Path *)path{
    _start = start;
    _end = end;
    _path = path;
    return self;
}

@end
