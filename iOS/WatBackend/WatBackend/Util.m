
#import <Foundation/Foundation.h>
#import "Util.h"

double GLOBAL_PATHING_WEIGHT = 3.0;

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

NSArray *findOppositeVector(double a1, double a2, double b1, double b2){
    a1 += 0.0001;
    a2 += 0.0001;
    double length_a = sqrt(a1*a1 + a2*a2);
    double length_b = sqrt(b1*b1 + b2*b2);
    a1 /= length_a;
    a2 /= length_a;
    b1 /= length_b;
    b2 /= length_b;
    double c1 = -a1-b1;
    double c2 = -a2-b2;
    double length_c = sqrt(c1*c1 + c2*c2);
    c1 /= length_c;
    c2 /= length_c;
    
    return @[[NSNumber numberWithDouble:c1], [NSNumber numberWithDouble:c2]];
}

