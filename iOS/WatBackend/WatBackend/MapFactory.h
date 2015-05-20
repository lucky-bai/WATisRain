
#ifndef WatBackend_MapFactory_h
#define WatBackend_MapFactory_h
#import "Map.h"

@interface MapFactory : NSObject

+ (Map*)readMapFromPath: (NSString*) path;

@end

#endif
