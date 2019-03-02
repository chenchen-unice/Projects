package flu.epidemic.state;

import flu.epidemic.Gird.Location;
import flu.epidemic.Gird.Field;
import flu.epidemic.livingthings.BeingsType;
import flu.epidemic.livingthings.Person;
import flu.epidemic.virus.Virus;

import java.util.Random;

public class Events {
    public StateType currentState;
    public Virus currentVirus;
    private Field field;
    private Location location;
    //    private static final double INFECTED_RATE = 0.5;
    private static final double SICK_RATE = 0.5;
    private static final double RECOVER_RATE = 0.8;
    private boolean IsSick;
    private boolean IsRecover;

//    protected Field field;
//    protected Location location;

    public Events(StateType state, BeingsType being) {
        this.currentState = state;
        IsRecover = false;
        IsSick = false;
//        person = new Person(field,location);

    }


    public StateType InfectedAnalyze(Virus virus, int dayIncubationTime){//在被传染的前提下
        SickOrRecover();
        if(IsSick){
            return StateType.SICK;
        }else{
            if (dayIncubationTime >= virus.getIncubationTime()) {return StateType.CONTAGIOUS;}
            return StateType.HEALTHY;}
    }

    public StateType LIVE_OR_DEADAnalyze(Virus virus, int dayRecover){//在结束感染期的前提下
        SickOrRecover();
        if (IsRecover){
            if (dayRecover >= virus.getRecoverTime()){
                return StateType.HEALTHY;
            }
            return StateType.RECOVERING;
        }
        return StateType.DEAD;
    }

    public Virus getCurrentVirus(){
        return currentVirus;
    }

    public void SickOrRecover() {
        Random random = new Random();
        if (random.nextDouble() >= SICK_RATE){
            IsSick = true;}
        if (random.nextDouble() >= RECOVER_RATE){
            IsRecover = true;}
    }


}
