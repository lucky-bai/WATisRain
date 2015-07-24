
#ifndef WATisRain_MapView_h
#define WATisRain_MapView_h
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "Map.h"
#import "Route.h"
#import "RouteFinder.h"
#import "DirectionsView.h"

static const float MAP_WIDTH = 2048.0f;
static const float MAP_HEIGHT = 964.0f;
static const float MAP_ADJUST_X = 611.0f;
static const float MAP_ADJUST_Y = 773.0f;
static const float MAP_ADJUST_SCALING = 0.8156f;

@interface MapView : UIView

@property UIImage *image;
@property Map *map;
@property RouteFinder *routeFinder;
@property DirectionsView *directionsView;
@property UIBarButtonItem *clearBtn;

@property NSString *selectedBuilding1;
@property NSString *selectedBuilding2;
@property Route *route;

@property UIImage *defaultLocationImage;
@property UIImage *activeLocationImage;
@property UIImage *stairsUpImage;
@property UIImage *stairsDownImage;

- (MapView*)initWithImage:(UIImage*)image;
- (void)handleUserTapOnX:(float)x OnY:(float)y;

- (Building*)determineBuildingFromPositionWithX: (float)x WithY:(float)y Threshold:(float)threshold;
- (void)clearRoute;
- (void)updateRoute;
- (void)recalculateRoute;

@end

#endif
