/* Kruskal.java */

package graphalg;

import graph.*;
import list.*;
import dict.*;
import set.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g){
	  Object[] vertices = g.getVertices();
	  WUGraph mst = new WUGraph();
	  LinkedQueue edges1 = new LinkedQueue();
	  HashTableChained htable = new HashTableChained(vertices.length);
	  
	  for (int i = 0; i < vertices.length; i++){
		  mst.addVertex(vertices[i]);
		  htable.insert(vertices[i], i);

		  if (g.getNeighbors(vertices[i]) == null){
			  continue;
		  }
		  
		  Object[] neighbors = g.getNeighbors(vertices[i]).neighborList;
		  int[] weights = g.getNeighbors(vertices[i]).weightList;
		  
		  for (int k = 0; k < neighbors.length; k++){
			  edges1.enqueue(new CEdge(vertices[i], neighbors[k], weights[k]));
		  }
	  }

	  ListSorts.mergeSort(edges1);
	  DisjointSets minTree = new DisjointSets(vertices.length);
	  
	  while(edges1.size() > 0){
		  try {
			CEdge e = (CEdge) edges1.dequeue();
			Object v1 = e.v1();
			int iv1 = ((Integer) htable.find(v1).value()).intValue();
			Object v2 = e.v2();
			int iv2 = ((Integer) htable.find(v2).value()).intValue();
			
			if (minTree.find(iv1) == minTree.find(iv2)){
				  continue;
			}
			else{
				mst.addEdge(v1, v2, e.weight());
				minTree.union(minTree.find(iv1), minTree.find(iv2));
			}
		  } 
		 catch (QueueEmptyException e) {
			e.printStackTrace();
		 }
	  }
	  return mst;
  }
}