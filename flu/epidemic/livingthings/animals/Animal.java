package flu.epidemic.livingthings.animals;

import java.util.List;
import java.util.Random;

import flu.epidemic.Gird.Field;
import flu.epidemic.Gird.Location;
import flu.epidemic.livingthings.BeingsType;
import flu.epidemic.livingthings.Livingbeings;
import flu.epidemic.state.Events;
import flu.epidemic.state.StateType;

public abstract class Animal extends Livingbeings {

    private int dayContagious;
    Events currentEvents;

    public Animal(BeingsType being, Field field, Location location) {
        super(being, StateType.SICK, field, location);
        Random rand = new Random();
        if(rand.nextBoolean()) {
            this.state = StateType.SICK;
        }else {this.state = StateType.HEALTHY;}

    }

    @Override
    public void act(List<Livingbeings> newBing) {
        if (isAlive()) {
            Location newLocation = getField().freeNeighborLocation(getLocation());
            if (newLocation != null)
                setLocation(newLocation);
            if (dayContagious >= virus.getContagiousTime()) {
                setDead();
            }
        }
        updateTime();
    }

    protected  void setLocation(Location newLocation){
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this,newLocation);
    }

    @Override
    protected void updateTime() {
        if (state.isEquals(StateType.SICK)) {
            dayContagious++;
        }
    }

}
