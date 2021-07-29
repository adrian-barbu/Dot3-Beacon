//
//  HomeDetailViewController.m
//  Kew-Version-2
//
//  Created by remedy on 2015-07-23.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "HomeDetailViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "UIColor+DTColor.h"
#import "MaskEffect.h"
#import "HomeDetailTextCollectionViewCell.h"
#import "HomeDetailDescriptionCell.h"
#import "D3Get.h"
#import "HomeEntryDetailViewController.h"


@interface HomeDetailViewController () <UICollectionViewDataSource,UICollectionViewDelegate> {
    
}

@property (strong, nonatomic) IBOutlet UICollectionView *homeDetailCollectionView;
@property (strong, nonatomic) IBOutlet UILabel *viewcatname;

@property (strong, nonatomic) IBOutlet UIView *collectViewBackground;
@property (nonatomic, strong) NSArray* viewCatsEntryArr;
@property CGRect adjustedSize;

@end

@implementation HomeDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.viewcatname.text = self.strViewCatName;
    [self.collectViewBackground setHidden:NO];
    [self getViewCats];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}


#pragma mark Get Entries for View Cat

-(void)getViewCats{
    
    [D3Get getEntriesArrayWithCatKey:self.strViewCatID completion:^(NSArray *catsArr) {
        self.viewCatsEntryArr = catsArr;
       [self.homeDetailCollectionView reloadData];
    }];
    
}


#pragma mark - Collection View Delegate


-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    
    return [self.viewCatsEntryArr count];
    
}

-(UICollectionViewCell*)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionViewCell *cell;
    if (cell == nil) {
    }
    if (indexPath.item == 0){
        static NSString *CellIdentifier = @"descriptionCell";
        HomeDetailDescriptionCell *homeDescription = (HomeDetailDescriptionCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
        homeDescription.viewCatText.text = self.strViewCatText;
        cell = homeDescription;
    
    }else{
        static NSString *CellIdentifier = @"entryCell";
        HomeDetailTextCollectionViewCell *homeDetailText = (HomeDetailTextCollectionViewCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
        
        homeDetailText.entryheadline.text = [[self.viewCatsEntryArr objectAtIndex:indexPath.item] objectForKey:@"entryHeadline"];
        homeDetailText.entryteaser.text = [[self.viewCatsEntryArr objectAtIndex:indexPath.item] objectForKey:@"entryTeaserText"];
        
        NSString *initialImageString = [[self.viewCatsEntryArr objectAtIndex:indexPath.item] objectForKey:@"entryInitialImage"];
        NSString *imageString = [[self.viewCatsEntryArr objectAtIndex:indexPath.item] objectForKey:@"entryImage"];
        
        if (initialImageString != nil) {
            [homeDetailText.entryimage sd_setImageWithURL:[NSURL URLWithString:initialImageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            }else{
                [homeDetailText.entryimage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
            }
    cell = homeDetailText;
    }
    return cell;
    
}

#pragma mark - Segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"entryToDetail"]) {
        NSIndexPath *indexPath = [[self.homeDetailCollectionView indexPathsForSelectedItems] lastObject];
        HomeEntryDetailViewController *destViewController = segue.destinationViewController;
        
        NSDictionary * viewcat = [self.viewCatsEntryArr objectAtIndex:indexPath.item];
        destViewController.strEntryHeadline = viewcat[@"entryHeadline"];
        destViewController.strEntryImage = viewcat[@"entryImage"];
        destViewController.strEntryText = viewcat[@"entryText"];

        
    }
}


// Unwind segue method needed by storyboard. The custom animation doesn't seem to currently trigger

- (IBAction)unwindFromViewController:(UIStoryboardSegue *)sender {
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    CGSize size;
    if (indexPath.item == 0) {
        
        // This cell is currently set to zero height. The idea was for the text box to appear in cell #1 but for the cell height to expand based on the amount of text.
        
        NSDictionary *fontAttributes = @{NSFontAttributeName : [UIFont systemFontOfSize:12]};
        CGSize textSize = [self.strViewCatText sizeWithAttributes:fontAttributes];
    //    CGFloat textWidth = textSize.width;
        CGFloat textHeight = textSize.height;
      //  size.height = textHeight+100;
        size.height = 0;
        
        size.width = 320;
    } else{
        size.height = 107;
        size.width = 320;
    }
    return size;
}


@end
