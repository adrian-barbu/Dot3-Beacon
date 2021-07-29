//
//  TabBarController.m
//  Kew-Version-2
//
//  Created by remedy on 2015-09-02.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import "TabBarController.h"
#import "UIColor+DTColor.h"
#import "DiscoverViewController.h"
#import "HomeViewController.h"
#import "PlacesViewController.h"
#import "MapViewController.h"

typedef NS_ENUM(NSInteger, eTab) {
    eTabHome = 0,
    eTabPlaces,
    eTabDiscover,
    eTabMap,
    eTabCount
};


@implementation TabBarController

- (void)viewDidLoad {
    [self setupTabBarTint];
}

- (void)setupTabBarTint {
    [[UITabBar appearance] setTintColor:[UIColor kewGreenTintColor]];
    [[UITabBar appearance] setBarTintColor:[UIColor whiteColor]];
    
}

- (void)selectTab:(eTab)tab {
    self.selectedIndex = tab;
}

/* These methods can be used to programatically call the tabs */

- (void)selectDiscoverTab {
    NSLog(@"Selected");
    
    [self selectTab:eTabDiscover];
    UINavigationController *nav = (UINavigationController*)[[self viewControllers] objectAtIndex:eTabDiscover];
    [nav popToRootViewControllerAnimated:YES];
}

- (void)selectHomeTab {
    NSLog(@"Selected Home");
    
    [self selectTab:eTabHome];
    UINavigationController *nav = (UINavigationController*)[[self viewControllers] objectAtIndex:eTabHome];
    [nav popToRootViewControllerAnimated:YES];
}

- (void)selectPlacesTab {
    NSLog(@"Selected Places");
    
    [self selectTab:eTabHome];
    UINavigationController *nav = (UINavigationController*)[[self viewControllers] objectAtIndex:eTabHome];
    [nav popToRootViewControllerAnimated:YES];
}

- (void)selectMapTab {
    NSLog(@"Selected Places");
    
    [self selectTab:eTabHome];
    UINavigationController *nav = (UINavigationController*)[[self viewControllers] objectAtIndex:eTabHome];
    [nav popToRootViewControllerAnimated:YES];
}



-(void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item{
    if (item == [[tabBar items] objectAtIndex:eTabDiscover]) {
        DiscoverViewController *discoverVc = (DiscoverViewController*)[[[[self viewControllers] objectAtIndex:eTabDiscover] viewControllers] objectAtIndex:0];
        [discoverVc reloadContent];
    }
    if (item == [[tabBar items] objectAtIndex:eTabHome]) {
        HomeViewController *homeVc = (HomeViewController*)[[[[self viewControllers] objectAtIndex:eTabHome] viewControllers] objectAtIndex:0];
        [homeVc reloadContent];
    }
    if (item == [[tabBar items] objectAtIndex:eTabPlaces]) {
        PlacesViewController *placeVc = (PlacesViewController*)[[[[self viewControllers] objectAtIndex:eTabPlaces] viewControllers] objectAtIndex:0];
        [placeVc reloadContent];
    }
    if (item == [[tabBar items] objectAtIndex:eTabMap]) {
        MapViewController *mapVc = (MapViewController*)[[[[self viewControllers] objectAtIndex:eTabMap] viewControllers] objectAtIndex:0];
        [mapVc reloadContent];
    }
    
}

@end
