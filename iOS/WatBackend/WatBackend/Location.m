
#import <Foundation/Foundation.h>
#import "Location.h"
#import "Waypoint.h"
#import "Util.h"

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

- (NSString*) getBuildingName{
    return getBuilding(_name);
}

- (int) getFloorNumber{
    return getFloor(_name);
}

- (BOOL) isPassive{
    return !_active;
}

- (id) copyWithZone:(NSZone *)zone{
    Location *copy = [[[self class] alloc] init];
    
    if (copy) {
        copy.name = _name;
        copy.position = _position;
        copy.active = _active;
    }
    
    return copy;
}

- (BOOL) isEqual:(Location*)other{
    return [_name isEqualToString:[other name]];
}

- (NSUInteger) hash{
    return [_name hash];
}

@end
