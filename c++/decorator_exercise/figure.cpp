//
//  figure.cpp
//  Figure
//
//  Created by maxime on 2018/11/22.
//  Copyright © 2018 maxime. All rights reserved.
//

#include <cassert>
#include <algorithm>
#include "figure.h"
#include "group.h"

using namespace std;

unsigned Figure::_nb_figures = 0;

list<Observer*> Figure::obs;

void Figure::print_path(ostream& os) const
{
    if (_group)
    {
        _group->print_path(os);
        os << "->";
    }
    os << _name;
}

Group *Figure::group() { return _group; }


void Figure::set_group(Group *group)
{
    if (_group) throw Figure_Already_In_Group();
    _group = group;
}

/**
 * In Design Pattern terms, this is a <em>template method</em>. We print here
 * all the properties common to all kinds of figure and we delegate the subtype
 * specific information to virtual function Figure::_display().
 */

// Display general infos then, specific info
//
ostream& operator<<(ostream& os, const Figure& fig)
{
    fig.draw();
    os << "center: " << fig._center
    << ", angle: " << fig._angle
    << ", name: " << fig.name();
    fig._display(os);
    return os;
}

void Figure::attach(Observer *o)
{
    obs.push_back(o);
    
}
