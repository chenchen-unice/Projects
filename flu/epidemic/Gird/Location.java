package flu.epidemic.Gird;

import flu.epidemic.livingthings.Livingbeings;

public class Location {
    private int row;
    private int col;
    private boolean isEmpty;
    private Livingbeings beings;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
        isEmpty = true;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    boolean addBeings(Livingbeings being) {
       if(! isEmpty)
           return false;
       this.setBeings(being);
       return true;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
    
    void isOccupy() {
        isEmpty = false;
    }
    
    public Livingbeings getBeings() {
        return beings;
    }

    public void setBeings(Livingbeings being) {
        this.beings = being;
    }
    
    public void exchangeBeings(Location location) {
        Livingbeings beingA = location.getBeings();
        boolean flag = location.isEmpty;
        location.isEmpty = this.isEmpty;
        location.setBeings(this.beings);
        this.setBeings(beingA);
        isEmpty = flag;
        
    }

    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        if(this.isEmpty) return "[      ]";
        return "[ "+this.beings.getBeing()+" ]";
    }
 
}
