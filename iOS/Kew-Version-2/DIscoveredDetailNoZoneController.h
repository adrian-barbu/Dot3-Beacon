//
//  DIscoveredDetailNoZoneController.h
//  Kew-Version-2
//
//  Created by remedy on 2015-09-03.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DIscoveredDetailNoZoneController : UIViewController

@property (strong, nonatomic) NSString *strEntryHeadline;
@property (strong, nonatomic) NSString *strEntryDetails;
@property (strong, nonatomic) NSString *strImageName;

@property (strong, nonatomic) IBOutlet UILabel *entryHeadline;
@property (strong, nonatomic) IBOutlet UITextView *entryDetails;
@property (strong, nonatomic) IBOutlet UIImageView *entryImage;

@end
