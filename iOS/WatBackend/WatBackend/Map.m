
#import <Foundation/Foundation.h>
#import "Map.h"

@implementation Map

- (Map*) init{
    _buildings = [[NSMutableArray alloc] init];
    _locations = [[NSMutableArray alloc] init];
    _paths = [[NSMutableArray alloc] init];
    return self;
}

- (void) addBuilding:(Building *)building{
    [self.buildings addObject:building];
    [self.locations addObjectsFromArray:[building getAllFloors]];
    [self.paths addObjectsFromArray:[building getAllStairs]];
}

- (void) addPassiveLocation:(id)location{
    [self.locations addObject:location];
}

- (void) addPath:(Path *)path{
    [self.paths addObject:path];
}

@end
