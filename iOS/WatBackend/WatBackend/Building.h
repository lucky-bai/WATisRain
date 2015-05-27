
#ifndef WatBackend_Building_h
#define WatBackend_Building_h
#import "Waypoint.h"
#import "Location.h"

@interface Building : NSObject

@property NSMutableArray *floors;
@property NSMutableArray *stairs;
@property NSString *name;
@property Waypoint *position;
@property int num_floors;
@property int main_floor;
@property BOOL zero_indexed;
@property BOOL selectable;

- (Building*) initWithName: (NSString*)name position:(Waypoint*)position num_floors:(int)num_floors main_floor:(int)main_floor zero_indexed:(BOOL)zero_indexed selectable:(BOOL)selectable;

- (NSArray*) getAllFloors;

- (NSArray*) getAllStairs;

- (Location*) getMainFloor;

@end

#endif
