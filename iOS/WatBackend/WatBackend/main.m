
#import <Foundation/Foundation.h>
#import "Waypoint.h"
#import "Util.h"
#import "MapFactory.h"
#import "RouteFinder.h"
#import "Map.h"
#import "Location.h"
#import "Route.h"
#import "Path.h"
#import "RouteStep.h"

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        [MapFactory readMapFromPath:@"/Users/bai-personal/Documents/WATisRain/deprecated/locations.txt"];
        
        NSMutableArray *A = [[NSMutableArray alloc] init];
        for(int i=1; i<10; i++){
            [A addObject:[NSNumber numberWithInt:i]];
            [A addObject:[NSNumber numberWithInt:-i]];
        }
        
        A = (NSMutableArray*)[A sortedArrayUsingSelector:@selector(compare:)];
        NSLog(@"%@", A);
        
    }
    return 0;
}
