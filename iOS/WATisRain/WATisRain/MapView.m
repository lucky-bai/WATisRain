
#import <Foundation/Foundation.h>
#import "MapView.h"
#import "MapFactory.h"
#import "Building.h"
#import "Map.h"
#import "Util.h"

@implementation MapView

- (MapView*)initWithImage:(UIImage*)image{
    _image = image;
    self = [super initWithFrame:CGRectMake(0, 0, image.size.width, image.size.height)];
    
    _defaultLocationImage = [UIImage imageNamed:@"default_location.png"];
    _activeLocationImage = [UIImage imageNamed:@"active_location.png"];
    _stairsUpImage = [UIImage imageNamed:@"stairs_up.png"];
    _stairsDownImage = [UIImage imageNamed:@"stairs_down.png"];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"locations" ofType:@"txt" inDirectory:@""];
    _map = [MapFactory readMapFromPath:path];
    _routeFinder = [[RouteFinder alloc] initWithMap:_map];
    
    return self;
}

- (void)handleUserTapOnX:(float)x OnY:(float)y{
    _clearBtn.enabled = true;
    _clearBtn.title = @"Clear";
    
    float map_x = (x / MAP_ADJUST_SCALING) + MAP_ADJUST_X;
    float map_y = (y / MAP_ADJUST_SCALING) + MAP_ADJUST_Y;
    
    Building *closestBuilding = [self determineBuildingFromPositionWithX:map_x WithY:map_y Threshold:70];
    if(closestBuilding == nil ||
       [[closestBuilding name] isEqual:_selectedBuilding1] ||
       [[closestBuilding name] isEqual:_selectedBuilding2]){
        [self clearRoute];
    }
    else if(_selectedBuilding1 == nil){
        _selectedBuilding1 = [closestBuilding name];
        [_directionsView selectDestination:_selectedBuilding1];
    }else{
        _selectedBuilding2 = [closestBuilding name];
        [self updateRoute];
    }

}

- (void)clearRoute{
    _selectedBuilding1 = nil;
    _selectedBuilding2 = nil;
    _route = nil;
    [_directionsView unselectDestination];
    _clearBtn.enabled = false;
    _clearBtn.title = @"";
    [self setNeedsDisplay];
}

- (void)updateRoute{
    _route = [[_routeFinder findRouteFrom:[_map getBuildingByID:_selectedBuilding1] To:[_map getBuildingByID:_selectedBuilding2]] getContractedRoute];
    [_directionsView generateDirectionsFromRoute:_route];
}

- (void)recalculateRoute{
    if(_selectedBuilding1 == nil || _selectedBuilding2 == nil) return;
    [self updateRoute];
    [self setNeedsDisplay];
}

void drawImageOnMap(CGContextRef context, UIImage *img, float x_, float y_, float w_){
    float x = (x_ - MAP_ADJUST_X) * MAP_ADJUST_SCALING;
    float y = (y_ - MAP_ADJUST_Y) * MAP_ADJUST_SCALING;
    float w = w_ * MAP_ADJUST_SCALING;
    
    [img drawInRect:CGRectMake(x-w/2.0, y-w/2.0, w, w)];
}

void drawPathOnMap(CGContextRef context, Path *path){
    NSArray *wps = [path waypoints];
    
    CGContextSetStrokeColorWithColor(context, [UIColor colorWithRed:0.0f green:0x70/255.0f blue:0xcf/255.0f alpha:1.0f].CGColor);
    CGContextSetRGBFillColor(context, 0.0f, 0x70/255.0, 0xcf/255.0, 1.0f);
    CGContextSetLineWidth(context, 8);
    for(int i=0; i<[wps count]-1; i++){
        Waypoint *wp1 = [wps objectAtIndex:i];
        Waypoint *wp2 = [wps objectAtIndex:i+1];
        float xx1 = ([wp1 x] - MAP_ADJUST_X) * MAP_ADJUST_SCALING;
        float yy1 = ([wp1 y] - MAP_ADJUST_Y) * MAP_ADJUST_SCALING;
        float xx2 = ([wp2 x] - MAP_ADJUST_X) * MAP_ADJUST_SCALING;
        float yy2 = ([wp2 y] - MAP_ADJUST_Y) * MAP_ADJUST_SCALING;
        CGContextMoveToPoint(context, xx1, yy1);
        CGContextAddLineToPoint(context, xx2, yy2);
        CGContextStrokePath(context);
        
        // draw caps
        CGContextFillEllipseInRect(context, CGRectMake(xx1-4, yy1-4, 8, 8));
        CGContextFillEllipseInRect(context, CGRectMake(xx2-4, yy2-4, 8, 8));
    }

}

- (void)drawRect:(CGRect)rect{
    CGContextRef context = UIGraphicsGetCurrentContext();
    [self.image drawAtPoint:CGPointMake(0, 0)];

    // Draw all locations
    for(Building *building in [_map buildings]){
        if(![building selectable]) continue;
        drawImageOnMap(context, _defaultLocationImage, [[[building getMainFloor] position] x], [[[building getMainFloor] position] y], 120);
    }
    
    // Draw route
    if(_route != nil){
        NSArray *all_steps = [_route routeSteps];
        for(RouteStep *step in all_steps){
            drawPathOnMap(context, [step path]);
        }
    }

    
    // Draw active locations
    for(Building *building in [_map buildings]){
        if([[building name] isEqual:_selectedBuilding1] ||
           [[building name] isEqual:_selectedBuilding2]){
            drawImageOnMap(context, _activeLocationImage, [[[building getMainFloor] position] x], [[[building getMainFloor] position] y], 120);
        }
    }
    
    // Draw stairs
    if(_route != nil){
        NSMutableArray *all_steps = [_route routeSteps];
        
        NSMutableArray *throughBuildings = [[NSMutableArray alloc] init];
        RouteStep *allsteps0 = [all_steps objectAtIndex:0];
        [throughBuildings addObject:[[allsteps0 start] getBuildingName]];
        for(RouteStep *step in all_steps) {
            NSString *next_build = [[step end] getBuildingName];
            if(![throughBuildings containsObject:next_build])
                [throughBuildings addObject:next_build];
        }
        
        NSMutableArray *throughWaypointsM = [[NSMutableArray alloc] init];
        for(RouteStep *step in all_steps){
            [throughWaypointsM addObjectsFromArray:[step getWaypoints]];
        }
        // remove duplicates
        NSMutableArray *throughWaypoints = [[NSMutableArray alloc] init];
        for(Waypoint *w in throughWaypointsM){
            if(![throughWaypoints containsObject:w])
                [throughWaypoints addObject:w];
        }
        
        for(NSString *buildingName in throughBuildings){
            int ix = [throughWaypoints indexOfObject:[[_map getBuildingByID:buildingName] position]];
            
            Waypoint *wp_cur = [throughWaypoints objectAtIndex:ix];
            Waypoint *wp_before;
            if(ix==0){
                wp_before = [throughWaypoints objectAtIndex:1];
            }else{
                wp_before = [throughWaypoints objectAtIndex:ix-1];
            }
            Waypoint *wp_after;
            if(ix==[throughWaypoints count]-1){
                wp_after = [throughWaypoints objectAtIndex:[throughWaypoints count]-2];
            }else{
                wp_after = [throughWaypoints objectAtIndex:ix+1];
            }
            
            NSArray *vec_c = findOppositeVector([wp_before x]-[wp_cur x], [wp_before y]-[wp_cur y], [wp_after x]-[wp_cur x], [wp_after y]-[wp_cur y]);
            
            BOOL stairs_down = false;
            BOOL stairs_up = false;
            for(RouteStep *step in all_steps){
                if([[[step start] getBuildingName] isEqual:buildingName] && [[step path] pathType] == TYPE_STAIR){
                    if([[step start] getFloorNumber] > [[step end] getFloorNumber]){
                        stairs_down = true;
                    }else{
                        stairs_up = true;
                    }
                }
            }
            
            if(stairs_down){
                drawImageOnMap(context, _stairsDownImage, [[vec_c objectAtIndex:0] doubleValue]*25+[wp_cur x], [[vec_c objectAtIndex:1] doubleValue]*25+[wp_cur y], 35);
            }
            if(stairs_up){
                drawImageOnMap(context, _stairsUpImage, [[vec_c objectAtIndex:0] doubleValue]*25+[wp_cur x], [[vec_c objectAtIndex:1] doubleValue]*25+[wp_cur y], 35);
            }
            
        }
    }

}

- (Building*) determineBuildingFromPositionWithX:(float)x WithY:(float)y Threshold:(float)threshold{
    NSMutableArray *buildings = [_map buildings];
    
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

@end