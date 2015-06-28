#import <Foundation/Foundation.h>
#import "DirectionsView.h"

@implementation DirectionsView

- (void)drawTextInRect:(CGRect)rect{
    UIEdgeInsets insets = {20, 20, -18, 20};
    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, insets)];
}

@end

