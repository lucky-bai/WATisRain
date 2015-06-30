#import <Foundation/Foundation.h>
#import "DirectionsView.h"

@implementation DirectionsView

- (void)awakeFromNib{
    // This is called on app startup, similar to viewDidLoad
    NSError *err = nil;
    NSString *html =
        @"<div style='font-size:11pt;font-family:sans-serif;'>"
        "I am a <b>map</b>!"
        "</div>";
    self.attributedText =
    [[NSAttributedString alloc]
        initWithData:[html dataUsingEncoding:NSUTF8StringEncoding]
        options:@{NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType}
        documentAttributes:nil
        error:&err];
    self.layer.borderWidth = 2.0f;
    self.layer.borderColor = [UIColor colorWithRed:0.6 green:0.6 blue:0.6 alpha:1].CGColor;
    
    // Tap listener
    UITapGestureRecognizer *singleTapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(directionsViewTapped:)];
    singleTapRecognizer.numberOfTapsRequired = 1;
    singleTapRecognizer.numberOfTouchesRequired = 1;
    [self addGestureRecognizer:singleTapRecognizer];
}

- (void)drawTextInRect:(CGRect)rect{
    // Add an inset (aka padding) to the box
    UIEdgeInsets insets = {20, 20, -18, 20};
    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, insets)];
}

- (void)directionsViewTapped:(UITapGestureRecognizer*)recognizer{
    NSLog(@"directions view tapped");
}

@end

