//
//  LRLandingViewController.m
//  LunchRoulette
//
//  Created by Valentin Tsatskin on 2013-09-21.
//  Copyright (c) 2013 Valentin Tsatskin. All rights reserved.
//

#import "LRLandingViewController.h"
#import "LRLobbyViewController.h"

@interface LRLandingViewController ()

@end

@implementation LRLandingViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)didPressStart:(id)sender {
    PFUser *user = [PFUser currentUser];
    [user fetch];
    user[@"lookingForGroup"] = [NSNumber numberWithBool:YES];
    [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        LRLobbyViewController *lobby = [[LRLobbyViewController alloc] initWithClassName:@"_User"];
        [self.navigationController pushViewController:lobby animated:YES];
    }];
}

- (IBAction)didPressLogout:(id)sender {
    [PFUser logOut]; // Log out

    // Return to login page
    [self performSegueWithIdentifier:@"login" sender:self];
}

@end
