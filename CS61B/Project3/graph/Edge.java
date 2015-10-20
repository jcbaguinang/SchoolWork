package graph;
public class Edge {
	/**
	*  v1 is the first vertice of the edge. It is also always the vertice that contains this edge in its list.
	*  v2 is the second vertice of the edge. It always contains the neighbor vertice, not the vertice that contains this edge.
	*  weight is the weight of the edge.
	**/
	protected Object v1;
	protected Object v2;
	protected int weight;
	/**
	*  creates an edge with the following vertices and weight
	*  @param vertex1 is the first vertice (origin).
	*  @param vertex2 is the second vertice (neighbor).
	*  @param weight is the weight of the edge.
	**/
	public Edge(Object vertex1, Object vertex2, int weight) {
		v1 = vertex1;
		v2 = vertex2;
		this.weight = weight;
	}
	/**
	* returns the origin vertice
	*  @return the origin vertice
	**/
	public Object v1() {
		return v1;
	}
	/**
	* returns the neighbor vertice
	*  @return the neighbor vertice
	**/
	public Object v2() {
		return v2;
	}
	/**
	*  returns the weight
	*  @return the weight of the edge
	**/
	public int weight(){
		return weight;
	}
	/**
	* takes in an int parameter and updates the weight with this int.
	*  @param the new weight of the edge.
	**/
	void updateWeight(int weight) {
		this.weight = weight;
	}
}