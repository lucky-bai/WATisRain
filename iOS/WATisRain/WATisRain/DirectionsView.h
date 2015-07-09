
#ifndef WATisRain_DirectionsView_h
#define WATisRain_DirectionsView_h
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "Route.h"

@interface DirectionsView : UILabel

- (void)generateDirectionsFromRoute: (Route*)route;

@end

#endif
