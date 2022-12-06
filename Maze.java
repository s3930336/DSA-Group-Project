package Assignment_2_Maze_Robot;

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
		rows = 30;
		cols = 100;
		map = new String[rows];
		map[0] =  "....................................................................................................";
		map[1] =  ".                                              ..                                                  .";
		map[2] =  ".                                              ..                          ..                      .";
		map[3] =  ".                                              ..                          ..                      .";
		map[4] =  ".      ..............                          ..                  ...     ..                      .";
		map[5] =  ".            .............                     ..                  ...     ..   ....................";
		map[6] =  ".                                              ..                  ...     ..   ....................";
		map[7] =  ".       ....  .   .  .  .....                                                   ...     ..         .";
		map[8] =  ".       .  .  .. ..  .    .            ........                     .........   ...     ..         .";
		map[9] =  ".       ....  . . .  .    .            ........                    .........    ...     ..         .";
		map[10] = ".       ..    .   .  .    .            ........                                 ...     ..         .";
		map[11] = ".       . .   .   .  .    .        .   ........                        ..                       ....";
		map[12] = ".       .  .  .   .  .    .        .   ........                        ..                       ....";
		map[13] = ".                                  .                                   ..  ..           .......    .";
		map[14] = ".    ..       ..    ..       ..    .        ..      ..    ...          ..  ..           .......    .";
		map[15] = ".    ....     ..    ....     ..    .        ..      ..    .....        ..  ..                      .";
		map[16] = ".    .. ..    ..    .. ..    ..        .    ..      ..    ..  ...  ..      ..                      .";
		map[17] = ".    ..  ..   ..    ..  ..   ..   .......   ..........    ..   ..  ..      ..                      .";
		map[18] = ".    ..   ..  ..    ..   ..  ..   .......   ..........    ..   ..  ..      ..                      .";
		map[19] = ".    ..    .. ..    ..    .. ..        .    ..      ..    ..  ...          ..                      .";
		map[20] = ".    ..     ....    ..     ....             ..      ..    .....            ..    ..........        .";
		map[21] = ".    ..       ..    ..       ..             ..      ..    ...              ..    ..........        .";
		map[22] = ".                                                                          ..                      .";
		map[23] = ".      .            .           .            .                             ..                      .";
		map[24] = ".      .     .      .           .            .                             ..         X            .";
		map[25] = ".      .     .      .                        .        .                    ..                      .";
		map[26] = ".      .     .      .                        .        .                    ..                      .";
		map[27] = ".      .     .      .           .            .        .                    ..                      .";
		map[28] = ".            .                  .                     .                    ..                      .";
		map[29] = "....................................................................................................";
		robotCol = 44;
		robotRow = 25;
		steps = 0;

//		//Test case for dead end
//		rows = 3;
//		cols = 7;
//		map = new String[rows];
//		map[0] =  ".......";
//		map[1] =  ".X    .";
//		map[2] =  ".......";
//		robotCol = 2;
//		robotRow = 1;
//		steps = 0;
	}

	/*
	 * The go() method of the maze.
	 * Precondition: The method take a String that
	 * specify the direction the robot will attempt to travel to. The only valid
	 * directions are UP, RIGHT, DOWN, LEFT.
	 * 
	 * Return a String containing the result of the attempt to move in the Maze: 
	 * 	"invalid": if the direction given is invalid 
	 * 	"win": if the exit has been reached 
	 * 	"false": if the direction the robot try to reach is a wall 
	 * 	"true": if the direction the robot try to reach is a space 
	 * 
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

		// Flag to check if starting position == exit gate
		if (steps == 0 && map[robotRow].charAt(robotCol) == 'X') {
			return "win";
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
			robotRow = currentRow;
			robotCol = currentCol;
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

/*
 * The Robot class that navigates the Map
 * 
 */
class Robot {
	/*
	 * Private Data 
	 * The Robot includes an internal map that keep track of the route
	 * it has been using to navigate the maze 
	 * 
	 * The map is a 2D integer array that will be marked and updated 
	 * by the robot as it traverse the maze 
	 * 
	 * The map is double the size of the maximum row and columns of 
	 * the maze, because the robot can start at any corner of the map.
	 * 
	 * The robot starts at the center of the map
	 * 
	 * The map stores the data as follow: 
	 * 	-1: a wall 
	 * 	0: not yet visited positive
	 * 	positive integer: number of times this space has been visited
	 */
	
//	//For testing default small map
//	private int mapRowSize = 60;
//	private int mapColSize = 200;
	
	//Correct data
	private int mapRowSize = Maze.MAX_ROW * 2;
	private int mapColSize = Maze.MAX_COL * 2;
	private int[][] map = new int[mapColSize][mapRowSize];
	private Coordinate robotPosition = new Coordinate(mapColSize/2, mapRowSize/2);

	/*
	 * Navigate method The navigate method of the robot will repeatedly attempt to
	 * find the exit in the maze, one step at a time, until it finds the exit If
	 * current position is marked as wall, backtrack to the previous position
	 */
	public void navigate() {
		Maze maze = new Maze();
		String result = "";
		String direction = "";

		while (!result.equals("win")) {
			DeadEndFlag deadEnd = new DeadEndFlag();
			direction = directionToGo(deadEnd);
			result = maze.go(direction);
			updateMapAndPosition(result, direction, deadEnd);
			System.out.println(direction + ", " + result  + ". Robot position: " 
			+ robotPosition.toString() + ". Maze Position: " + maze.robotCol + ", " + maze.robotRow);
		}
		System.out.println("Steps to reach the Exit gate " + maze.steps);
//		printMap();
	}

	/*
	 * directionToGo() method 
	 * The directionToGo() method of the robot will return a
	 * String indicating the direction the robot will move in 
	 * 
	 * The method takes an Object of class DeadEndFlag to flag
	 * whether the space occupied is a dead end, in order to 
	 * improve the algorithm.
	 * 
	 * Return: "UP", "RIGHT", "DOWN", "LEFT" 
	 * 
	 * The directionToGo() method will prioritize which space to move
	 * as follow: 
	 * 	1. Position marked as wall will not be considered to be visited
	 * 		again 
	 * 	2. Space that have been visited the smallest number of time will be
	 * 		visited 
	 * 	3. If multiple places have the same number of visits, prioritize in
	 * 		order of UP, RIGHT, DOWN, LEFT
	 * 
	 */
	private String directionToGo(DeadEndFlag deadEnd) {
		// Create the heap to put the DirectionInfo objects into
		MinHeapDI heap = new MinHeapDI();

		// Create the temporary Coordinate to get the 4 direction info
		Coordinate temp = new Coordinate(robotPosition);

		// Create the "UP" DirectionInfo object
		temp.up();
		int visitNum = map[temp.getX()][temp.getY()];
		DirectionInfo up = new DirectionInfo("UP", visitNum);
		heap.push(up); // Push the UP object into the heap

		// Create the "RIGHT" DirectionInfo object
		temp.down();
		temp.right();
		visitNum = map[temp.getX()][temp.getY()];
		DirectionInfo right = new DirectionInfo("RIGHT", visitNum);
		heap.push(right); // Push the RIGHT object into the heap

		// Create the "DOWN" DirectionInfo object
		temp.left();
		temp.down();
		visitNum = map[temp.getX()][temp.getY()];
		DirectionInfo down = new DirectionInfo("DOWN", visitNum);
		heap.push(down); // Push the RIGHT object into the heap

		// Create the "LEFT" DirectionInfo object
		temp.up();
		temp.left();
		visitNum = map[temp.getX()][temp.getY()];
		DirectionInfo left = new DirectionInfo("LEFT", visitNum);
		heap.push(left); // Push the RIGHT object into the heap

		// Once the 4 directions has been arranged in the heap, we can pop the heap to
		// get the direction we want to move
		DirectionInfo result;
		while (!heap.isEmpty()) {
			result = heap.pop();
			
			//If after popping the heap, the heap is empty, it means all three other directions are wall,
			//then this space is a dead end
			if (heap.isEmpty()) 
				deadEnd.setFlag(true);	 
			
			if (result.isWall())
				continue; // If the direction is a wall, we ignore and pop the next direction
			else
				return result.direction();
		}

		return "INVALID"; // If all four direction has been popped, but no valid movement, maze is broken
	}

	/*
	 * updateMapAndPosition method 
	 * This method takes the result of the movement in the maze, 
	 * the direction the robot moves in and the dead end flag to update its internal map
	 * Precondition: 
	 * 		result must be one of 4 String: "invalid", "win", "true", "false" 
	 * 		direction must be one of 4 String: "UP", "DOWN", "LEFT", "RIGHT"
	 */
	private void updateMapAndPosition(String result, String direction, DeadEndFlag deadEnd) {
		Coordinate temp = new Coordinate(robotPosition);
		if ((result == "win" || result == "true" || result == "false")
				&& (direction == "UP" || direction == "DOWN" || direction == "LEFT" || direction == "RIGHT")) {

			switch (direction) {
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
				// If the result of the movement in the maze is false, meaning the robot
				// encounter a wall, mark it in the internal map
				map[temp.getX()][temp.getY()] = -1;
			} else {
				// If the movement is successful, add the count of visits to the current
				// position and move the robot in its internal map
				map[robotPosition.getX()][robotPosition.getY()]++;
				
				//If it's a dead end, mark 1 plus to prevent going in again
				if (deadEnd.isDeadEnd()) 
					map[robotPosition.getX()][robotPosition.getY()]++; 
				
				//Move the robot in its internal map
				robotPosition = temp;
			}
		}

		return; // input is invalid, do nothing
	}
	
	/*
	 * printMap()
	 * Method to print the internal map of the robot to the out stream
	 */
	private void printMap() {
		for(int i = 0; i < mapRowSize; i++) {
			for (int j = 0; j < mapColSize; j++) {
				if (map[j][i] == -1) {
					System.out.print(".");
				} else if (map[j][i] >= 10) {
					System.out.print("^");
				} else {
					System.out.print(map[j][i]);
				}
			}
			System.out.println();
		}
	}
}

/*
 * Simple class to keep track of the x and y position in a coordinate system
 */
class Coordinate {
	private int x, y;

	public Coordinate() {
		x = y = 0;
	}

	public Coordinate(int col, int row) {
		x = col;
		y = row;
	}

	public Coordinate(Coordinate other) {
		this(other.x, other.y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void up() {
		y--;
	}

	public void down() {
		y++;
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

/*
 * MinHeap class of DirectionInfo 
 * This MinHeapDI class holds objects of the
 * DirectionInfo class and put them into a minimum heap, where the root of a
 * tree is always the minimum object 
 * 
 * Public operations: 
 * 	buildHeap(): build MinHeap from an array of type DirectionInfo
 *  push(): add a new DirectionInfo object to the heap
 *  pop(): return the DirectionInfo object at the root of the
 * 			MinHeap and delete the object from the heap
 * 
 * Acknowledgement: Some elements of this class has been modified from the
 * example solution from week 4 module, Problem 3 (from the course public
 * GitHub)
 */
class MinHeapDI {
	private static final int MAX_SIZE = 4; // Each step only have 4 directions to compare
	private int size; // actual number of elements in the heap
	private DirectionInfo[] heap;

	// Default Constructor with empty array
	public MinHeapDI() {
		heap = new DirectionInfo[MAX_SIZE];
		;
		size = 0;
	}

	// Constructor to build a heap from given array
	public MinHeapDI(DirectionInfo[] array) {
		heap = new DirectionInfo[MAX_SIZE];
		size = array.length;
		buildHeap(array);
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// construct a heap from an array
	public void buildHeap(DirectionInfo[] arr) {
		// first, copy the values from array to heap
		// and set the size accordingly
		size = arr.length;
		for (int i = 0; i < size; i++) {
			heap[i] = arr[i];
		}

		// Note 1: we apply the heapify down process to all internal nodes
		// because leaf nodes are heap already

		// Note 2: the number of internal nodes in a complete binary tree
		// of n nodes is floor(n/2)
		// https://en.wikipedia.org/wiki/Binary_tree#Properties_of_binary_trees

		for (int i = size / 2; i >= 0; i--) {
			heapifyDown(i);
		}
	}

	// heapify the tree whose root node has index nodeIdx
	// assumption: its two subtrees are heap already
	private void heapifyDown(int nodeIdx) {
		// index of left child 2 * nodeIdx + 1
		// index of right child 2 * nodeIdx + 2
		// index of parent (i - 1) / 2

		// if this node < left and right childdren => heap already
		// otherwise, exchange it with the max(left, right)
		int idx = nodeIdx;
		// left child
		if (2 * nodeIdx + 1 < size && heap[2 * nodeIdx + 1].isSmaller(heap[idx])) {
			idx = 2 * nodeIdx + 1;
		}
		// right child
		if (2 * nodeIdx + 2 < size && heap[2 * nodeIdx + 2].isSmaller(heap[idx])) {
			idx = 2 * nodeIdx + 2;
		}
		// the element at nodeIdx is the minimum
		if (idx == nodeIdx) {
			return;
		}
		// swap the element at nodeIdx with its minimum child
		DirectionInfo temp = heap[nodeIdx];
		heap[nodeIdx] = heap[idx];
		heap[idx] = temp;
		// recursively call heapify to the minimum child of nodeIdx
		heapifyDown(idx);
	}

	public DirectionInfo pop() {
		// let's implement this method to extract the root of the heap
		// and use it to test the buildHeap method

		// first, store the value of root
		DirectionInfo temp = heap[0];

		// now, copy the last node to root to maintain the SHAPE property
		heap[0] = heap[size - 1];

		// decrease the size
		size--;

		// make the remaining array a heap by calling heapify with the new root
		heapifyDown(0);

		return temp;
	}

	// Heapify the node at the nodeIndex by compare it with its parent.
	// If it swaps with its parent, recursively heapify up to the root
	private void heapifyUp(int nodeIndex) {
		// Already at the root, so heapifyUp must have finish
		if (nodeIndex == 0)
			return;

		// index of parent (i - 1) / 2
		int parentIndex = (nodeIndex - 1) / 2;

		// If the element at the index is smaller than its parent, swap, then
		// recursively call heapifyUp on the parentIndex after swap
		if (heap[nodeIndex].isSmaller(heap[parentIndex])) {
			DirectionInfo temp = heap[nodeIndex];
			heap[nodeIndex] = heap[parentIndex];
			heap[parentIndex] = temp;
			heapifyUp(parentIndex);
		}

		// If no swap happens, then the element at the index must be at the right
		// position
		return;
	}

	// Insert a new element in the heap
	public void push(DirectionInfo item) {
		// Increase the array size to put in new element
		size++;

		// Put the new element into the array
		heap[size - 1] = item;

		// heapify up at the index of the new item
		heapifyUp(size - 1);
	}
}

/*
 * DirectionInfo Class 
 * The class is to create an object that corresponds to each
 * of the four directions that the robot can move to at each space. 
 * 
 * This object is one of the 4 directions and contains the number 
 * of visits the robot has made in that space. 
 * 
 * The number of visit can be -1 to represent a invalid
 * space (a "wall") 
 * 
 * The directional priority of this class ("smaller" direction)
 * is defined as "UP" < "RIGHT" < "DOWN" < "LEFT"
 */
class DirectionInfo {
	private String direction; // The direction name. Only "UP", "RIGHT", "DOWN", "LEFT" are valid
	private int directionalPriority; // Priority of the direction represented as an int
	private int visitNum; // Number of visits of the space in that direction
	private boolean valid; // Flag to check if the object holds all the necessary information

	// Default Constructor
	public DirectionInfo() {
		this("", 0);
	}

	// Constructor with 2 arguments
	public DirectionInfo(String direction, int visitNum) {
		valid = true;
		this.visitNum = visitNum;
		this.direction = direction;

		// Set the directional priority of the object
		switch (direction) {
		case "UP":
			directionalPriority = 1;
			break;
		case "RIGHT":
			directionalPriority = 2;
			break;
		case "DOWN":
			directionalPriority = 3;
			break;
		case "LEFT":
			directionalPriority = 4;
			break;
		default:
			directionalPriority = 0;
			valid = false;
		}
	}

	// Return whether the object is valid i.e. holds all the necessary info
	public boolean isValid() {
		return valid;
	}

	// Return the direction name
	public String direction() {
		return direction;
	}

	// Return the number of visit in this direction
	public int numOfVisit() {
		return visitNum;
	}

	// Return a boolean of whether the space in this direction is a wall
	public boolean isWall() {
		return visitNum == -1;
	}

	// Return the object as a String
	public String toString() {
		return "Direction: " + direction + ", Number of Visit: " + visitNum;
	}

	/*
	 * ========================================= 
	 * Comparison Methods The comparison methods of the class 
	 * will determine how the direction is chosen at each step
	 * The "smallest" object of this class is prioritized as follow: 
	 * 	1. The number of visit is smaller 
	 * 	2. If number of visit is equal, "UP" < "RIGHT" < "DOWN" < "LEFT" 
	 * ==========================================
	 */
	public boolean isEqual(DirectionInfo other) {
		return this.direction == other.direction && this.visitNum == other.visitNum;
	}

	public boolean isSmaller(DirectionInfo other) {
		if (this.isEqual(other))
			return false;

		if (this.visitNum > other.visitNum)
			return false;
		else if (this.visitNum < other.visitNum)
			return true;
		else
			return this.directionalPriority < other.directionalPriority;
	}

	public boolean isBigger(DirectionInfo other) {
		return !(this.isEqual(other) || this.isSmaller(other));
	}

	public boolean isEqualOrSmaller(DirectionInfo other) {
		return this.isEqual(other) || this.isSmaller(other);
	}

	public boolean isEqualOrBigger(DirectionInfo other) {
		return this.isEqual(other) || this.isBigger(other);
	}
}

/*
 * DeadEndFlasg class
 * Simple wrapper class to flag a dead end to enhance the algorithm's efficiency
 */
class DeadEndFlag {
	private boolean deadEnd;
	
	public DeadEndFlag() {
		this(false);
	}
	
	public DeadEndFlag(boolean flag) {
		deadEnd = flag;
	}
	
	public void setFlag(boolean flag) {
		deadEnd = flag;
	}
	
	public boolean isDeadEnd() {
		return deadEnd;
	}
}