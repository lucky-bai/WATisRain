
#import <Foundation/Foundation.h>
#import "Building.h"
#import "Util.h"
#import "Location.h"
#import "Waypoint.h"
#import "Path.h"

@implementation Building

- (Building*) initWithName:(NSString *)name position:(Waypoint *)position num_floors:(int)num_floors main_floor:(int)main_floor zero_indexed:(BOOL)zero_indexed selectable:(BOOL)selectable{
    
    _name = name;
    _position = position;
    _num_floors = num_floors;
    _main_floor = main_floor;
    _zero_indexed = zero_indexed;
    _selectable = selectable;
    
    _floors = [[NSMutableArray alloc] init];
    for(int i=1; i<=num_floors; i++){
        int floor_num = i;
        if(zero_indexed) floor_num--;
        NSString *floor_id = makeBuildingAndFloor(name, floor_num);
        Location *this_floor = [[Location alloc] initWithName:floor_id withX:[position x] withY:[position y] active:true];
        [_floors addObject:this_floor];
    }
    
    _stairs = [[NSMutableArray alloc] init];
    for(int i=1; i<num_floors; i++){
        Location *upper_floor = [_floors objectAtIndex:i-1];
        Location *lower_floor = [_floors objectAtIndex:i];
        Path *this_stair = [[Path alloc] initWithLocationA:lower_floor withLocationB:upper_floor];
        this_stair.pathType = TYPE_STAIR;
        [_stairs addObject:this_stair];
    }
    
    return self;
}

- (NSArray*) getAllFloors{
    return _floors;
}

- (NSArray*) getAllStairs{
    return _stairs;
}

- (Location*) getMainFloor{
    for(Location *floor in _floors){
        if([floor getFloorNumber] == _main_floor)
            return floor;
    }
    return nil;
}

@end
