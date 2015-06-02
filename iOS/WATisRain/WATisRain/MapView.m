
#import <Foundation/Foundation.h>
#import "MapView.h"

@implementation MapView

- (MapView*)initWithImage:(UIImage*)image{
    _image = image;
    self = [super initWithFrame:CGRectMake(0, 0, image.size.width, image.size.height)];
    return self;
}

- (void)drawRect:(CGRect)rect{
    CGContextRef context = UIGraphicsGetCurrentContext();
    [self.image drawAtPoint:CGPointMake(0, 0)];
    
    // Can draw stuff!
    CGContextSetStrokeColorWithColor(context, [UIColor redColor].CGColor);
    CGContextSetLineWidth(context, 3.0f);
    CGContextMoveToPoint(context, 0, 0);
    CGContextAddLineToPoint(context, self.image.size.width, self.image.size.height);
    CGContextStrokePath(context);
    CGContextMoveToPoint(context, self.image.size.width, 0);
    CGContextAddLineToPoint(context, 0, self.image.size.height);
    CGContextStrokePath(context);
}

@end