package flu.epidemic.Gird;

import java.util.*;

import flu.epidemic.livingthings.Livingbeings;

public class Field {

    public int depth, width;
//    final Location[][] locations = new Location[SIZE][SIZE];
//    List<Location> emptyGird = new ArrayList<>();
    Random rand = new Random();

    private Livingbeings[][] field;

    public Field(int depth, int width){
        this.depth = depth;
        this.width = width;
        field = new Livingbeings[depth][width];
    }

    public void clear(){
        for(int row = 0; row < depth; row++){
            for(int col =0; col < width; col++){
                field[row][col] = null;
            }
        }
    }

    public void clear(Location location){
        field[location.getRow()][location.getCol()] = null;
    }

    public void place(Livingbeings beings, int row , int col){
        place(beings, new Location(row,col));
    }

    public void place(Livingbeings beings, Location location){
        field[location.getRow()][location.getCol()] = beings;
    }


    public Location randomNeighborLocation(Location location) {
        List<Location> adjacent = getNeighbor(location);
        return adjacent.get(0);
    }


    public List<Location> getFreeNeighborLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getNeighbor(location);
        for (Location next : adjacent) {
            if (getBeingsAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }


    public Location freeNeighborLocation(Location location) {
        // The available free ones.
        List<Location> free = getFreeNeighborLocations(location);
        return free.size() > 0 ? free.get(0) : null;
    }


//
//    void addBeings(Livingbeings being) {
//        if (emptyGird.isEmpty())
//            throw new RuntimeException("no space");
//        int target = rand.nextInt(emptyGird.size());
//        emptyGird.get(target).addBeings(being);
//        emptyGird.get(target).isOccupy();
//        emptyGird.remove(target);
//    }

//    void move() {
//        for (int row = 0; row < SIZE; row++)
//            for (int col = 0; col < SIZE; col++) {
//                int target = rand.nextInt(7);
//                Location[] neighbors = getNeighbor(row, col);
//                locations[row][col].exchangeBeings(neighbors[target]);
//            }
//    }


//    public Location[] getNeighbor(Location location) {
//        int row = location.getRow();
//        int col = location.getCol();
//        int DownRow = row - 1;
//        int UpRow = row + 1;
//        int LeftCol = col - 1;
//        int RightCol = col + 1;
//        if (row + 1 > SIZE - 1)
//            UpRow = 0;
//        if (row - 1 < 0)
//            DownRow = SIZE - 1;
//        if (col + 1 > SIZE - 1)
//            RightCol = 0;
//        if (col - 1 < 0)
//            LeftCol = SIZE - 1;
//        Location[] neighbor = new Location[8];
//        neighbor[0] = locations[row][RightCol];
//        neighbor[1] = locations[row][LeftCol];
//        neighbor[2] = locations[UpRow][RightCol];
//        neighbor[3] = locations[UpRow][col];
//        neighbor[4] = locations[UpRow][LeftCol];
//        neighbor[5] = locations[DownRow][LeftCol];
//        neighbor[6] = locations[DownRow][col];
//        neighbor[7] = locations[DownRow][RightCol];
//        return neighbor;
//
//    }

//    Location[] getNeighbor(int row, int col) {
//        int DownRow = row - 1;
//        int UpRow = row + 1;
//        int LeftCol = col - 1;
//        int RightCol = col + 1;
//        if (row + 1 > SIZE - 1)
//            UpRow = 0;
//        if (row - 1 < 0)
//            DownRow = SIZE - 1;
//        if (col + 1 > SIZE - 1)
//            RightCol = 0;
//        if (col - 1 < 0)
//            LeftCol = SIZE - 1;
//        Location[] neighbor = new Location[8];
//        neighbor[0] = locations[row][RightCol];
//        neighbor[1] = locations[row][LeftCol];
//        neighbor[2] = locations[UpRow][RightCol];
//        neighbor[3] = locations[UpRow][col];
//        neighbor[4] = locations[UpRow][LeftCol];
//        neighbor[5] = locations[DownRow][LeftCol];
//        neighbor[6] = locations[DownRow][col];
//        neighbor[7] = locations[DownRow][RightCol];
//        return neighbor;
//
//    }

    public List<Location> getNeighbor(Location location){
        assert location != null : "Null location passed to neighborLocations";
        List<Location> locations = new LinkedList<>();
        if(location != null){
            int row = location.getRow();
            int col = location.getCol();
            for (int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if (nextCol >= 0 && nextCol < width
                                && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            Collections.shuffle(locations,rand);
        }
        return locations;
    }

//      */
//    public Animal getAnimalAt(Location location) {
//        return getAnimalAt(location.getRow(), location.getCol());
//    }
//
//    /**
//     * Return the animal at the given location, if any.
//     *
//     * @param row
//     *            The desired row.
//     * @param col
//     *            The desired column.
//     * @return The animal at the given location, or null if there is none.
//     */
//    public Animal getAnimalAt(int row, int col) {
//        return field[row][col];
//    }

    public Livingbeings getBeingsAt(Location location){
        return getBeingsAt(location.getRow(),location.getCol());
    }

    public Livingbeings getBeingsAt(int row, int col){
        return field[row][col];
    }

    public int getDepth(){
        return depth;
    }

    public int getWidth(){
        return  width;
    }

    public void printField(){
        StringBuilder build = new StringBuilder();
        for(int i = 0; i<depth;i++){
            for(int j = 0;j<width;j++){
                build.append(field[i][j]);
            }
            build.append("\n");
        }
        System.out.println(build);

    }

}
