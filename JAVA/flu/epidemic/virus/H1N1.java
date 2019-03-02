package flu.epidemic.virus;

public class H1N1 extends Virus{
    public H1N1() {
        this.name = VirusType.H1N1;
        this.contagiousTime = 8;
        this.incubationTime = 7;
        this.recoverTime = 5;
        this.deadRate = 0.8;
        this.InfectionRate = 0.9;            
    }

}
