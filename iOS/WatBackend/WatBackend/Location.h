
#ifndef WatBackend_Location_h
#define WatBackend_Location_h
#import "Waypoint.h"

@interface Location : NSObject

@property NSString *name;
@property Waypoint *position;
@property BOOL active;

@end

#endif
