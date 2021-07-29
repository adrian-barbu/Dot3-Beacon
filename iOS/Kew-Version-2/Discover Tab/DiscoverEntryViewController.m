//
//  DiscoverEntryViewController.m
//  Kew-Version-2
//
//  Created by remedy on 2015-08-05.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "DiscoverEntryViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface DiscoverEntryViewController ()
@property (strong, nonatomic) IBOutlet UIImageView *entryImage;
@property (strong, nonatomic) IBOutlet UILabel *entryHeadline;
@property (strong, nonatomic) IBOutlet UITextView *entryText;

@end

@implementation DiscoverEntryViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.entryHeadline.text = self.strEntryName;
    self.entryText.text = self.strEntryText;
    [self.entryImage sd_setImageWithURL:[NSURL URLWithString:self.strEntryImage] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}



@end
