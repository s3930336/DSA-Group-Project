import java.util.ArrayList;

public class Maze {
  static final int MAX_ROW = 1000;
  static final int MAX_COL = 1000;
  int rows;
  int cols;
  String[] map;
  int robotRow;
  int robotCol;
  int steps;

  public Maze() {
    rows = 4;
    cols = 5;
    map = new String[rows];
    map[0] = ".....";
    map[1] = ".   X";
    map[2] = ".   .";
    map[3] = ".....";
    robotRow = 2;
    robotCol = 1;
    steps = 0;
  }

  /* The go() method of the maze.
   * Precondition: The method take a String that specify the direction the robot will attempt to travel to.
   * 				The only valid directions are UP, RIGHT, DOWN, LEFT.
   * Return a String containing the result of the attempt to move in the Maze:
   * 				"invalid": if the direction given is invalid
   * 				"win": if the exit has been reached
   * 				"false": if the direction the robot try to reach is a wall
   * 				"true": if the direction the robot try to reach is a space
   * All result updated the step count
   *
   */
  public String go(String direction) {
    if (!direction.equals("UP") && !direction.equals("DOWN") && !direction.equals("LEFT")
            && !direction.equals("RIGHT")) {
      // invalid direction
      steps++;
      return "invalid";
    }
    int currentRow = robotRow;
    int currentCol = robotCol;
    if (direction.equals("UP")) {
      currentRow--;
    } else if (direction.equals("DOWN")) {
      currentRow++;
    } else if (direction.equals("LEFT")) {
      currentCol--;
    } else {
      currentCol++;
    }

    // check the next position
    if (map[currentRow].charAt(currentCol) == 'X') {
      // Exit gate
      steps++;
      return "win";
    } else if (map[currentRow].charAt(currentCol) == '.') {
      // Wall
      steps++;
      return "false";
    } else {
      // Space => update robot location
      steps++;
      robotRow = currentRow;
      robotCol = currentCol;
      return "true";
    }
  }

  public static void main(String[] args) {
    (new Robot()).navigate();
  }
}

/* The Robot class that navigates the Map
 *
 */
class Robot {
  /* Private Data
   * The Robot includes an internal map that keep track of the route it has been using to navigate the maze
   * The map is a 2D integer array that will be marked and updated by the robot as it traverse the maze
   * The map is double the size of the maximum row and columns of the maze,
   * because the robot can start at any corner of the map.
   * The robot starts at the center of the map
   *
   * The map stores the data as follow:
   * 		-1: a wall
   * 		0:	not yet visited
   * 		positive integer: number of times this space has been visited
   */
  int[][] map = new int[Maze.MAX_ROW*2][Maze.MAX_COL*2];
  Coordinate robotPosition = new Coordinate(Maze.MAX_ROW, Maze.MAX_COL);

  /* Navigate method
   * The navigate method of the robot will repeatedly attempt to find the exit in the maze, one step at a time, until it finds the exit
   * If current position is marked as wall, backtrack to the previous position
   */
  public void navigate() {
    Maze maze = new Maze();
    String result = "";
    String direction = "";

    while (!result.equals("win")) {
      direction = directionToGo();
      result = maze.go(direction);
      updateMapAndPosition(result, direction);
      System.out.println(direction + ", Robot position: " + robotPosition.toString());
    }
    System.out.println("Steps to reach the Exit gate " + maze.steps);
  }

  /* directionToGo() method
   * The directionToGo() method of the robot will return a String indicating the direction the robot will move in
   * Return:
   * 		"UP", "RIGHT", "DOWN", "LEFT"
   * The directionToGo() method will prioritize which space to move as follow:
   * 		1. Position marked as wall will not be considered to be visited again
   * 		2. Space that have been visited the smallest number of time will be visited
   * 		3. If multiple places have the same number of visits, prioritize in order of UP, RIGHT, DOWN, LEFT
   *
   * TODO: Still random movement atm
   */
  private String directionToGo() {


// Direction is going to be from left to right: UP, RIGHT, BOTTOM, lEFT is : 4, 3, 2, 1


// 1. Get (X,Y coordinates of the location) & (Number of time we've went through)
// 2. Get location of from left to right : UP, RIGHT, BOTTOM, LEFT, which is from left to right: [x, y-1], [x+1, y], [x, y+1] , [x-1, y]
// 3. From (1), (2), get the location and numbers of time the location has went through into a Class, then place them in Min Heap
// 4. We then pop out the first object, only move with number that is greater or equal to zero:
    //     1. For case if there are two numbers that is equal, we then go in order from left to right: UP, RIGHT, BOTTOM, LEFT.


    ArrayList<LocationSpotted> directions = new ArrayList<LocationSpotted>();
    directions.add(new LocationSpotted(new Coordinate(robotPosition.getX(), robotPosition.getY() - 1), map[robotPosition.getX()][ robotPosition.getY() - 1], 1));
    directions.add(new LocationSpotted(new Coordinate(robotPosition.getX() + 1, robotPosition.getY()), map[robotPosition.getX() + 1][robotPosition.getY()], 2));
    directions.add(new LocationSpotted(new Coordinate(robotPosition.getX(), robotPosition.getY() + 1), map[robotPosition.getX()][robotPosition.getY() + 1], 3));
    directions.add(new LocationSpotted(new Coordinate(robotPosition.getX() - 1, robotPosition.getY()), map[robotPosition.getX() - 1][robotPosition.getY()], 4));




    int direction = calculateDirection(directions);


    if (direction == -1) {
      System.out.println("Invalid move");
    }
    if (direction <= 1) {
      return "UP";
    }
    else if (direction <= 2) {
      return "RIGHT";
    }
    else if (direction <= 3) {
      return "BOTTOM";
    }
    else {
      return "LEFT";
    }
  }

  int calculateDirection(ArrayList<LocationSpotted> directions) {
    ArrayList<TempDirectionFound> tempDirectionFounds = new ArrayList<TempDirectionFound>();
    int tempDirection = -1;

    for (int i = 0; i < directions.size(); i++) {
      int equalRefounds = 0;
      int directionNumber = directions.get(i).getDirection();
      int temp1 = directions.get(i).getPositionRefound();
      if (temp1 < 0) {
        continue;
      }
      tempDirection = directionNumber;

      for (int j = i + 1; j < directions.size(); j++) {
        int temp2 = directions.get(j).getPositionRefound();
        if (temp2 < 0) {
          continue;
        }
        if (temp1 == temp2) {
          equalRefounds++;
        }
        else if (temp1 > temp2) {
          tempDirection = directions.get(j).getDirection();
        }

      }
      if (equalRefounds > 0) {
        tempDirectionFounds.add(new TempDirectionFound(directionNumber, temp1, equalRefounds));
      }
    }
    if (tempDirectionFounds.size() == 3) {
      return tempDirectionFounds.get(0).direction;
    }
    else if (tempDirectionFounds.size() == 2) {
      if (tempDirectionFounds.get(0).reFound < tempDirectionFounds.get(1).reFound) {
        return tempDirectionFounds.get(0).direction;
      }
      else {
        return tempDirectionFounds.get(1).direction;
      }
    }
    else if (tempDirectionFounds.size() == 1) {
      if (tempDirectionFounds.get(0).direction < tempDirection) {
        return tempDirectionFounds.get(0).direction;
      }
      else {
        return tempDirection;
      }
    }
    else {
      return tempDirection;
    }
  }

class TempDirectionFound {
    int direction;
    int reFound;
    int equalRefounds;

  public TempDirectionFound(int direction, int reFound, int equalRefounds) {
    this.direction = direction;
    this.reFound = reFound;
    this.equalRefounds = equalRefounds;
  }
}

  /* updateMapAndPosition method
   * This method takes the result of the movement in the maze and the direction the robot moves in to update its internal map
   * Precondition:
   * 				result must be one of 4 String: "invalid", "win", "true", "false"
   * 				direction must be one of 4 String: "UP", "DOWN", "LEFT", "RIGHT"
   */
  private void updateMapAndPosition(String result, String direction) {
    Coordinate temp = new Coordinate(robotPosition.getX(), robotPosition.getY());
    if ((result == "win" || result == "true" || result == "false") &&
            (direction == "UP" || direction == "DOWN" || direction == "LEFT" || direction == "RIGHT")) {

      switch(direction) {
        case "UP":
          temp.up();
          break;
        case "DOWN":
          temp.down();
          break;
        case "LEFT":
          temp.left();
          break;
        case "RIGHT":
          temp.right();
          break;
        default:
      }

      if (result == "false") {
        //If the result of the movement in the maze is false, meaning the robot encounter a wall, mark it in the internal map
        map[temp.getX()][temp.getY()] = -1;
      } else {
        //If the movement is successful, add the count of visits to the current position and move the robot in its internal map
        map[robotPosition.getX()][robotPosition.getY()]++;
        robotPosition = temp;
      }
    }

    return; //input is invalid, do nothing
  }
}

// Example
class LocationSpotted {
  Coordinate location;
  int positionRefound = 0;
  int direction;

  public LocationSpotted(Coordinate coordinate, int positionRefound, int direction) {
    location = coordinate;
    this.positionRefound = positionRefound;
    this.direction = direction;
  }

  public Coordinate getLocation() {
    return location;
  }

  public int getPositionRefound() {
    return positionRefound;
  }

  public int getDirection() {
    return direction;
  }
}

class minHeap {
  int MAX_SIZE = 4;
  int size;
  LocationSpotted[] heap;


  public minHeap() {
    heap = new LocationSpotted[MAX_SIZE];
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }


  public void buildHeap(LocationSpotted[] arr) {
    size = arr.length;
    for (int i = 0; i < size; i++) {
      heap[i] = arr[i];
    }

    for (int i = size / 2; i >= 0; i--) {
        heapify(i);
    }
  }

  public void heapify(int nodeIdx) {
      int idx = nodeIdx;


      if (2 * nodeIdx + 1 < size && heap[2 * nodeIdx + 1].positionRefound < heap[idx].positionRefound) {
        idx = 2 * nodeIdx + 1;
      }
      // right child
      if (2 * nodeIdx + 2 < size && heap[2 * nodeIdx + 2].positionRefound < heap[idx].positionRefound) {
        idx = 2 * nodeIdx + 2;
      }
      // the element at nodeIdx is the maximum
      if (idx == nodeIdx) {
        return;
      }
      // swap the element at nodeIdx with its maximum child
      LocationSpotted temp = heap[nodeIdx];
      heap[nodeIdx] = heap[idx];
      heap[idx] = temp;
      // recursively call heapify to the maximum child of nodeIdx
      heapify(idx);
  }

  public LocationSpotted pop() {
    LocationSpotted temp = heap[0];

    // now, copy the last node to root to maintain the SHAPE property
    heap[0] = heap[size - 1];

    // decrease the size
    size--;

    // make the remaining array a heap by calling heapify with the new root
    heapify(0);

    return temp;
  }

}



/* Simple class to keep track of the x and y position in a coordinate system
 */
class Coordinate{
  int x;
  int y;

  public Coordinate(){
    x = y = 0;
  }

  public Coordinate(int row, int col) {
    x = row;
    y = col;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void up() {
    y++;
  }

  public void down() {
    y--;
  }

  public void right() {
    x++;
  }

  public void left() {
    x--;
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}