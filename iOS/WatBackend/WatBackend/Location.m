
#import <Foundation/Foundation.h>
#import "Location.h"
#import "Waypoint.h"

@implementation Location

- (Location*) initWithName:(NSString *)name withX:(int)x withY:(int)y active:(int)active{
    
    _name = name;
    _position = [[Waypoint alloc] initWithX:x withY:y];
    _active = active;
    
    return self;
}

- (NSString*) description{
    return [NSString stringWithFormat:@"%@%@", _name, _position];
}

@end
