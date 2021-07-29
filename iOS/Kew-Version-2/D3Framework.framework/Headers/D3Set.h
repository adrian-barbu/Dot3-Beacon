//
//  D3Set.h
//  dot3Xcode
//
//  Created by Shanmin on 2015-06-30.
//  Copyright (c) 2015 Dot3. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface D3Set : NSObject

//User
+ (void)registerAnonymousUser;
+ (void)getUserIdWithCompletion:(void(^)(NSString *))completion;

@end
