//
// Created by chen on 2018/12/6.
//

#ifndef TP_FINAL_CONTOUR_DECORATOR_H
#define TP_FINAL_CONTOUR_DECORATOR_H

#include "decorator.h"
#include "group.h"
using namespace std;

class Contour_Decorator : public Decorator {
private:
    string color;
    int width;
public:
    Contour_Decorator(Figure *f, string s, int i) : Decorator(f), color(s), width(i) {}

    virtual void draw() const {
        Decorator::draw();
        cout << " [decorated: line: " << color << "--3]";
    }

    static void factory(Figure *f, string s, int i) {
        f->group()->replace(f,new Contour_Decorator(f,s,i));

    }
};

#endif //TP_FINAL_CONTOUR_DECORATOR_H
