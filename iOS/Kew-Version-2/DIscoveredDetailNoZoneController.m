//
//  DIscoveredDetailNoZoneController.m
//  Kew-Version-2
//
//  Created by remedy on 2015-09-03.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "DIscoveredDetailNoZoneController.h"
#import <SDWebImage/UIImageView+WebCache.h>

@implementation DIscoveredDetailNoZoneController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.entryHeadline.text = self.strEntryHeadline;
    self.entryDetails.text = self.strEntryDetails;
     
    [self.entryImage sd_setImageWithURL:[NSURL URLWithString:self.strImageName] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}

@end
