
#import <Foundation/Foundation.h>
#import "MapFactory.h"
#import "Map.h"

@implementation MapFactory

NSScanner *preprocess(NSString *str){
    NSMutableString *poststr = [[NSMutableString alloc] init];
    for(NSString *line in [str componentsSeparatedByString:@"\n"]){
        NSString *line2 = [line stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        if([line2 length] == 0) continue;
        if([line2 characterAtIndex:0] == '#') continue;
        [poststr appendString:line2];
        [poststr appendString:@"\n"];
    }
    return [NSScanner scannerWithString:poststr];
}

void handleCommandLocation(Map *map, NSScanner *scanner){
    NSLog(@"1\n");
}

void handleCommandPassiveLocation(Map *map, NSScanner *scanner){
    NSLog(@"2\n");
}

void handleCommandPath(Map *map, NSScanner *scanner){
    NSLog(@"3\n");
}

Map *readMapFromScanner(NSScanner *scanner){
    Map *map = [[Map alloc] init];
    
    while(!scanner.atEnd){
        NSString *command;
        if([scanner scanString:@"location" intoString:&command]){
            handleCommandLocation(map, scanner);
        }else if([scanner scanString:@"passive_location" intoString:&command]){
            handleCommandPassiveLocation(map, scanner);
        }else if([scanner scanString:@"path" intoString:&command]){
            handleCommandPath(map, scanner);
        }else{
            scanner.scanLocation++;
        }
    }
    
    return map;
}

+ (Map*)readMapFromPath: (NSString*) path{
    
    NSError *error;
    NSString *content = [[NSString alloc] initWithContentsOfFile:path encoding:NSUTF8StringEncoding error:&error];
    if(content == nil){
        NSLog(@"%@\n", error);
    }
    
    NSScanner *stream = preprocess(content);
    Map *map = readMapFromScanner(stream);
    
    return map;
}

@end


