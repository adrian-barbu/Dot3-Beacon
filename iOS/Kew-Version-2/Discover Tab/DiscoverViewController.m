//
//  DiscoverViewController.m
//  Kew-Version-2
//
//  Created by Shanmin on 2015-07-29.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "DiscoverViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "DiscoverCollectionViewCell.h"
#import "DiscoverFirstCell.h"
#import "DiscoverSecondCell.h"
#import "DiscoverEntryViewController.h"
#import "UIColor+DTColor.h"
#import "MaskEffect.h"
#import "D3Get.h"
#import "D3Services.h"
#import "DIscoveredDetailNoZoneController.h"


@interface DiscoverViewController () <UITabBarControllerDelegate>

@property (strong, nonatomic) IBOutlet UIView *navBarBackView;
@property (strong, nonatomic) IBOutlet UILabel *zoneName;

// Views out of zone
@property (strong, nonatomic) IBOutlet UIView *outOfZoneBackView;
@property (strong, nonatomic) IBOutlet UICollectionView *discoverOutOfZoneCollectionView;

// Views for In Zone
@property (strong, nonatomic) IBOutlet UICollectionView *discoverCollectionView;
@property (strong, nonatomic) IBOutlet UIView *infoBackgroundView;
@property (strong, nonatomic) IBOutlet UIView *collectionViewBackgroundView;

// Zone info

@property (strong, nonatomic) IBOutlet UIImageView *zoneImage;
@property (strong, nonatomic) IBOutlet UITextView *zoneText;

// Zones
@property (nonatomic, strong) NSString* currentZoneKey;
@property (nonatomic) BOOL isInZone;

// Dictionary of Zone
@property (nonatomic, strong) NSDictionary* zoneDiction;
@property (nonatomic, strong) NSArray* entriesForZoneArr;

// Array for entries when not in zone
@property (nonatomic, strong) NSArray* entriesForDiscoverArr;

// Buttons
@property (strong, nonatomic) IBOutlet UIView *buttonBackgroundView;
@property (strong, nonatomic) IBOutlet UIButton *infoButton;
@property (strong, nonatomic) IBOutlet UIButton *exploreButton;

@property (nonatomic, strong) NSString* viewKey;
@property (strong, nonatomic) IBOutlet UILabel *screenNameDiscover;


@end

@implementation DiscoverViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.viewKey = @"-JubktoQL9VvhrK6FcmO";
    self.isInZone = NO;
    [self setHiddenNotInZone];
    [self getView];
    [self getViewCatsForView];
    [self setUpNotifications];
    [D3Services checkBeaconState];
    self.infoButton.backgroundColor = [UIColor kewAquaTintColor];
    self.exploreButton.backgroundColor = [UIColor grayColor];

}

-(void)viewDidAppear{
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (self.isInZone == 0){
        
    CGFloat offset=scrollView.contentOffset.y+15;
    CGFloat percentage=offset/150;
    CGFloat alphaValue = fabs(percentage);
    [_navBarBackView setBackgroundColor:[[UIColor kewAquaTintColor] colorWithAlphaComponent:alphaValue]];
    }
}

-(void)setHiddenForInZone{
    
    [self.discoverOutOfZoneCollectionView setHidden:YES];
    [self.outOfZoneBackView setHidden:YES];
    [self.discoverCollectionView setHidden:NO];
    [self.collectionViewBackgroundView setHidden:NO];
    [self.infoBackgroundView setHidden:NO];
    [self.infoButton setHidden:NO];
    [self.exploreButton setHidden:NO];
    [self.buttonBackgroundView setHidden:NO];
    [self.navBarBackView setBackgroundColor:[[UIColor kewAquaTintColor]colorWithAlphaComponent:1.0]];
    
}

-(void)setHiddenNotInZone{
    [self.outOfZoneBackView setHidden:NO];
    [self.discoverOutOfZoneCollectionView setHidden:NO];
    [self.discoverCollectionView setHidden:YES];
    [self.collectionViewBackgroundView setHidden:YES];
    [self.infoBackgroundView setHidden:YES];
    [self.buttonBackgroundView setHidden:YES];
    [self.infoButton setHidden:YES];
    [self.exploreButton setHidden:YES];
    [self.navBarBackView setBackgroundColor:[[UIColor kewAquaTintColor]colorWithAlphaComponent:0.0]];
    
}

-(void)reloadContent{
    if (self.isInZone == 0){
        [self setHiddenNotInZone];
        [D3Services checkBeaconState];
        self.viewKey = @"-JubktoQL9VvhrK6FcmO";
        [self getView];
    }else{
        [D3Services checkBeaconState];
        self.viewKey = @"-JubktoQL9VvhrK6FcmO";
        [self getView];
    }
}

-(void)getView{
    self.viewKey = @"-JubktoQL9VvhrK6FcmO";
    [D3Get getViewWithViewKey:self.viewKey completion:^(NSDictionary *viewDict){
        // to avoid crash when internet is down
        if ( ![viewDict isKindOfClass:[NSDictionary class]] ) {
            [[[UIAlertView alloc] initWithTitle:@"Alert" message:@"Data not loaded, please check your internet connection" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            return;
        }
        if (self.isInZone == 0){
            self.screenNameDiscover.text = [viewDict valueForKey:@"viewTitle"];
            self.zoneName.text = [viewDict valueForKey:@"viewTagline"];
        }
    }];
}

-(void)getViewCatsForView{
    
    [D3Get getViewCatsArrayWithViewKey:self.viewKey completion:^(NSArray *catsArr) {
        self.entriesForDiscoverArr = catsArr;
        [self.discoverOutOfZoneCollectionView reloadData];
    }];
    
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}


#pragma mark - Set up LISTENERS AND CHANGES

-(void)setUpNotifications{

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didEnterZone:) name:D3DidEnterZone object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didExitRegion:) name:D3BeaconDidExitRegion object:nil];
    
    
}

- (void) didEnterZone:(NSNotification *)notification{
    
    NSLog(@"didArrive from app");
    self.isInZone = YES;
    [self setHiddenForInZone];
    NSArray *zonesKeyArr = [notification.userInfo objectForKey:@"zonesKeyArray"];
    if ( zonesKeyArr.count > 0 ) {
        //NSLog(@"Zone Key Array %@", zonesKeyArr);
        
        [self getZoneViewWithZoneKey:[zonesKeyArr objectAtIndex:0]];
        
       // NSLog(@"Zone at index %@", [zonesKeyArr objectAtIndex:0]);
    }
}

- (void) didExitRegion:(NSNotification *)notification{
    
    NSLog(@"didExit");
    self.isInZone = NO;
    [self setHiddenNotInZone];
    [self.discoverCollectionView reloadData];
    [self getView];
    [self.discoverOutOfZoneCollectionView reloadData];
    self.currentZoneKey = nil;
    
}


#pragma mark - GET ZONE AND ENTRIESFORZONE

-(void)getZoneViewWithZoneKey:(NSString *)zoneKey {
    
    [D3Get getZoneWithZoneKey:zoneKey completion:^(NSDictionary *zoneDict){
        NSLog(@"Zone Received %@", zoneDict);
        if ( zoneKey != nil && zoneDict != nil ) {
            self.currentZoneKey = zoneKey;
            self.isInZone = YES;
            self.zoneName.text = [zoneDict valueForKey:@"zoneName"];
            self.zoneText.text = [zoneDict valueForKey:@"zoneText"];
            NSString *imageString = [zoneDict valueForKey:@"zoneImage"];
            
            [self.zoneImage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            self.zoneDiction = zoneDict;
            
            [self getEntriesForZoneKey:self.currentZoneKey];
        }
    }];
    
}

-(void)getEntriesForZoneKey:(NSString *)zoneKey {
    
    [D3Get getEntriesArrayWithZoneKey:zoneKey completion:^(NSArray *entriesArr) {
        self.isInZone = YES;
        self.entriesForZoneArr = entriesArr;
        self.infoButton.backgroundColor = [UIColor kewAquaTintColor];
        self.exploreButton.backgroundColor = [UIColor grayColor];
        [self.collectionViewBackgroundView setHidden:YES];
        [self.infoBackgroundView setHidden:NO];
        
        [self.discoverCollectionView reloadData];

    }];
    
}

#pragma mark - Discover Buttons
- (IBAction)pressInfoButton:(id)sender {
    
    self.infoButton.backgroundColor = [UIColor kewAquaTintColor];
    self.exploreButton.backgroundColor = [UIColor grayColor];
    [self.collectionViewBackgroundView setHidden:YES];
    [self.infoBackgroundView setHidden:NO];
}

- (IBAction)pressExploreButton:(id)sender {
    
    self.infoButton.backgroundColor = [UIColor grayColor];
    self.exploreButton.backgroundColor = [UIColor kewAquaTintColor];
    [self.collectionViewBackgroundView setHidden:NO];
    [self.infoBackgroundView setHidden:YES];
    
}

#pragma mark - Collection View Delegate

-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    
    if ( collectionView == self.discoverCollectionView && self.isInZone == 1 ){
        
        return [self.entriesForZoneArr count];
    }
    else if (collectionView == self.discoverCollectionView && self.isInZone == 0){
        return [self.entriesForDiscoverArr count];
        
    }
    /*
    else if (collectionView == self.discoverOutOfZoneCollectionView && self.isInZone == 1){
        
        NSLog(@"Count of array: %lu", (unsigned long)[self.entriesForDiscoverArr count]);
        return [self.entriesForDiscoverArr count];
        
    }
    
     */
    else return [self.entriesForDiscoverArr count];
    
    //return [self.entriesForZoneArr count];
    
}

-(UICollectionViewCell*)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionViewCell *cell;
    if ( collectionView == self.discoverCollectionView ){
        
        UICollectionViewCell *cell;
        static NSString *CellIdentifier = @"entryCell";
        
        DiscoverCollectionViewCell *entryCell = (DiscoverCollectionViewCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
    
        entryCell.entryTitle.text = [[self.entriesForZoneArr objectAtIndex:indexPath.item] objectForKey:@"entryHeadline"];
        entryCell.entryTeaserText.text = [[self.entriesForZoneArr objectAtIndex:indexPath.item] objectForKey:@"entryTeaserText"];
        NSString *imageString = [[self.entriesForZoneArr objectAtIndex:indexPath.item] objectForKey:@"entryImage"];
        [entryCell.entryImage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];

        cell = entryCell;
        return cell;
        
    
    }else if ( collectionView == self.discoverOutOfZoneCollectionView ) {
        
        if (indexPath.item == 0){
            static NSString *CellIdentifier = @"firstCell";
            DiscoverFirstCell *firstcell = (DiscoverFirstCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
            NSString *imageString = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatImage"];
            NSString *initialImageString = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatInitialImage"];
            
            if (initialImageString != nil) {
                [firstcell.entryImage sd_setImageWithURL:[NSURL URLWithString:initialImageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            }else{
                [firstcell.entryImage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            }

            firstcell.entryHeadline.text = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatName"];
            firstcell.entryTeaserText.text = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatTeaserText"];
            firstcell.entryTagline.text = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatTagline"];
            
            cell = firstcell;
        }else{
            static NSString *CellIdentifier = @"secondCell";
            DiscoverSecondCell *secondcell = (DiscoverSecondCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
            NSString *imageString = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatImage"];
            
            NSString *initialImageString = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatInitialImage"];
            
            if (initialImageString != nil) {
                [secondcell.entryImage sd_setImageWithURL:[NSURL URLWithString:initialImageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            }else{
                [secondcell.entryImage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            }

            secondcell.viewCatTitle.text = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatName"];
            secondcell.viewCatTeaserText.text = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatTeaserText"];
            secondcell.viewCatTagline.text = [[self.entriesForDiscoverArr objectAtIndex:indexPath.item] objectForKey:@"viewCatTagline"];
            cell = secondcell;

        }
        return cell;
    }
    return cell;
    
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    CGSize size;
    if ( collectionView == self.discoverOutOfZoneCollectionView ) {
    
        if (indexPath.item == 0) {
            size.height = 311;
            size.width = 320;
        } else if (indexPath.item == 1) {
            size.height = 120;
            size.width = 320;
        }else{
            size.height = 120;
            size.width = 320;
        }
            return size;
    }
    else{
        if (indexPath.item == 0) {
            size.height = 304;
            size.width = 320;
        } else if (indexPath.item == 1) {
            size.height = 304;
            size.width = 320;
        }else{
            size.height = 304;
            size.width = 320;
        }
        return size;
        
    }
    return size;
}

#pragma mark - Segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"entryToDetail"]) {
        NSIndexPath *indexPath = [[self.discoverCollectionView indexPathsForSelectedItems] lastObject];
        DiscoverEntryViewController *destViewController = segue.destinationViewController;
        
        NSDictionary * entry = [self.entriesForZoneArr objectAtIndex:indexPath.item];
        destViewController.strEntryName = entry[@"entryHeadline"];
        destViewController.strEntryImage = entry[@"entryImage"];
        destViewController.strEntryText = entry[@"entryText"];
        
    }else if([segue.identifier isEqualToString:@"secondCellPush"]) {
        NSIndexPath *indexPath = [[self.discoverOutOfZoneCollectionView indexPathsForSelectedItems] lastObject];
        DIscoveredDetailNoZoneController *destViewController = segue.destinationViewController;
        NSDictionary * entry = [self.entriesForDiscoverArr objectAtIndex:indexPath.item];
        destViewController.strEntryHeadline = entry[@"viewCatName"];
        destViewController.strEntryDetails = entry[@"viewCatText"];
        destViewController.strImageName = entry[@"viewCatImage"];

    }
    else if([segue.identifier isEqualToString:@"firstCellPush"]) {
        NSIndexPath *indexPath = [[self.discoverOutOfZoneCollectionView indexPathsForSelectedItems] lastObject];
        DIscoveredDetailNoZoneController *destViewController = segue.destinationViewController;
        NSDictionary * entry = [self.entriesForDiscoverArr objectAtIndex:indexPath.item];
        destViewController.strEntryHeadline = entry[@"viewCatName"];
        destViewController.strEntryDetails = entry[@"viewCatText"];
        destViewController.strImageName = entry[@"viewCatImage"];
        
    }
}

// Unwind segue method needed by storyboard.

- (IBAction)unwindFromViewController:(UIStoryboardSegue *)sender {
}


@end
