
#ifndef WatBackend_Util_h
#define WatBackend_Util_h

NSString *makeBuildingAndFloor(NSString *location, int floor);
NSString *getBuilding(NSString *combinedID);
int getFloor(NSString *combinedID);

static const int GLOBAL_PATHING_WEIGHT = 3.0;

#endif
