
#import <Foundation/Foundation.h>
#import "Waypoint.h"
#import "Util.h"

NSString *readFromPath(NSString *path){
    NSError *error;
    NSString *content = [[NSString alloc] initWithContentsOfFile:path encoding:NSUTF8StringEncoding error:&error];
    if(content == nil){
        NSLog(@"%@\n", error);
    }
    return content;
}

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSString *content = readFromPath(@"/Users/bai-personal/Documents/WATisRain/deprecated/locations.txt");
        NSLog(@"%@", content);
    }
    return 0;
}
