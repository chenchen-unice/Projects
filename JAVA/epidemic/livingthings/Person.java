package flu.epidemic.livingthings;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import flu.epidemic.Gird.Field;
import flu.epidemic.Gird.Location;
import flu.epidemic.state.StateType;
import flu.epidemic.state.Events;

public class Person extends Livingbeings {

    private static final double INFECTED_RATE = 0.5;
    private static final double SICK_RATE = 0.5;
    private static final double RECOVER_RATE = 0.8;
    Events currentEvents;
    private int dayIncubation;
    private int dayContagious;
    private int dayRecover;
    private boolean isInfected;
    private Location newLocation;


    public Person(Field field, Location location) {
        super(BeingsType.PERSON, StateType.HEALTHY, field, location);
        this.currentEvents = new Events(StateType.HEALTHY, BeingsType.PERSON);
        this.dayIncubation = 0;
        isInfected = false;
        this.dayContagious = 0;
        this.dayRecover = 0;


    }

    @Override
    public void act(List<Livingbeings> newBing) {
        Random random = new Random();
        if (isAlive()) {

//            Location newLocation = InfectSource();
//            if (newLocation == null) {
//                newLocation = getField().freeNeighborLocation(getLocation());
//            }
//            if (newLocation != null) {
//                if (random.nextDouble() >= INFECTED_RATE) {
//                    isInfected = true;
//                    this.virus = field.getBeingsAt(newLocation).virus;
//                    this.state = currentEvents.InfectedAnalyze(virus, dayIncubation);
//                    if (dayContagious >= virus.getContagiousTime()) {
//                        this.state = currentEvents.LIVE_OR_DEADAnalyze(virus, dayRecover);
//                    }
//
//
//                } else if (state.isEquals(StateType.DEAD)) {
//                    setDead();
//                }
//            Field field = getField();
//            List<Location> neighbour = field.getNeighbor(getLocation());
//            Iterator<Location> it = neighbour.iterator();
//            while (it.hasNext()) {
//                Location where = it.next();
//                Livingbeings livingbeings = field.getBeingsAt(where);
//                if (livingbeings != null)
            if (state.isEquals(StateType.HEALTHY)) {
                Location newLocation = getField().freeNeighborLocation(getLocation());
                if (newLocation != null)
                    setLocation(newLocation);
                resetTime();

            } else if (state.isEquals(StateType.CONTAGIOUS) || state.isEquals(StateType.SICK)) {
                if (random.nextDouble() >= INFECTED_RATE) {
                    isInfected = true;

                    this.state = currentEvents.InfectedAnalyze(virus, dayIncubation);
                    Location newLocation = getField().freeNeighborLocation(getLocation());
                    if (newLocation != null)
                        setLocation(newLocation);

                }
                if (dayContagious >= virus.getContagiousTime()) {
                    this.state = currentEvents.LIVE_OR_DEADAnalyze(virus, dayRecover);
                }
            } else if (this.state.isEquals(StateType.DEAD)) {
                setDead();
            }
        }


        updateTime();
//            if newLocation = getField().freeNeighborLocation(getLocation());
    }

    private void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this,newLocation);
    }


    private void resetTime() {
        dayContagious = 0;
        dayIncubation = 0;
        dayRecover = 0;
    }


    //
//
    private Location InfectSource(){
        Field field = getField();
        List<Location> neighbour = field.getNeighbor(getLocation());
        Iterator<Location> it = neighbour.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Livingbeings livingbeings = field.getBeingsAt(where);
            if (livingbeings != null)
                if (livingbeings.state.isEquals(StateType.CONTAGIOUS) || livingbeings.state.isEquals(StateType.SICK)){
                    return where;
                }

        }
        return  null;
    }


    @Override
    protected void updateTime() {
        if (isInfected) {
            dayIncubation++;
        }
        if (state.isEquals(StateType.CONTAGIOUS) || state.isEquals(StateType.SICK))
            dayContagious++;
        if (state.isEquals(StateType.RECOVERING))
            dayRecover++;
    }


}

