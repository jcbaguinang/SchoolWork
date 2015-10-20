package player;

public class Coord {
	
	private int x;
	private int y;
	private boolean visited;
	//Constructor for a Coord object, which stores a location and a visited boolean.
	//@param int x: x coordinate of object.
	//@param int y: y coordinate of object.
	public Coord(int x, int y){
		this.x = x;
		this.y = y;
		this.visited = false;
	}
	//returns the x value of the coordinate object.
	//@return int: x value.
	public int x(){
		return x;
	}
	//returns the y value of the coordinate object.
	//@return int: y value.
	public int y(){
		return y;
	}
	//marks the coordinate as visited.
	public void visit(){
		visited = true;
	}
	//unmarks the coordinate, changing it to unvisited.
	public void unvisit(){
		visited = false;
	}
	//returns whether the coordinate has been visited or not.
	//@return boolean: whether it was visited before.
	public boolean visited(){
		return visited;
	}


}