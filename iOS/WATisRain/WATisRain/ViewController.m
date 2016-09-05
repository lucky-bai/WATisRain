#import "ViewController.h"
#import <Foundation/Foundation.h>
#import "Util.h"

@interface ViewController ()

@end

@implementation ViewController

@synthesize scrollView = _scrollView;
@synthesize mapView = _mapView;
@synthesize directionsView = _directionsView;
@synthesize clearButton = _clearButton;
@synthesize settingsButton = _settingsButton;

// reference tutorial:
// http://www.raywenderlich.com/10518/how-to-use-uiscrollview-to-scroll-and-zoom-content
- (void)viewDidLoad{
    [super viewDidLoad];
    
    UIImage *image = [UIImage imageNamed:@"map_downsized.png"];
    self.mapView = [[MapView alloc] initWithImage:image];
    self.mapView.frame = (CGRect){.origin=CGPointMake(0.0f, 0.0f), .size=image.size};
    self.mapView.directionsView = self.directionsView;
    [self.scrollView addSubview:self.mapView];
    self.scrollView.contentSize = image.size;
    self.scrollView.delegate = self;
    
    UITapGestureRecognizer *doubleTapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(scrollViewDoubleTapped:)];
    doubleTapRecognizer.numberOfTapsRequired = 2;
    doubleTapRecognizer.numberOfTouchesRequired = 1;
    [self.scrollView addGestureRecognizer:doubleTapRecognizer];
    
    UITapGestureRecognizer *singleTapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(scrollViewSingleTapped:)];
    singleTapRecognizer.numberOfTapsRequired = 1;
    singleTapRecognizer.numberOfTouchesRequired = 1;
    [self.scrollView addGestureRecognizer:singleTapRecognizer];
    
    self.mapView.clearBtn = self.clearButton;
    [self.clearButton setEnabled:false];
    self.clearButton.title = @"";
    
    // Hide status bar
    // http://stackoverflow.com/questions/12661031/how-to-hide-a-status-bar-in-ios
    if ([self respondsToSelector:@selector(setNeedsStatusBarAppearanceUpdate)]) {
        // iOS 7
        [self performSelector:@selector(setNeedsStatusBarAppearanceUpdate)];
    } else {
        // iOS 6
        [[UIApplication sharedApplication] setStatusBarHidden:YES withAnimation:UIStatusBarAnimationSlide];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.scrollView.minimumZoomScale = 0.5;
    self.scrollView.maximumZoomScale = 2.0;
    
    [self centerScrollViewContents];
    
    // try to position map around center of campus
    float zoomConstant = 1.2;
    [self.scrollView zoomToRect:CGRectMake(1500-self.scrollView.bounds.size.width/2*zoomConstant, 450-self.scrollView.bounds.size.height/2*zoomConstant, self.scrollView.bounds.size.width*zoomConstant, self.scrollView.bounds.size.height*zoomConstant) animated:false];
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

- (void)centerScrollViewContents {
    CGSize boundsSize = self.scrollView.bounds.size;
    CGRect contentsFrame = self.mapView.frame;
    
    if (contentsFrame.size.width < boundsSize.width) {
        contentsFrame.origin.x = (boundsSize.width - contentsFrame.size.width) / 2.0f;
    } else {
        contentsFrame.origin.x = 0.0f;
    }
    
    if (contentsFrame.size.height < boundsSize.height) {
        contentsFrame.origin.y = (boundsSize.height - contentsFrame.size.height) / 2.0f;
    } else {
        contentsFrame.origin.y = 0.0f;
    }
    
    self.mapView.frame = contentsFrame;
}

- (void)scrollViewDoubleTapped:(UITapGestureRecognizer*)recognizer {
    CGPoint pointInView = [recognizer locationInView:self.mapView];
    
    CGFloat newZoomScale = self.scrollView.zoomScale * 1.5f;
    newZoomScale = MIN(newZoomScale, self.scrollView.maximumZoomScale);
    
    CGSize scrollViewSize = self.scrollView.bounds.size;
    
    CGFloat w = scrollViewSize.width / newZoomScale;
    CGFloat h = scrollViewSize.height / newZoomScale;
    CGFloat x = pointInView.x - (w / 2.0f);
    CGFloat y = pointInView.y - (h / 2.0f);
    
    CGRect rectToZoomTo = CGRectMake(x, y, w, h);
    
    [self.scrollView zoomToRect:rectToZoomTo animated:YES];
}

- (void)scrollViewTwoFingerTapped:(UITapGestureRecognizer*)recognizer {
    CGFloat newZoomScale = self.scrollView.zoomScale / 1.5f;
    newZoomScale = MAX(newZoomScale, self.scrollView.minimumZoomScale);
    [self.scrollView setZoomScale:newZoomScale animated:YES];
}

- (UIView*)viewForZoomingInScrollView:(UIScrollView *)scrollView {
    return self.mapView;
}

- (void)scrollViewDidZoom:(UIScrollView *)scrollView {
    [self centerScrollViewContents];
}

- (void)scrollViewSingleTapped:(UITapGestureRecognizer*)recognizer {
    CGPoint pointInView = [recognizer locationInView:self.mapView];
    [_mapView handleUserTapOnX:pointInView.x OnY:pointInView.y];
    [_mapView setNeedsDisplay];
}

- (IBAction)clearButtonPressed:(UIBarButtonItem*)sender{
    [_mapView clearRoute];
}

- (IBAction)settingsButtonPressed:(UIBarButtonItem*)sender{
    
    UIAlertController *settings = [UIAlertController alertControllerWithTitle:@"Prefer indoors:" message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [settings addAction:[UIAlertAction actionWithTitle:@"None" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        GLOBAL_PATHING_WEIGHT = 1.0;
        [_mapView recalculateRoute];
    }]];
    [settings addAction:[UIAlertAction actionWithTitle:@"Within reason" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        GLOBAL_PATHING_WEIGHT = 3.0;
        [_mapView recalculateRoute];
    }]];
    [settings addAction:[UIAlertAction actionWithTitle:@"At any cost" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        GLOBAL_PATHING_WEIGHT = 100.0;
        [_mapView recalculateRoute];
    }]];
    [settings addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil]];
    [settings popoverPresentationController].barButtonItem = _settingsButton;

    [self presentViewController:settings animated:true completion:nil];
}

@end
