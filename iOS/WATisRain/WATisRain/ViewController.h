
#import <UIKit/UIKit.h>
#import "MapView.h"
#import "DirectionsView.h"

@interface ViewController : UIViewController

@property (nonatomic, strong) IBOutlet UIView *containerView;
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic, strong) IBOutlet DirectionsView *directionsView;
@property (nonatomic, strong) MapView *mapView;

@end

