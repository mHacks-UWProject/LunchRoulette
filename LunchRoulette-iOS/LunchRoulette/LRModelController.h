//
//  LRModelController.h
//  LunchRoulette
//
//  Created by Valentin Tsatskin on 2013-09-21.
//  Copyright (c) 2013 Valentin Tsatskin. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LRDataViewController;

@interface LRModelController : NSObject <UIPageViewControllerDataSource>

- (LRDataViewController *)viewControllerAtIndex:(NSUInteger)index storyboard:(UIStoryboard *)storyboard;
- (NSUInteger)indexOfViewController:(LRDataViewController *)viewController;

@end
