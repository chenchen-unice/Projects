#include <algorithm>
#include <memory>
#include <iostream>
#include <fstream>
#include <typeinfo>

#include "concrete_figures.h"

#include "group.h"					// for composite
// #include "console_observer.h"		// for observer
#include "fillcolor_decorator.h"	// for fillcolor decorator
#include "contour_decorator.h"		// for contour decorator

using namespace std;


// Some figures for test
Figure *c = new Circle("circle", Point(0.0, 0.0), 2.0);
Figure *e = new Ellipse("ellipse", Point(0.0, 1.0), 2.0, 4.0);
Figure *s = new Square("square", Point(0.0, 0.0), 3.0);
Figure *r = new Rectangle("rectangle", Point(0.0, 3.0), 1.0, 3.0);

Group *g1 = new Group("g1");
Group *g2 = new Group("g2");

Figure *e2 = new Ellipse("ellipse2", Point(5.0, 2.0), 2.0, 5.0);
Figure *r2 = new Rectangle("rectangle2", Point(4.0, 3.0), 3.0, 1.0);

Group *root = new Group("root");


// TODO 3
//
// You should be able to decorate a figure with two independent (and composable) decorations: adding a fill color (class FillColor_Decorator)
// and adding a contour line of some width (class Contour_Decorator). When the corresponding decorated figure is drawn, it must list its decorations.
// You should be able to decorate an individual figure, just created, as well as a figure already member of a group of figures. In the latter case,
// you may have to modify the Group file. Decoration of a Group is not allowed.
//
int main()
{
    // Terminate construction of groups

    g1->add(s);
    g1->add(r);

    g2->add(e2);
    g2->add(r2);

    root->add(c);
    root->add(e);
    root->add(g1);
    root->add(g2);

    cout << "Main group of figures" << endl;
    cout << "---------------------" << endl;
    cout << *root << '\n' << endl;

    /*
    cout << "------------------------------------------\n"
         << "Test observers\n"
         << "------------------------------------------\n\n";
    root->attach(new Console_Observer());
    c->move(10, 10);
    e->rotate(PI/2.0);
    g2->move(5, 5);
    */

    cout << endl;
    cout << "------------------------------------------\n"
         << "Test decorators\n"
         << "------------------------------------------\n\n";
    // add a (new) figure with Single decoration
    //
    Figure *c1 = new Circle("circle1", Point(2.0, 2.0), 2.2);
    Figure *cd1 = new FillColor_Decorator(c1, "BLUE");
    g1->add(cd1);

    // add a figure with Multiple decorations
    //
    Figure *s1 = new Square("square1", Point(1.0, 2.0), 3.3);
    Figure *sd1 = new FillColor_Decorator(s1, "GREY");
    Figure *sdd1 = new Contour_Decorator(sd1, "RED", 3);
    g1->add(sdd1);

    // Decoration in place (in a group)
    // Thus, call to fix_decorator_group() inside factory() is mandatory
    //
    Contour_Decorator::factory(s, "RED", 3);

    cout << "Main group of figures after decoration" << endl;
    cout << "--------------------------------------" << endl;
    cout << *root << '\n' << endl;

    return 0;
}