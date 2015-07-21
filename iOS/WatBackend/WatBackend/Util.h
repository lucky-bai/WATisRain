
#ifndef WatBackend_Util_h
#define WatBackend_Util_h

NSString *makeBuildingAndFloor(NSString *location, int floor);
NSString *getBuilding(NSString *combinedID);
int getFloor(NSString *combinedID);
NSArray *findOppositeVector(double a1, double a2, double b1, double b2);

extern double GLOBAL_PATHING_WEIGHT;

#endif
