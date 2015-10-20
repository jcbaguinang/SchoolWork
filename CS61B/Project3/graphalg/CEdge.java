package graphalg;


public class CEdge implements Comparable<CEdge> {
	//v1 and v2 are the vertices of the edge
	//weight is the weight of the edge
	protected Object v1;
	protected Object v2;
	protected int weight;
	
	//constructs the edge, given the two vertices and the weight
	public CEdge(Object vertex1, Object vertex2, int weight) {
		v1 = vertex1;
		v2 = vertex2;
		this.weight = weight;
	}
	
	//returns one of the vertices
	//@param:
	//@return: v1, an object representing a vertex of the Edge
	public Object v1() {
		return v1;
	}
	
	//returns one of the vertices
	//@param:
	//@return: v2, an object representing a vertex of the Edge
	public Object v2() {
		return v2;
	}
	
	//returns the weight of the edge
	//@param:
	//@return: an int representing the weight of the Edge
	public int weight(){
		return weight;
	}
	
	//compares the edges by their weights
	//@param: another edge e to compare to
	//@return: an int, positive if this is larger than Edge e, zero if this is equal to e, and negative is this is less than e
	public int compareTo(CEdge e){
		return weight - e.weight();	
	}
	
	//returns the weight 
	//@param:
	//@return: the weight in the form of a String
	public String toString(){
		return "" + weight;
	}
}