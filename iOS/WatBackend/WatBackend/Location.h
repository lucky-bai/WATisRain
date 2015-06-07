
#ifndef WatBackend_Location_h
#define WatBackend_Location_h
#import "Waypoint.h"

@interface Location : NSObject<NSCopying>

@property NSString *name;
@property Waypoint *position;
@property BOOL active;

- (Location*) initWithName:(NSString*)name withX:(int)x withY:(int)y active:(int)active;

- (NSString*) getBuildingName;
- (int) getFloorNumber;
- (BOOL) isPassive;

@end

#endif
