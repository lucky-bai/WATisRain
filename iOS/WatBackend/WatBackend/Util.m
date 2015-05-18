
#import <Foundation/Foundation.h>
#import "Util.h"

NSString *makeBuildingAndFloor(NSString *location, int floor){
    return [NSString stringWithFormat:@"%@:%d", location, floor];
}

NSString *getBuilding(NSString *combinedID){
    NSRange range = [combinedID rangeOfString:@":"];
    if(range.length == 0) return combinedID;
    return [combinedID substringToIndex:range.location];
}

int getFloor(NSString *combinedID){
    NSRange range = [combinedID rangeOfString:@":"];
    if(range.length == 0) return 1;
    return (int)[[combinedID substringFromIndex:range.location+1] integerValue];
}

