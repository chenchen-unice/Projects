//
//  Console_Observer.h
//  Figure
//
//  Created by maxime on 2018/12/3.
//  Copyright © 2018 maxime. All rights reserved.
//
#include <iostream>
#include <string>
#include "Observer.h"
#include "figure.h"

#ifndef Console_Observer_h
#define Console_Observer_h

class Console_Observer : public Observer{
public:
    virtual void update(Figure *fig){
        cout << *fig << endl;
    }
};


#endif /* Console_Observer_h */
