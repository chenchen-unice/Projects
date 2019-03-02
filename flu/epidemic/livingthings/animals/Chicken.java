package flu.epidemic.livingthings.animals;

import flu.epidemic.Gird.Field;
import flu.epidemic.Gird.Location;
import flu.epidemic.livingthings.BeingsType;
import flu.epidemic.state.StateType;
import flu.epidemic.virus.H5N1;
import flu.epidemic.virus.VirusType;

public class Chicken extends Animal {

    public Chicken(Field field, Location location) {
        super(BeingsType.CHICKEN, field, location);
        this.virus = new H5N1();
    }

}
