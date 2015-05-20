
#import <Foundation/Foundation.h>
#import "MapFactory.h"
#import "Map.h"

@implementation MapFactory

+ (Map*)readMapFromPath: (NSString*) path{
    
    Map *map = [[Map alloc] init];
    
    NSError *error;
    NSString *content = [[NSString alloc] initWithContentsOfFile:path encoding:NSUTF8StringEncoding error:&error];
    if(content == nil){
        NSLog(@"%@\n", error);
    }
    NSLog(@"%@\n", content);
    
    return map;
}

@end


