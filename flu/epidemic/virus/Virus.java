package flu.epidemic.virus;

public abstract class Virus {
    protected VirusType name;
    protected int incubationTime;
    protected int contagiousTime;
    protected int recoverTime;

    protected double deadRate;
    protected double InfectionRate;
    

    public int getIncubationTime() {
        return incubationTime;
    }

    public int getContagiousTime() {
        return contagiousTime;
    }

    public int getRecoverTime() {
        return recoverTime;
    }

    public double getDeadRate() {
        return deadRate;
    }

    public double getInfectionRate() {
        return InfectionRate;
    }

   
    
}
