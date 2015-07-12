
#ifndef WATisRain_DirectionsView_h
#define WATisRain_DirectionsView_h
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "Route.h"

static const int STATE_NONE = 1;
static const int STATE_COLLAPSED = 2;
static const int STATE_LONG = 3;

@interface DirectionsView : UILabel

@property int current_state;
@property NSString *directions_long;
@property NSString *directions_collapsed;

- (void)selectDestination: (NSString*)destinationName;
- (void)unselectDestination;
- (void)generateDirectionsFromRoute: (Route*)route;

@end

#endif
