
#import <Foundation/Foundation.h>
#import "MapView.h"
#import "MapFactory.h"
#import "Building.h"

@implementation MapView

Building *determineBuildingFromPosition(Map *map, float x, float y, float threshold){
    NSMutableArray *buildings = [map buildings];
    
    Building *closest = nil;
    float closest_dist = 1e9;
    
    for(Building *building in buildings){
        if(![building selectable]) continue;
        
        float dist = [[[building getMainFloor] position]distanceTo:[[Waypoint alloc] initWithX:x withY:y]];
        if(dist < closest_dist){
            closest = building;
            closest_dist = dist;
        }
    }
    
    if(closest_dist > threshold)
        closest = nil;
    
    return closest;
}

- (MapView*)initWithImage:(UIImage*)image{
    _image = image;
    self = [super initWithFrame:CGRectMake(0, 0, image.size.width, image.size.height)];
    
    _defaultLocationImage = [UIImage imageNamed:@"default_location.png"];
    _activeLocationImage = [UIImage imageNamed:@"active_location.png"];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"locations" ofType:@"txt" inDirectory:@""];
    _map = [MapFactory readMapFromPath:path];
    
    return self;
}

- (void)handleUserTapOnX:(float)x OnY:(float)y{
    float map_x = (x / MAP_ADJUST_SCALING) + MAP_ADJUST_X;
    float map_y = (y / MAP_ADJUST_SCALING) + MAP_ADJUST_Y;
    
    Building *closestBuilding = determineBuildingFromPosition(_map, map_x, map_y, 70);
    NSLog(@"%@", [closestBuilding getMainFloor]);
}

void drawImageOnMap(CGContextRef context, UIImage *img, float x_, float y_, float w_){
    float x = (x_ - MAP_ADJUST_X) * MAP_ADJUST_SCALING;
    float y = (y_ - MAP_ADJUST_Y) * MAP_ADJUST_SCALING;
    float w = w_ * MAP_ADJUST_SCALING;
    
    [img drawInRect:CGRectMake(x-w/2.0, y-w/2.0, w, w)];
}

- (void)drawRect:(CGRect)rect{
    CGContextRef context = UIGraphicsGetCurrentContext();
    [self.image drawAtPoint:CGPointMake(0, 0)];

    for(Building *building in [_map buildings]){
        drawImageOnMap(context, _defaultLocationImage, [[[building getMainFloor] position] x], [[[building getMainFloor] position] y], 120);
    }
}

@end