//
//  Observer.h
//  Figure
//
//  Created by maxime on 2018/12/3.
//  Copyright © 2018 maxime. All rights reserved.
//

#include <iostream>

#ifndef Observer_h
#define Observer_h

using namespace std;
class Figure;

class Observer{
public:
    virtual void update(Figure *fig) = 0;
    
};



#endif /* Observer_h */
