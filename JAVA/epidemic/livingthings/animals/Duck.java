package flu.epidemic.livingthings.animals;

import flu.epidemic.Gird.Field;
import flu.epidemic.Gird.Location;
import flu.epidemic.livingthings.BeingsType;
import flu.epidemic.state.StateType;
import flu.epidemic.virus.H5N1;


public class Duck extends Animal {

    public Duck(Field field, Location location) {
        super(BeingsType.DUCK, field, location);
        this.virus = new H5N1();
    }
    
   

}
