
#ifndef WatBackend_Path_h
#define WatBackend_Path_h
#import "Location.h"

@interface Path : NSObject

@property NSArray *waypoints;
@property Location *pointA;
@property Location *pointB;
@property int pathType;

- (Path*) initWithLocationA: (Location*)pointA withLocationB:(Location*)pointB;

@end

#endif
