import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class SlidingPuzzle {

    //To hold the Shortest Path points from start to finish.
    private static LinkedList<Point> ShortPathOrderPoints = new LinkedList<>();

    public static void main(String[] args) {

        System.out.println();
        System.out.println("------------------");
        System.out.println("| SLIDING PUZZLE |");
        System.out.println("------------------");
        System.out.println();

        int startX_coordinate = 0;
        int startY_coordinate = 0;
        int finishX_coordinate = 0;
        int finishY_coordinate = 0;

        // File path is passed as parameter
        File file = new File("src/examples/maze10_1.txt");

        //To get the no of rows & no of columns to create the 2D array.
        int rows = 0;
        int columns = 0;

        //To hold the int arrays for each string lines.
        ArrayList<int[]> totalRowArrays = new ArrayList<>();

        String rowString;

        //For looking the invalid characters in the Puzzle.
        boolean flag = true;

        //For printing the Columns Only once.
        boolean flag1 = true;

        // Creating an object of BufferedReader class
        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            while ((rowString = br.readLine()) != null){

                columns = rowString.length();

                //For printing the Columns No only Once.
                if(flag1) {
                    System.out.printf("%-5s", " ");
                    for (int i = 1; i <= columns; i++) {
                        System.out.printf("%-4s", i);
                    }

                    System.out.println();
                    System.out.printf("%-5s", " ");
                    for (int i = 1; i <= columns; i++) {
                        System.out.printf("%-4s", "__");
                    }

                    flag1 = false;
                    System.out.println();
                }

                // Printing the rows.
                System.out.printf("%-2s", (rows + 1));
                System.out.print(" | "+ rowString.replaceAll(".(?=.)", "$0   "));
                System.out.println();

                int[] singleRowArray = new int[rowString.length()];

                for(int i = 0; i < rowString.length(); i++){

                    //checking whether the value is S.
                    if(rowString.charAt(i) == 83){
                        singleRowArray[i] = 0;
                        startX_coordinate = i;
                        startY_coordinate = rows;
                    }
                    //checking whether the value is F.
                    else if(rowString.charAt(i) == 70){
                        singleRowArray[i] = 2;
                        finishX_coordinate = i;
                        finishY_coordinate = rows;
                    }
                    //checking whether the value is "."
                    else if(rowString.charAt(i) == 46){
                        singleRowArray[i] = 0;
                    }
                    //checking whether the value is 0.
                    else if(rowString.charAt(i) == 48){
                        singleRowArray[i] = 1;
                    }
                    //Validating the Puzzle checking whether there are invalid characters
                    else {
                        System.out.println();
                        System.out.println("ERROR : Invalid Character \" "+ rowString.charAt(i) +" \" Found In " + "(" + (i+1) + ", " + (rows+1) + ")" +" Of This Puzzle.");
                        flag = false;
                        break;
                    }
                }

                //If an Invalid Character found in the Puzzle.
                if(!flag){
                    break;
                }

                totalRowArrays.add(singleRowArray);
                rows++;

            }
        }
        catch (Exception e){
            System.out.println("File Is Not Available");
        }

        //If all the characters are valid then only puzzle can be solved.
        if(flag){

            System.out.println();
            System.out.println();

            //2D array was created.
            int[][] slidingPuzzle2DArray = new int[rows][columns];

            //Assigning values to the "slidingPuzzle2DArray" 2D array according to the Algorithm.
            for(int i=0; i < rows; i++){

                int[] copySingleRowArray = totalRowArrays.get(i);

                for(int j=0; j < columns; j++){

                    slidingPuzzle2DArray[i][j] = copySingleRowArray[j];

                }
            }

            System.out.println("Starting Position Coordinates");
            System.out.println("-----------------------------");
            System.out.println("startX_coordinate is : " + (startX_coordinate + 1));
            System.out.println("startY_coordinate is : " + (startY_coordinate + 1));
            System.out.println();

            System.out.println("Finishing Position Coordinates");
            System.out.println("------------------------------");
            System.out.println("finishX_coordinate is : " + (finishX_coordinate + 1));
            System.out.println("finishY_coordinate is : " + (finishY_coordinate + 1));
            System.out.println();

            int countingStepsForShortPath = solve(slidingPuzzle2DArray, startX_coordinate, startY_coordinate, finishX_coordinate, finishY_coordinate);

            if (countingStepsForShortPath != -1){
                System.out.println("---------------------");
                System.out.println("| The Shortest Path |");
                System.out.println("---------------------");
                System.out.println();

                int index = 0;
                for(Point point : ShortPathOrderPoints){
                    System.out.println(index + ". " +point);
                    index++;
                }

                System.out.println("Done!");
                System.out.println();
                System.out.println("----------------------------");
                System.out.println("The Total No Of Steps : " + countingStepsForShortPath);
                System.out.println("----------------------------");
            }else{
                System.out.println("This Sliding Puzzle Can't be Solved");
            }
        }
    }



    private static int solve(int[][] slidingPuzzle2DArray, int startX_coordinate, int startY_coordinate, int finishX_coordinate, int finishY_coordinate) {

        Point startPoint = new Point(startX_coordinate, startY_coordinate);

        LinkedList<Point> queue = new LinkedList<>();

        //This 2D array is build to avoid visiting the same point multiple times and to hold the previous point location point.
        Point[][] trackPath2DArray = new Point[slidingPuzzle2DArray.length][slidingPuzzle2DArray[0].length];

        //add an item to the end of the list...
        queue.addLast(startPoint);

        trackPath2DArray[startY_coordinate][startX_coordinate] = startPoint;

        while (queue.size() != 0) {

            //This will retrieve and remove the first element of this list, or returns null if this list is empty.
            Point currPosition = queue.pollFirst();

            // traverse adjacent nodes while sliding on the ice.
            for (moveDirection dir : moveDirection.values()) {

                //To hold the next suitable position.
                Point nextPosition = move(slidingPuzzle2DArray, trackPath2DArray, currPosition, dir);

                if (nextPosition != null) {

                    //adding that position to the queue so, we can traverse from that point later.
                    queue.addLast(nextPosition);

                    trackPath2DArray[nextPosition.getY_coordinate()][nextPosition.getX_coordinate()] = currPosition;

                    //To find the end point.
                    if (nextPosition.getY_coordinate() == finishY_coordinate && nextPosition.getX_coordinate() == finishX_coordinate) {

                        // if we start from nextPosition we will count one too many edges so, we have minimize one from the count or start from -1.
                        Point tmp = nextPosition;

                        //To Find how many steps requires for the shortest path.
                        int count = 0;
                        while (tmp != startPoint) {

                            count++;
                            ShortPathOrderPoints.addFirst(tmp);
                            tmp = trackPath2DArray[tmp.getY_coordinate()][tmp.getX_coordinate()];

                        }

                        //adding the start point first in the liked list.
                        ShortPathOrderPoints.addFirst(startPoint);
                        return count;
                    }
                }
            }
        }
        return -1;
    }



    private static Point move(int[][] slidingPuzzle2DArray, Point[][] trackPath2DArray, Point currPosition, moveDirection dir) {
        int x = currPosition.getX_coordinate();
        int y = currPosition.getY_coordinate();

        int diffX = (dir == moveDirection.LEFT ? -1 : (dir == moveDirection.RIGHT ? 1 : 0));
        int diffY = (dir == moveDirection.UP ? -1 : (dir == moveDirection.DOWN ? 1 : 0));

        //for checking whether it reaches the "F".
        boolean flag = true;

        int i = 1;
        while (x + i * diffX >= 0
                && x + i * diffX < slidingPuzzle2DArray[0].length   //column
                && y + i * diffY >= 0
                && y + i * diffY < slidingPuzzle2DArray.length   //row
                && slidingPuzzle2DArray[y + i * diffY][x + i * diffX] != 1) {

            //Checking whether it reaches the "F".
            if(slidingPuzzle2DArray[y + i * diffY][x + i * diffX] == 2){
                flag = false;
                break;
            }else{
                i++;
            }

        }

        //If it hits the Wall or Rock.
        if(flag) {
            //If it does go back to the Previous Point (reverse the last step).
            i--;
        }


        //Checking whether we have already visited this point or not.
        if (trackPath2DArray[y + i * diffY][x + i * diffX] != null) {
            // we've already seen this point.
            return null;
        }

        String Direction = "";

        if(diffX == -1){
            Direction = "LEFT";
        }else if(diffX == 1){
            Direction = "Right";
        }else if(diffY == -1){
            Direction = "UP";
        }else if(diffY == 1){
            Direction = "DOWN";
        }

        return new Point(x + i * diffX, y + i * diffY, Direction);

    }

}
