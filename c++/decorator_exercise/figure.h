//
//  figure.h
//  Figure
//
//  Created by maxime on 2018/11/22.
//  Copyright © 2018 maxime. All rights reserved.
//

#ifndef _FIGURE_H_
#define _FIGURE_H_
const double PI = 3.141592;

#include <iostream>
#include <memory>
#include <string>
#include <list>
#include <algorithm>
#include "Observer.h"

using namespace std;


struct Point
{
    double _x;
    double _y;
    
    Point(double x = 0.0, double y = 0.0) : _x(x), _y(y) {}
    
    friend std::ostream& operator<<(std::ostream& os, const Point& pt)
    {
        return os << "(" << pt._x << ", " << pt._y << ")";
    }
};

class Figure_Already_In_Group {}; // for set_group

class Group; // forward declaration for _group

class Figure
{
private:
    static list<Observer*> obs;
    Point _center;     ///< Center of this figure
    double _angle;    ///< Angle of this figure with the horizontal axis
    
    string _name;    ///< Name of this figure
    
    static unsigned _nb_figures;
    
    /**
     * \brief Pointer to the group to which this figure belongs.
     *
     * The pointer is empty if the figure does not belong to a group.
     *
     */
    Group *_group;
    
public:
    
    void attach(Observer *o);
    Figure(string name = "") : _center(), _angle(0), _name(name),_group(0) { ++_nb_figures; }
    
    Figure(string name, Point c, double a = 0.0) : _center(c), _angle(a), _name(name),_group(0) { ++_nb_figures; }
    
    string name() const { return _name; }
    
    Point center() const { return _center; }
    void modify()
    {
        list<Observer*>::iterator it = obs.begin();
        for (; it != obs.end(); it++)
        {
            (*it)->update(this);
        }
        
    }
    virtual void move(double dx, double dy)
    {
        _center._x += dx;
        _center._y += dy;
        modify();
        
    }
    
    virtual void rotate(double alpha)
    {
        _angle += alpha;
        modify();
    }
    
    
    
    virtual void scale(double s) = 0;
    
    /// Compute the surface of a figure.
    virtual double surface() const = 0;
    
    virtual void draw() const = 0;
    
    /// Draw figure and print properties
    //
    friend ostream& operator<<(ostream& os, const Figure& fig);
    
    /// Print the path of a figure (i.e. the list of groups it belongs to).
    //
    void print_path(ostream& os = cout) const;
    
    /// Group to which this figure belongs.
    Group *group();
    
    /// Set the Group to which this figure belongs.
    void set_group(Group *group);
    
    virtual ~Figure()
    {
        --_nb_figures;
        cout << "Figure::destructor: " << _nb_figures << " remaining" << std::endl;
    }
    
    
    // not important since abstract class
    // protected:
    
    /**
     * \brief An helper function to implement #operator<<().
     *
     * This function prints the subtype specific part of the figure information.
     *
     * \see #operator<<()
     */
    virtual void _display(std::ostream& os) const = 0;
    
    static int nb_figures() { return _nb_figures; }
};

#endif // _FIGURE_H_
