
#ifndef WATisRain_MapView_h
#define WATisRain_MapView_h
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "Map.h"

static const float MAP_WIDTH = 2048.0f;
static const float MAP_HEIGHT = 964.0f;
static const float MAP_ADJUST_X = 611.0f;
static const float MAP_ADJUST_Y = 773.0f;
static const float MAP_ADJUST_SCALING = 0.8156f;

@interface MapView : UIView

@property UIImage *image;
@property Map *map;

@property UIImage *defaultLocationImage;
@property UIImage *activeLocationImage;

- (MapView*)initWithImage:(UIImage*)image;
- (void)handleUserTapOnX:(float)x OnY:(float)y;

@end

#endif
