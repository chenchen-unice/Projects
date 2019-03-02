package flu.epidemic.virus;

public class H5N1 extends Virus{
    public H5N1() {
        this.name = VirusType.H5N1;
        this.contagiousTime = 12;
        this.incubationTime = 7;
        this.recoverTime = 10;
        this.deadRate = 1;
        this.InfectionRate = 0.9;            
    }

}
