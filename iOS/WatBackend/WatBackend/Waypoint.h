
#ifndef WatBackend_Waypoint_h
#define WatBackend_Waypoint_h

@interface Waypoint : NSObject

- initWithX:(int)x withY:(int)y;
- (NSString*)description;
- (BOOL)isEqual:(id)other;
- (double)distanceTo:(id)other;

@property int x;
@property int y;

@end


#endif
