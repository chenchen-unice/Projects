package flu.epidemic.Gird;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import flu.epidemic.livingthings.Livingbeings;
import flu.epidemic.livingthings.Person;
import flu.epidemic.livingthings.animals.Chicken;
import flu.epidemic.livingthings.animals.Duck;
import flu.epidemic.livingthings.animals.Pig;
import flu.epidemic.state.StateType;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
public class Simulator {
    private static final double PERSON_CREATION_PROBABILITY = 0.08;
    private static final double CHICKEN_CREATION_PROBABILITY = 0.01; 
    private static final double DUCK_CREATION_PROBABILITY = 0.02; 
    private static final double PIG_CREATION_PROBABILITY = 0.03;
    public static final int DEFAULT_WIDTH = 10;
    public static final int DEFAULT_DEPTH = 10;
    private Field field;
    private List<Livingbeings> livingBeings;
    private int step;
    private List<SimulatorView> views;
    private int speed;

    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    public Simulator(int depth, int width){
        depth = DEFAULT_DEPTH;
        width = DEFAULT_WIDTH;
        livingBeings = new ArrayList<>();
        field = new Field(depth, width);
        speed = 10;
        views = new ArrayList<>();
        SimulatorView view = new GridView(depth, width);
        view.setColor(Person.class, Color.BLACK);
        view.setColor(Chicken.class, Color.BLUE);
        view.setColor(Pig.class, Color.RED);
        view.setColor(Duck.class, Color.ORANGE);
        views.add(view);

        startGraphView(view);
        reset();
    }

    private void startGraphView(SimulatorView view) {
        // creates a new thread to run the busy GUI
        view = new GraphView(500, 150, 500);
        view.setColor(Person.class, Color.BLACK);
        view.setColor(Chicken.class, Color.BLUE);
        view.setColor(Pig.class, Color.RED);
        view.setColor(Duck.class, Color.ORANGE);
        views.add(view);
    }
    public void simulate() {
        for (step = 1; views.get(0).isViable(field);step++) {
            try {
                TimeUnit.MILLISECONDS.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simulateOneStep();
        }
    }

    public boolean canContinue() {
        boolean stop = true;
        for (Livingbeings being: livingBeings) {
            if ((being.getState() != StateType.DEAD) && (being.getState() != StateType.HEALTHY)) {
                stop = false;
            }
        }
        if (stop)
            return false;
        return true;
    }

    private void updateViews() {
        for (SimulatorView view : views) {
            view.showStatus(step, field);
        }
    }

    public void reset() {
        step = 0;
        livingBeings.clear();
        for (SimulatorView view : views) {
            view.reset();
        }
        populate();
        updateViews();
    }
    
    void populate() {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++)
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= DUCK_CREATION_PROBABILITY) {
                    Location location = new Location(row,col);
                    Livingbeings duck = new Duck(field, location);
                    livingBeings.add(duck);
                }else if (rand.nextDouble() <= CHICKEN_CREATION_PROBABILITY) {
                    Location location = new Location(row,col);
                    Livingbeings chicken = new Chicken(field, location);
                    livingBeings.add(chicken);
                }else if (rand.nextDouble() <= PIG_CREATION_PROBABILITY) {
                    Location location = new Location(row,col);
                    Livingbeings pig = new Pig(field, location);
                    livingBeings.add(pig);
                }else if (rand.nextDouble() <= PERSON_CREATION_PROBABILITY) {
                    Location location = new Location(row,col);
                    Livingbeings person = new Person(field, location);
                    livingBeings.add(person);
                }              
        }
    }
    

    

        public void simulateOneStep() {
        step ++;
        List<Livingbeings> newBeings = new ArrayList<>();
            for (Iterator<Livingbeings> it = livingBeings.iterator(); it.hasNext();) {
                Livingbeings livingbeings = it.next();
                livingbeings.act(newBeings);
                if (!livingbeings.isAlive()) {
                    it.remove();
                }
            }
            livingBeings.addAll(newBeings);
            updateViews();

        }



    
    
//    void move() {
//        field.move();
//    }
    
    @Override
    public String toString() {
        return field.toString();
    }


}


