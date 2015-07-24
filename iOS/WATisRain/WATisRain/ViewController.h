
#import <UIKit/UIKit.h>
#import "MapView.h"
#import "DirectionsView.h"

@interface ViewController : UIViewController <UIScrollViewDelegate, UIActionSheetDelegate>

@property (nonatomic, strong) IBOutlet UIView *containerView;
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic, strong) IBOutlet DirectionsView *directionsView;
@property (nonatomic, strong) MapView *mapView;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *clearButton;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *settingsButton;

@end

