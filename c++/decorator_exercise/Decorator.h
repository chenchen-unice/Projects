//
// Created by chen on 2018/12/6.
//
#include "figure.cpp"
#include "figure.h"
#include <iostream>
#ifndef TP_FINAL_DECORATOR_H
#define TP_FINAL_DECORATOR_H

using namespace std;

class Decorator : public Figure{
    Figure * undecorated;
public:
    Decorator(Figure * f):undecorated(f){}
    virtual void draw() const {
        undecorated->draw();
    }

    virtual void scale(double s){
        undecorated->scale(s);
    }

    virtual void move(double dx, double dy){
        undecorated->move(dx,dy);
    }

    virtual void rotate(double a){
        undecorated->rotate(a);
    }

    virtual double surface() const{
        return undecorated->surface();
    }

    virtual void _display(std::ostream& os) const{
        undecorated->_display(os);
    }
};


#endif //TP_FINAL_DECORATOR_H
