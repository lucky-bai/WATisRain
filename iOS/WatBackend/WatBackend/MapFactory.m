
#import <Foundation/Foundation.h>
#import "MapFactory.h"
#import "Map.h"
#import "Location.h"

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
    while(!scanner.atEnd){
        scanNext(scanner, &s);
        if([s isEqualTo:@";"]) break;
    }
    
    NSLog(@"1 %@ %d %d\n", name, pos_x, pos_y);
    [map addBuilding:[[Building alloc] initWithName:name position:[[Waypoint alloc] initWithX:pos_x withY:pos_y] num_floors:1 main_floor:1 zero_indexed:true selectable:true]];
}

void handleCommandPassiveLocation(Map *map, NSScanner *scanner){
    NSString *name; scanNext(scanner, &name);
    int pos_x; [scanner scanInt:&pos_x];
    int pos_y; [scanner scanInt:&pos_y];
    [map addPassiveLocation:[[Location alloc] initWithName:name withX:pos_x withY:pos_y active:false]];
    NSLog(@"2 %@ %d %d\n", name, pos_x, pos_y);
}

void handleCommandPath(Map *map, NSScanner *scanner){
    NSString *name1; scanNext(scanner, &name1);
    NSString *name2; scanNext(scanner, &name2);
    NSString *s;
    while(!scanner.atEnd){
        scanNext(scanner, &s);
        if([s isEqualTo:@";"]) break;
    }
    [map addPath:[[Path alloc] initWithLocationA:nil withLocationB:nil]]; // implement after getLocationByID
    NSLog(@"3 %@ %@\n", name1, name2);
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


