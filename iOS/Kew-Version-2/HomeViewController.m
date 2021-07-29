//
//  HomeViewController.m
//  Kew-Version-2
//
//  Created by remedy on 2015-07-22.
//
//

#import "HomeViewController.h"
#import "HomeCollectionViewCell.h"
#import "HomeSecondCollectionViewCell.h"
#import "HomeCollectionViewCell.h"
#import "D3Get.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "UIColor+DTColor.h"
#import "MaskEffect.h"
#import "HomeDetailViewController.h"

@interface HomeViewController () <UICollectionViewDataSource,UICollectionViewDelegate> {
    
}

// The string for the view
@property (nonatomic, strong) NSString* viewKey;

// Three common arrays for a view

@property (nonatomic, strong) NSArray* viewArr;
@property (nonatomic, strong) NSArray* viewCatsArr;

@property (strong, nonatomic) IBOutlet UIView *navBarBackView;
@property (strong, nonatomic) IBOutlet UICollectionView *homeCollectionView;

@end



@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.viewKey = @"-Jubkn8WvoI_s-HmG0R-";
    [self getViewCats];
     [self setUpNotifications];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

}

-(void)reloadContent{
    self.viewKey = @"-Jubkn8WvoI_s-HmG0R-";
    [self getView];
}

-(void)setUpNotifications{
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didApplyRule:) name:D3DidApplyRule object:nil];
   
    
}

-(void)didApplyRule:(NSNotification *)notification{

    //[self getView];
    [self getViewCats];
    
}

-(void)getView{
    [D3Get getViewWithViewKey:self.viewKey completion:^(NSDictionary *viewDict){
        // to avoid crash when internet is down
        if ( ![viewDict isKindOfClass:[NSDictionary class]] ) {
            [[[UIAlertView alloc] initWithTitle:@"Alert" message:@"Data not loaded, please check your Internet connection" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
            return;
        }
        // self.homeTabNavBarLabel.text = [viewDict valueForKey:@"viewTitle"];
    }];
    
}

#pragma mark - designs and view

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    CGFloat offset=scrollView.contentOffset.y+15;
    CGFloat percentage=offset/150;
    CGFloat alphaValue = fabs(percentage);
    [_navBarBackView setBackgroundColor:[[UIColor kewGreenTintColor] colorWithAlphaComponent:alphaValue]];
    
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return UIStatusBarStyleLightContent;
}


#pragma mark - GET VIEW AND VIEW CATS


-(void)getViewCats{
    
    
    [D3Get getViewCatsArrayWithViewKey:self.viewKey completion:^(NSArray *catsArr) {
        self.viewCatsArr = catsArr;
        //NSLog(@"Array in view %@", self.viewCatsArr);
        [self.homeCollectionView reloadData];
    }];
    
}

#pragma mark - Collection View Delegate

// Note that we use items to determine the size of the collectionView, based on it being a uniform collection

-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    
    return [self.viewCatsArr count];
 
}

-(UICollectionViewCell*)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{

    UICollectionViewCell *cell;

    if (cell == nil) {
    }
    
    if (indexPath.item == 0){
        static NSString *CellIdentifier = @"homecell";
        HomeCollectionViewCell *homecell = (HomeCollectionViewCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
        NSString *imageString = [[self.viewCatsArr objectAtIndex:indexPath.item] objectForKey:@"viewCatImage"];
        [homecell.viewcatimage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
        homecell.viewcatname.text = [[self.viewCatsArr objectAtIndex:indexPath.item] objectForKey:@"viewCatName"];
        homecell.viewcattagline.text = [[self.viewCatsArr objectAtIndex:indexPath.item] objectForKey:@"viewCatTagline"];
        cell = homecell;
    }else{
        static NSString *CellIdentifier = @"secondhomecell";
        HomeSecondCollectionViewCell *secondhomecell = (HomeSecondCollectionViewCell*)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
        NSString *imageString = [[self.viewCatsArr objectAtIndex:indexPath.item] objectForKey:@"viewCatImage"];
        [secondhomecell.viewcatimage sd_setImageWithURL:[NSURL URLWithString:imageString] placeholderImage:[UIImage imageNamed:@"placeholder"] options:SDWebImageRefreshCached];
        secondhomecell.viewcatname.text = [[self.viewCatsArr objectAtIndex:indexPath.item] objectForKey:@"viewCatName"];
        secondhomecell.viewcattagline.text = [[self.viewCatsArr objectAtIndex:indexPath.item] objectForKey:@"viewCatTagline"];
        cell = secondhomecell;
        
    }
    return cell;
    
}

#pragma mark - Segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"homeToDetail"]) {
        NSIndexPath *indexPath = [[self.homeCollectionView indexPathsForSelectedItems] lastObject];
        HomeDetailViewController *destViewController = segue.destinationViewController;
        
        NSDictionary * viewcat = [self.viewCatsArr objectAtIndex:indexPath.item];
        destViewController.strViewCatName = viewcat[@"viewCatName"];
        destViewController.strViewCatImage = viewcat[@"viewCatImage"];
        destViewController.strViewCatText = viewcat[@"viewCatText"];
        destViewController.strViewCatID = viewcat[@"key"];
        
    }
}


// Unwind segue method needed by storyboard. The custom animation doesn't seem to currently trigger

- (IBAction)unwindFromViewController:(UIStoryboardSegue *)sender {
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    CGSize size;
    if (indexPath.item == 0) {
        size.height = 320;
        size.width = 320;
    } else if (indexPath.item == 1) {
        size.height = 160;
        size.width = 320;
    }else{
        size.height = 160;
        size.width = 320;
    }
    return size;
}


@end
     
