//
//  LRLobbyViewController.m
//  LunchRoulette
//
//  Created by Valentin Tsatskin on 2013-09-21.
//  Copyright (c) 2013 Valentin Tsatskin. All rights reserved.
//

#import "LRLobbyViewController.h"

double const kLRPollingInterval = 2.5;

@interface LRLobbyViewController ()

@property NSTimer *pollingTimer;
@property NSUInteger lastCount;
@property NSUInteger prelastCount;
@property UILabel *statusLabel;

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

- (id) initWithStyle:(UITableViewStyle)style className:(NSString *)aClassName {
    style = UITableViewStyleGrouped;
    self = [super initWithStyle:style className:aClassName];
    if(self) {
        self.parseClassName = aClassName;
        self.pullToRefreshEnabled = NO;
        self.tableView.scrollEnabled = NO;
        [self.navigationController setNavigationBarHidden:NO animated:YES];
        self.title = @"Finding a lunch party";


        CGFloat width = self.view.frame.size.width;
        UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, width, 50)];
        UILabel *labelView = [[UILabel alloc] initWithFrame:CGRectMake(80, 10, width, 25)];
        labelView.text = @"Your Lunch Partners";
        labelView.font = [UIFont boldSystemFontOfSize:18];
        [headerView addSubview:labelView];
        self.tableView.tableHeaderView = headerView;

        UIView *footerView = [[UIView alloc] initWithFrame:CGRectMake(150, 0, width, 50)];
        UIActivityIndicatorView *spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
        [spinner startAnimating];
        spinner.frame = CGRectMake(-150, 20, width, 50);
        [footerView addSubview:spinner];
        self.statusLabel = [[UILabel alloc] initWithFrame:CGRectMake(-65, 0, width, 25)];
        self.statusLabel.text = @"0/3 Partners Found";
        [footerView addSubview:self.statusLabel];
        self.tableView.tableFooterView = footerView;
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

        if(objects.count > 0) {
            PFObject *group = objects.firstObject;
            NSArray *userIds = group[@"users"];
            if(userIds.count > 0) {
                self.lastCount = userIds.count;
                self.statusLabel.text = [NSString stringWithFormat:@"%i/3 Partners Found", self.lastCount - 1];
                [self loadObjects];
            }
        }
    }];
}

- (PFQuery *)queryForTable {
    PFUser *user = [PFUser currentUser];
    PFQuery *query = [PFUser query];
    PFObject *group = [[PFQuery queryWithClassName:@"Group"] getFirstObject];
    [query whereKey:@"objectId" containedIn:group[@"users"]];
    [query whereKey:@"objectId" notEqualTo:user.objectId];
    return query;
}

- (UITableViewCell*) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath object:(PFObject *)object {

    static NSString *identifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if(cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }

    cell.textLabel.text = [object objectForKey:@"name"];

    NSString *picturePath = [NSString stringWithFormat:@"http://graph.facebook.com/%@/picture?type=normal",  object[@"facebookId"]];
    NSData *pictureData = [NSData dataWithContentsOfURL:[NSURL URLWithString:picturePath]];
    UIImage *picture =[UIImage imageWithData:pictureData];
    cell.imageView.image = picture;

    self.statusLabel.text = [NSString stringWithFormat:@"%i/3 Partners Found", [self.tableView numberOfRowsInSection:0]];

    return cell;
}

@end
