//
// Created by chen on 2018/12/6.
//

#ifndef TP_FINAL_FILLCOLOR_DECORATOR_H
#define TP_FINAL_FILLCOLOR_DECORATOR_H

#include "Decorator.h"
#include <string>

using namespace std;


class FillColor_Decorator : public Decorator{
    string color;
public:
    FillColor_Decorator(Figure * f,string s): Decorator(f), color(s){}

    virtual void draw() const {
        Decorator::draw();
        cout<<" [decorated: fill: "<<color<<"]";
    }


};


#endif //TP_FINAL_FILLCOLOR_DECORATOR_H
