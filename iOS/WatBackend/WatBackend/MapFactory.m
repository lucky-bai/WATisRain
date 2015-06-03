
#import <Foundation/Foundation.h>
#import "MapFactory.h"
#import "Map.h"
#import "Location.h"
#import "Util.h"

@implementation MapFactory

// Pretend we're java Scanner.next()
void *scanNext(NSScanner *scanner, NSString **into){
    [scanner scanUpToCharactersFromSet:[NSCharacterSet whitespaceAndNewlineCharacterSet] intoString:into];
    return 0;
}

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
    NSString *s;
    NSString *name; scanNext(scanner, &name);
    int pos_x; [scanner scanInt:&pos_x];
    int pos_y; [scanner scanInt:&pos_y];
    int num_floors = 1;
    int main_floor = 1;
    BOOL zero_indexed = false;
    BOOL selectable = true;
    while(!scanner.atEnd){
        scanNext(scanner, &s);
        if([s isEqualTo:@";"]) break;
        
        if([s isEqualTo:@"floors"]){
            [scanner scanInt:&num_floors];
        }
        if([s isEqualTo:@"main_floor"]){
            [scanner scanInt:&main_floor];
        }
        if([s isEqualTo:@"has_basement"]){
            zero_indexed = true;
        }
        if([s isEqualTo:@"unselectable"]){
            selectable = false;
        }
    }
    
    [map addBuilding:[[Building alloc] initWithName:name position:[[Waypoint alloc] initWithX:pos_x withY:pos_y] num_floors:num_floors main_floor:main_floor zero_indexed:zero_indexed selectable:selectable]];
}

void handleCommandPassiveLocation(Map *map, NSScanner *scanner){
    NSString *name; scanNext(scanner, &name);
    int pos_x; [scanner scanInt:&pos_x];
    int pos_y; [scanner scanInt:&pos_y];
    [map addPassiveLocation:[[Location alloc] initWithName:name withX:pos_x withY:pos_y active:false]];
}

void handleCommandPath(Map *map, NSScanner *scanner){
    NSString *name1; scanNext(scanner, &name1);
    NSString *name2; scanNext(scanner, &name2);
    NSString *s;
    while(!scanner.atEnd){
        scanNext(scanner, &s);
        if([s isEqualTo:@";"]) break;
        
        if([s isEqualTo:@"p"]){
            // stuff
        }
        if([s isEqualTo:@"type"]){
            // stuff
        }
        if([s isEqualTo:@"connects"]){
            // stuff
        }
    }
    [map addPath:[[Path alloc] initWithLocationA:[map getLocationByID:name1] withLocationB:[map getLocationByID:name2]]];
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


