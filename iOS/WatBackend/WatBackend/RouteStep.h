
#ifndef WatBackend_RouteStep_h
#define WatBackend_RouteStep_h
#import "Location.h"
#import "Path.h"

@interface RouteStep : NSObject

@property Location *start;
@property Location *end;
@property Path *path;

- (RouteStep*) initWithStart: (Location*)start withEnd:(Location*)end withPath:(Path*)path;

@end

#endif
