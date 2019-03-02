#ifndef _GROUP_H_
#define _GROUP_H_
//=============================================================================
// Â©Jean-Paul Rigault --- December 2009
// Email: jpr@polytech.unice.fr --- URL : http://www.polytech.unice.fr/~jpr
//-----------------------------------------------------------------------------
// La Roquette sur Siagne, France
//-----------------------------------------------------------------------------
// Computer Science Department --- Engineering School
// University of Nice Sophia Antipolis, France
//=============================================================================

/**
 * @file   Group.h
 * @author Jean-Paul Rigault
 * @date   Tue Sep 15 19:18:09 2009
 *
 * @brief  Definition of groups of figures.
 *
 */

#include <set>
#include <algorithm>
#include <functional>


#include "figure.h"


class Group : public Figure
{
private:
    set<Figure *> _children;
    
    /// Number of spaces to indent a group with for _display
    static const int _basic_prefix_length = 4;
    
public:
    
    Group(string name = "") : Figure(name) {}
    
    void add(Figure *fig)
    {
        fig->set_group(this);
        _children.insert(fig);
    }
    
    void replace(Figure *old, Figure *replacement)
    {
#ifdef CPP0X
        auto it = _children.find(old);
#else
        set<Figure *>::iterator it = _children.find(old);
#endif
        if (it == _children.end()) return;
        _children.erase(it);
        _children.insert(replacement);
    }
    
    /**
     * \brief Move a whole group.
     *
     * Simply propagate to components.
     */
    virtual void move(double dx, double dy)
    {
#ifdef CPP0X
        for (auto child : _children) child->move(dx, dy);
#else
        for (set<Figure *>::iterator it = _children.begin(); it != _children.end(); it++) (*it)->move(dx,dy);
#endif
    }
    
    /**
     * \brief Rotate a whole group
     *
     * We simply propagate rotation to components. Of course other
     * interpretations would be possible... but we want to keep things simple.
     */
    virtual void rotate(double a)
    {
#ifdef CPP0X
        for (auto child : _children)
            child->rotate(a);
#else
        for (set<Figure *>::iterator it = _children.begin(); it != _children.end(); it++)
            (*it)->rotate(a);
#endif
    }
    
    /**
     * \brief Homothetic scaling of a group
     *
     * Simply propagate to components.
     */
    virtual void scale(double s)
    {
#ifdef CPP0X
        for (auto child : _children) child->scale(s);
#else
        for (set<Figure *>::iterator it = _children.begin(); it != _children.end(); it++) (*it)->scale(s);
#endif
    }
    
    /// Surface of a group
    /**
     * We simply sum the surface of all figures contained in the group. As can
     * be seen, we use the \c std::accumulate algorithm, and a nested call to
     * \c std::bind to compose two functions: the binary \c std::plus and the
     * member-function Figure::surface(). Neat, isn't it?
     */
    virtual double surface() const
    {
        double res=0;
#ifdef CPP0X
        return std::accumulate(_children.begin(), _children.end(), 0.0,
                               [] (double acc, stdsp::shared_ptr<Figure> fig) {return acc + fig->surface();});
#else
        for (set<Figure *>::iterator it = _children.begin(); it != _children.end(); it++) res += (*it)->surface();
        return res;
#endif
    }
    
    
    virtual void draw() const
    {
        // for (set<Figure *>::iterator it = _children.begin(); it != _children.end(); it++) (*it)->draw();
        cout << "drawing a group...     ";
    }
    
    /// Group specific information display.
    virtual void _display(std::ostream& os) const
    {
        static int depth = 0;  // group nesting level
        ++depth;
        string prefix(_basic_prefix_length * depth, ' '); // current indentation
        
        os << "\n" << prefix << "*** Begin Group " << name() << " *** " << _children.size() << " figures\n";
        
        for (set<Figure *>::iterator it = _children.begin(); it != _children.end(); it++)
        {
            os << prefix;
            os << (**it);
            os << endl;
        }
        os  << prefix << "*** End Group " << name() << " ***";
        
        --depth;
    }
    
};


#endif // _GROUP_H_

