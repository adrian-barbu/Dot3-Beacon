//
//  PlacesViewController.m
//  Kew-Version-2
//
//  Created by Shanmin on 2015-07-29.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "PlacesViewController.h"
#import "D3Get.h"
#import "PlacesCollectionViewCell.h"
#import "PlaceDetailViewController.h"
#import "UIColor+DTColor.h"
#import "MaskEffect.h"


@interface PlacesViewController () <UICollectionViewDataSource,UICollectionViewDelegate>

// The string for the view
@property (nonatomic, strong) NSString* viewKey;

@property (nonatomic, strong) NSArray* placesArr;
@property (nonatomic, strong) NSArray* staffPickArr;
@property (strong, nonatomic) IBOutlet UICollectionView *placesCollectionView;
@property (strong, nonatomic) IBOutlet UIView *headerBackgroundVIew;
@property (strong, nonatomic) IBOutlet UIButton *featuredButton;
@property (strong, nonatomic) IBOutlet UIButton *popularButton;
@property (strong, nonatomic) IBOutlet UILabel *screenNamePlaces;
@property (strong, nonatomic) IBOutlet UILabel *screenSubHead;
@property (strong, nonatomic) NSString *viewCatKey;


@end

@implementation PlacesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.viewKey = @"-JxBdtyYyDE8H8_N_5JZ";
    self.viewCatKey = @"-JyFtxl2X-grCXzoToxV";
    [self setUpNavigationBar];
    self.featuredButton.backgroundColor = [UIColor kewDarkBlueTintColor];
    self.popularButton.backgroundColor = [UIColor grayColor];
    [self getEntriesForViewCatFeatured];
}

- (void)didReceiveMemoryWarning {
    
    [super didReceiveMemoryWarning];

}

- (void)viewWillAppear:(BOOL)animated {
    }

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}

-(void)reloadContent{
    self.viewKey = @"-JxBdtyYyDE8H8_N_5JZ";
    [self getView];
}

-(void)getView{
    [D3Get getViewWithViewKey:self.viewKey completion:^(NSDictionary *viewDict){
        // to avoid crash when internet is down
        if ( ![viewDict isKindOfClass:[NSDictionary class]] ) {
            [[[UIAlertView alloc] initWithTitle:@"Alert" message:@"Data not loaded, please check your internet connection" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            return;
        }
        self.screenNamePlaces.text = [viewDict valueForKey:@"viewTitle"];
        self.screenSubHead.text = [viewDict valueForKey:@"viewDescription"];
        
    }];
    
}

-(void)getEntriesForViewCatFeatured{
    [D3Get getEntriesArrayWithCatKey:self.viewCatKey completion:^(NSArray *catsArr) {
        self.placesArr = catsArr;
        [self.placesCollectionView reloadData];
    }];
    
}

-(void)setUpNavigationBar{
    
    [self.headerBackgroundVIew setBackgroundColor:[UIColor kewDarkBlueTintColor]];
    [D3Get getViewWithViewKey:(NSString *)self.viewKey completion:^(NSDictionary *viewDict) {
        self.screenNamePlaces.text = [viewDict valueForKey:@"viewName"];
        self.screenSubHead.text = [viewDict valueForKey:@"viewDescription"];
    }];
}


#pragma mark Buttons

- (IBAction)pressFeatured:(id)sender {
    
    self.featuredButton.backgroundColor = [UIColor kewDarkBlueTintColor];
    self.popularButton.backgroundColor = [UIColor grayColor];
    self.viewCatKey = @"-JyFtxl2X-grCXzoToxV";
     [self getEntriesForViewCatFeatured];
    [self.placesCollectionView reloadData];
    
}

- (IBAction)pressPopular:(id)sender {
    
    self.featuredButton.backgroundColor = [UIColor grayColor];
    self.popularButton.backgroundColor = [UIColor kewDarkBlueTintColor];
    self.viewCatKey = @"-JyVBmZExoehEUZ-f5zo";
    [self getEntriesForViewCatFeatured];
    [self.placesCollectionView reloadData];
}


#pragma mark - Collection View Delegate


-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    
    return [self.placesArr count];
    
}

-(UICollectionViewCell*)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionViewCell *cell;
    
    if (cell == nil) {
    }
    
    static NSString *CellIdentifier = @"PlacesCollectionViewCell";
    PlacesCollectionViewCell *placesCollectionViewCell = (PlacesCollectionViewCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
    [placesCollectionViewCell showContent:[self.placesArr objectAtIndex:indexPath.item]];
    
    cell = placesCollectionViewCell;
    
    return cell;
    
}

#pragma mark - Segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"placeToDetail"]) {
        NSIndexPath *indexPath = [[self.placesCollectionView indexPathsForSelectedItems] lastObject];
        PlaceDetailViewController *placeDetailViewController = segue.destinationViewController;
        
        NSDictionary * placeDict = [self.placesArr objectAtIndex:indexPath.item];
        placeDetailViewController.strPlaceName = placeDict[@"entryHeadline"];
        placeDetailViewController.strPlaceImage = placeDict[@"entryImage"];
        placeDetailViewController.strPlaceText = placeDict[@"entryText"];
        
        NSArray *mapArr = [self.placesArr objectAtIndex:indexPath.item];
        NSDictionary *mapDict = [mapArr valueForKeyPath:@"mapPin"];
        NSLog(@"Map Dict: %@", mapDict);
        placeDetailViewController.latitude = mapDict[@"geoLat"];
        placeDetailViewController.longitude = mapDict[@"geoLong"];
        
    }
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    CGSize size;
    size.height = 107;
    size.width = 320;
    
    return size;
}

// Unwind segue method needed by storyboard.
- (IBAction)unwindFromViewController:(UIStoryboardSegue *)sender {
    
   
    
}

@end
