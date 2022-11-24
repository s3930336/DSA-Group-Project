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
		double rnd = Math.random();
		if (rnd <= 0.25) {
			
			return "UP";
		} else if (rnd <= 0.50) {
			return "DOWN";
		} else if (rnd <= 0.75) {
			return "LEFT";
		} else {
			return "RIGHT";
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