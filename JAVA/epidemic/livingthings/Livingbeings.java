package flu.epidemic.livingthings;

import flu.epidemic.Gird.Location;
import flu.epidemic.Gird.Field;
import flu.epidemic.state.StateType;
import flu.epidemic.virus.Virus;

import java.util.List;

public abstract class Livingbeings {
    protected Virus virus;
    protected BeingsType being;
    protected StateType state;
    protected Field field;
    protected Location location;
    protected boolean alive;

    public Livingbeings(BeingsType being, StateType state, Field field, Location location) {
        this.virus = null;
        this.being = being;
        this.state = state;
        this.alive = true;
        this.field = field;
        setLoaction(location);
    }

    abstract public void act(List<Livingbeings> newBeing);

    public boolean isAlive() {
        return alive;
    }

    protected void setDead() {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    public Virus getVirus() {
        return virus;
    }

    public BeingsType getBeing() {
        return being;
    }

    public StateType getState() {
        return state;
    }



    protected abstract void updateTime();

    protected void setLoaction(Location newLocation){
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this,newLocation);
    }

    protected Field getField(){ return field; }

    protected Location getLocation(){ return location; }
}
