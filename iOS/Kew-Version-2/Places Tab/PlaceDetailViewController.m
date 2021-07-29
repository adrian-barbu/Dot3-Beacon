//
//  PlaceDetailViewController.m
//  Kew-Version-2
//
//  Created by Shanmin on 2015-07-30.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "PlaceDetailViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "UIColor+DTColor.h"
#import "MaskEffect.h"
#import <MapKit/MapKit.h>

@interface PlaceDetailViewController () <MKMapViewDelegate>

@property (strong, nonatomic) IBOutlet UIImageView *placeImage;
@property (strong, nonatomic) IBOutlet UILabel *placeName;
@property (strong, nonatomic) IBOutlet UITextView *placeText;
@property (strong, nonatomic) IBOutlet UIButton *infoButton;
@property (strong, nonatomic) IBOutlet UIButton *mapButton;
@property (strong, nonatomic) IBOutlet UIView *mapBackground;
@property (strong, nonatomic) IBOutlet UIView *infoBackground;
@property (nonatomic, strong) IBOutlet MKMapView *mapView;

@end

@implementation PlaceDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mapView.delegate = self;
    [self createMapCoordinates];
    self.placeName.text = self.strPlaceName;
    self.placeText.text = self.strPlaceText;
    [self.placeImage sd_setImageWithURL:[NSURL URLWithString:self.strPlaceImage] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
    self.infoButton.backgroundColor = [UIColor kewDarkBlueTintColor];
    self.mapButton.backgroundColor = [UIColor grayColor];
    [self.mapBackground setHidden:YES];
    [self.infoBackground setHidden:NO];
    
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

-(void)createMapCoordinates{
    MKPointAnnotation *myAnnotation = [[MKPointAnnotation alloc]init];
    CLLocationCoordinate2D pinCoordinate;
    
    MKCoordinateRegion region;
    pinCoordinate.latitude = [self.latitude doubleValue];
    pinCoordinate.longitude = [self.longitude doubleValue];
    myAnnotation.coordinate = pinCoordinate;
    [self.mapView addAnnotation:myAnnotation];
    
    // Set Region
    float spanX = 0.00925;
    float spanY = 0.00925;

    region.center.latitude = pinCoordinate.latitude;
    region.center.longitude = pinCoordinate.longitude;
    region.span.latitudeDelta = spanX;
    region.span.longitudeDelta = spanY;
    
    
    [self.mapView setRegion:region animated:YES];
}

- (IBAction)pressInfo:(id)sender {
    
    self.infoButton.backgroundColor = [UIColor kewDarkBlueTintColor];
    self.mapButton.backgroundColor = [UIColor grayColor];
    [self.mapBackground setHidden:YES];
    [self.infoBackground setHidden:NO];
    
    
}

- (IBAction)pressMap:(id)sender {
    
    self.infoButton.backgroundColor = [UIColor grayColor];
    self.mapButton.backgroundColor = [UIColor kewDarkBlueTintColor];
    [self.mapBackground setHidden:NO];
    [self.infoBackground setHidden:YES];
}


#pragma mark - Map methods

- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(userLocation.coordinate, 800, 800);
    [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES];
}


@end
