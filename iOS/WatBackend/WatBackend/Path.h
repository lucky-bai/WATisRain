
#ifndef WatBackend_Path_h
#define WatBackend_Path_h
#import "Location.h"

static const int TYPE_OUTSIDE = 1;
static const int TYPE_STAIR = 2;
static const int TYPE_INSIDE = 3;
static const int TYPE_INDOOR_TUNNEL = 4;
static const int TYPE_UNDERGROUND_TUNNEL = 5;
static const int TYPE_BRIEFLY_OUTSIDE = 6;

@interface Path : NSObject

@property NSArray *waypoints;
@property Location *pointA;
@property Location *pointB;
@property int pathType;

- (Path*) initWithLocationA: (Location*)pointA withLocationB:(Location*)pointB;
- (NSString*) description;

@end

#endif
