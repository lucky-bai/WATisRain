
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

@end

#endif
