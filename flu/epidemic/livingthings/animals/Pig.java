package flu.epidemic.livingthings.animals;

import flu.epidemic.Gird.Field;
import flu.epidemic.Gird.Location;
import flu.epidemic.livingthings.BeingsType;
import flu.epidemic.state.StateType;
import flu.epidemic.virus.H1N1;
import flu.epidemic.virus.H5N1;
import flu.epidemic.virus.VirusType;

public class Pig extends Animal {

    public Pig(Field field, Location location) {
        super(BeingsType.PIG, field, location);
        this.virus = new H1N1();
    }
    


}
