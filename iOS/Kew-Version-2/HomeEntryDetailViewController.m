//
//  HomeEntryDetailViewController.m
//  Kew-Version-2
//
//  Created by remedy on 2015-08-04.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "HomeEntryDetailViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface HomeEntryDetailViewController ()
@property (strong, nonatomic) IBOutlet UIImageView *entryImage;
@property (strong, nonatomic) IBOutlet UILabel *entryHeadline;
@property (strong, nonatomic) IBOutlet UITextView *entryText;

@end

@implementation HomeEntryDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.entryHeadline.text = self.strEntryHeadline;
    self.entryText.text = self.strEntryText;
    [self.entryImage sd_setImageWithURL:[NSURL URLWithString:self.strEntryImage] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
    
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}



@end
