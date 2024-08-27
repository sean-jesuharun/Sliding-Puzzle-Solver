public class Point {

    private int X_coordinate;
    private int Y_coordinate;
    private String Direction;

    public Point(int x, int Y_coordinate) {
        this.X_coordinate = x;
        this.Y_coordinate = Y_coordinate;
    }

    public Point(int x, int Y_coordinate, String Direction) {
        this.X_coordinate = x;
        this.Y_coordinate = Y_coordinate;
        this.Direction = Direction;
    }

    public int getX_coordinate() {
        return X_coordinate;
    }

    public int getY_coordinate() {
        return Y_coordinate;
    }

    public String getDirection(){
        return Direction;
    }

    @Override
    public String toString() {

        if(this.Direction == null){
            return "Start at" +
                    " ( " + (X_coordinate + 1) +
                    ", " + (Y_coordinate + 1) +" ) "
                    ;
        }

        return "Move " + Direction + " To" +
                " ( " + (X_coordinate + 1) +
                ", " + (Y_coordinate + 1) + " ) "
                ;
    }
}
