//
//  MapViewController.m
//  Kew-Version-2
//
//  Created by Shanmin on 2015-07-29.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "MapViewController.h"
#import "D3Get.h"
#import "D3Services.h"
#import "UIColor+DTColor.h"

@interface MapViewController ()

// The string for the view
@property (nonatomic, strong) NSString* viewKey;
@property (strong, nonatomic) IBOutlet UILabel *mapTitle;
@property (strong, nonatomic) IBOutlet UILabel *mapTagline;
@property (strong, nonatomic) IBOutlet UIView *navBarBackground;

@end

@implementation MapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setUpNavigationBar];
    self.viewKey = @"-JyEPjxmu3p-YconNmo9";
    [self getView];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}

-(void)setUpNavigationBar{
    [self.navBarBackground setBackgroundColor:[UIColor kewBrightGreenTintColor]];
    
}

-(void)reloadContent{
    self.viewKey = @"-JyEPjxmu3p-YconNmo9";
    [self getView];
}

-(void)getView{
    [D3Get getViewWithViewKey:self.viewKey completion:^(NSDictionary *viewDict){
        // to avoid crash when internet is down
        if ( ![viewDict isKindOfClass:[NSDictionary class]] ) {
            [[[UIAlertView alloc] initWithTitle:@"Alert" message:@"Data not loaded, please check your internet connection" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            return;
        }
        self.mapTitle.text = [viewDict valueForKey:@"viewTitle"];
        self.mapTagline.text = [viewDict valueForKey:@"viewDescription"];
        
    }];
    
}



@end
