
#ifndef WatBackend_Building_h
#define WatBackend_Building_h
#import "Waypoint.h"

@interface Building : NSObject

@property NSArray *floors;
@property NSArray *stairs;
@property NSString *name;
@property Waypoint *position;
@property int num_floors;
@property int main_floor;
@property BOOL selectable;

- (Building*) initWithName: (NSString*)name position:(Waypoint*)position num_floors:(int)num_floors main_floor:(int)main_floor zero_indexed:(BOOL)zero_indexed selectable:(BOOL)selectable;

@end

#endif
