//
//  LoginPageViewController.h
//  UstadMobileIOS
//
//  Created by Mike Dawson on 25/09/16.
//  Copyright © 2016 UstadMobile FZ-LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoginPageContentViewController.h"

#define LOGINPAGEVIEWCONTROLLER_INDEX_LOGIN 1
#define LOGINPAGEVIEWCONTROLLER_INDEX_REGISTER 2
#define LOGINPAGEVIEWCONTROLLER_NUMPAGES 3

@interface LoginPageViewController : UIPageViewController<UIPageViewControllerDataSource>

@end
