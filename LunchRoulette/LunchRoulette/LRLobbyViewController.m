//
//  LRLobbyViewController.m
//  LunchRoulette
//
//  Created by Valentin Tsatskin on 2013-09-21.
//  Copyright (c) 2013 Valentin Tsatskin. All rights reserved.
//

#import "LRLobbyViewController.h"

double const kLRPollingInterval = 10;

@interface LRLobbyViewController ()

@property NSTimer *pollingTimer;
@property NSUInteger lastCount;

@end

@implementation LRLobbyViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id) initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if(self) {
        self.parseClassName = @"_User";
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.pollingTimer = [NSTimer scheduledTimerWithTimeInterval:kLRPollingInterval
                                                         target:self
                                                       selector:@selector(queryData:)
                                                       userInfo:nil
                                                        repeats:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) queryData:(NSTimer*) timer {
    PFUser *user = [PFUser currentUser];

    PFQuery *query = [[PFQuery alloc] initWithClassName:@"Group"];
    [query whereKey:@"users" containsAllObjectsInArray:@[ user.objectId ]];

    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (error) {
            NSLog(@"Error retrieving groups: %@", error.description);
            return;
        }

        NSLog(@"%@", objects);

        if(objects.count > 0) {
            PFObject *group = objects.firstObject;
            NSArray *userIds = group[@"users"];
            if(userIds.count > 0) {
                self.lastCount = userIds.count;
                [self loadObjects];
            }
        }
    }];
}

- (PFQuery *)queryForTable {
    PFQuery *query = [PFUser query];

    PFUser *user = [PFUser currentUser];
    PFObject *group = [[PFQuery queryWithClassName:@"Group"] getFirstObject];

    [query whereKey:@"objectId" containedIn:group[@"users"]];
    return query;
}

@end
