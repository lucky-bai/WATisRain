//
//  ViewController.m
//  WATisRain
//
//  Created by Bai-Personal on 5/12/15.
//  Copyright (c) 2015 Bai. All rights reserved.
//

#import "ViewController.h"
#import <Foundation/Foundation.h>

NSString *readFromPath(NSString *path){
    NSError *error;
    NSString *content = [[NSString alloc] initWithContentsOfFile:path encoding:NSUTF8StringEncoding error:&error];
    if(content == nil){
        NSLog(@"%@\n", error);
    }
    return content;
}

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"locations" ofType:@"txt" inDirectory:@""];
    NSString *content = readFromPath(path);
    NSLog(@"%@", content);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
