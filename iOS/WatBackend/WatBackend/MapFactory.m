
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
        if([s isEqual:@";"]) break;
        
        if([s isEqual:@"floors"]){
            [scanner scanInt:&num_floors];
        }
        if([s isEqual:@"main_floor"]){
            [scanner scanInt:&main_floor];
        }
        if([s isEqual:@"has_basement"]){
            zero_indexed = true;
        }
        if([s isEqual:@"unselectable"]){
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
    
    Location *roughly_loc1 = [map getLocationByID:name1];
    Location *roughly_loc2 = [map getLocationByID:name2];
    
    NSMutableArray *connect_floors1 = [[NSMutableArray alloc] init];
    NSMutableArray *connect_floors2 = [[NSMutableArray alloc] init];
    
    int pathType = TYPE_OUTSIDE;
    
    NSMutableArray *waypoints = [[NSMutableArray alloc] init];
    [waypoints addObject:[roughly_loc1 position]];
     
    NSString *s;
    while(!scanner.atEnd){
        scanNext(scanner, &s);
        if([s isEqual:@";"]) break;
        
        if([s isEqual:@"p"]){
            int wx; [scanner scanInt:&wx];
            int wy; [scanner scanInt:&wy];
            [waypoints addObject:[[Waypoint alloc] initWithX:wx withY:wy]];
        }
        if([s isEqual:@"type"]){
            NSString *type_str; scanNext(scanner, &type_str);
            if([type_str isEqual:@"inside"]){
                pathType = TYPE_INSIDE;
            }
            if([type_str isEqual:@"indoor_tunnel"]){
                pathType = TYPE_INDOOR_TUNNEL;
            }
            if([type_str isEqual:@"underground_tunnel"]){
                pathType = TYPE_UNDERGROUND_TUNNEL;
            }
            if([type_str isEqual:@"briefly_outside"]){
                pathType = TYPE_BRIEFLY_OUTSIDE;
            }
        }
        if([s isEqual:@"connects"]){
            int c1; [scanner scanInt:&c1];
            int c2; [scanner scanInt:&c2];
            [connect_floors1 addObject:[NSNumber numberWithInt:c1]];
            [connect_floors2 addObject:[NSNumber numberWithInt:c2]];
        }
    }
    [waypoints addObject:[roughly_loc2 position]];
    
    if([connect_floors1 count] == 0){
        int main_floor1 = 1;
        int main_floor2 = 1;
        Building *build1 = [map getBuildingByID:name1];
        Building *build2 = [map getBuildingByID:name2];
        if(build1 != nil) main_floor1 = build1.main_floor;
        if(build2 != nil) main_floor2 = build2.main_floor;
        [connect_floors1 addObject:[NSNumber numberWithInt:main_floor1]];
        [connect_floors2 addObject:[NSNumber numberWithInt:main_floor2]];
    }
    
    for(int i=0; i<[connect_floors1 count]; i++){
        Location *loc1 = [map getLocationByID:makeBuildingAndFloor(name1, [[connect_floors1 objectAtIndex:i] intValue])];
        Location *loc2 = [map getLocationByID:makeBuildingAndFloor(name2, [[connect_floors2 objectAtIndex:i] intValue])];
        Path *path = [[Path alloc] initWithLocationA:loc1 withLocationB:loc2];
        [path setWaypointsInfo:waypoints];
        [path setPathType:pathType];
        [map addPath:path];
    }
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


