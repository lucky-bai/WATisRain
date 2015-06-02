
#ifndef WATisRain_MapView_h
#define WATisRain_MapView_h
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface MapView : UIView

@property UIImage *image;

- (MapView*)initWithImage:(UIImage*)image;

@end

#endif
