/* WUGraph.java */

package graph;
import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  private DList vertices;
  private HashTableChained verticeTable;
  private HashTableChained edgeTable;
  private int numVertices;
  private int numEdges;
  public WUGraph() {
    verticeTable = new HashTableChained(10);
    edgeTable = new HashTableChained(10);
    vertices = new DList();
    numVertices = 0;
    numEdges = 0;

  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount()
  {
    return numVertices;
  }
  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount(){
    return numEdges;
  }
  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
    try {
      if (vertices.length() != 0) {
        Object[] result = new Object[vertices.length()];
        DListNode current = (DListNode) vertices.front();
        for (int i = 0; i < vertices.length(); i++) {
          result[i] = current.item();
          current = (DListNode) current.next();
        }
        return result;
      }
      return new Object[0];
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
    return null;
  }
  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */

  public void addVertex(Object vertex){
    if (verticeTable.loadFactor() > 1.0) {
      verticeTable.resize();
    }
    if (verticeTable.find(vertex) == null) {
      vertices.insertBack(vertex, true);
      verticeTable.insert(vertex, vertices.back());
      numVertices++;
    }
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    try {
      if (verticeTable.loadFactor() < .5) {
        verticeTable.resize();
      }
      Entry vertexNode = verticeTable.remove(vertex);
      if (vertexNode != null) {
        DList edge = ((DListNode) vertexNode.value()).edgeList();
        DListNode cycle = (DListNode) edge.front();
        while (cycle.isValidNode()) {
          if (cycle.partnerEdge() != null) {
            edgeTable.remove(new VertexPair(((Edge) cycle.partnerEdge().item()).v1(), ((Edge) cycle.partnerEdge().item()).v2()));
            (cycle.partnerEdge()).remove();
          }
          DListNode cycleNext = (DListNode) cycle.next();
          edgeTable.remove(new VertexPair(((Edge) cycle.item()).v1(), ((Edge) cycle.item()).v2()));
          cycle.remove();
          cycle = cycleNext;
          numEdges--;
        }
        ((DListNode) vertexNode.value()).remove();
        numVertices--;
      }
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex){
    Entry vertexNode = verticeTable.find(vertex);
    if (vertexNode == null) {
      return false;
    }
    return true;

  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex){
    Entry vertexNode = verticeTable.find(vertex);
    if (vertexNode == null) {
      return 0;
    }
    else {
      return ((DListNode) vertexNode.value()).edgeList().length();
    }
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex){
    try {
      Neighbors vertexNeighbor = new Neighbors();
      Entry vertexNode = verticeTable.find(vertex);
      if (vertexNode != null && ((DListNode) vertexNode.value()).edgeList().length() != 0) {
        DList edge = ((DListNode) vertexNode.value()).edgeList();
        vertexNeighbor.neighborList = new Object[edge.length()];
        vertexNeighbor.weightList = new int[edge.length()];
        DListNode cycle = (DListNode) edge.front();
        for (int i = 0; cycle.isValidNode(); i++) {
          vertexNeighbor.neighborList[i] = ((Edge) cycle.item()).v2();
          vertexNeighbor.weightList[i] = ((Edge) cycle.item()).weight();
          cycle = (DListNode) cycle.next();
        }
        return vertexNeighbor;
      }
      return null;
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
    return null;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight){
    try {
      if (edgeTable.loadFactor() > 1.0) {
        edgeTable.resize();
      }
      Edge e1 = new Edge(u, v, weight);
      Edge e2 = new Edge(v, u, weight);
      Entry uEntry = verticeTable.find(u);
      Entry vEntry = verticeTable.find(v);
      if(uEntry != null && vEntry != null && !isEdge(u, v)) {
        if (u == v) {
          ((DListNode) uEntry.value()).edgeList().insertBack(e1, false);
          DListNode nodeEdge1 = (DListNode) ((DListNode) uEntry.value()).edgeList().back();
          edgeTable.insert(new VertexPair(u, v), nodeEdge1);
        }
        else {
          ((DListNode) uEntry.value()).edgeList().insertBack(e1, false);
          ((DListNode) vEntry.value()).edgeList().insertBack(e2, false);
          DListNode nodeEdge1 = (DListNode) ((DListNode) uEntry.value()).edgeList().back();
          DListNode nodeEdge2 = (DListNode) ((DListNode) vEntry.value()).edgeList().back();
          nodeEdge1.partnerEdge(nodeEdge2);
          nodeEdge2.partnerEdge(nodeEdge1);
          edgeTable.insert(new VertexPair(u, v), nodeEdge1);
          edgeTable.insert(new VertexPair(u, v), nodeEdge2);
        }
        numEdges++;
      }
      else if (uEntry != null && vEntry != null) {
        DListNode edgeNode = (DListNode) edgeTable.find(new VertexPair(u, v)).value();
        ((Edge) edgeNode.item()).updateWeight(weight);
        ((Edge) edgeNode.partnerEdge().item()).updateWeight(weight);
      }
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v){
    try {
      if (edgeTable.loadFactor() < .5) {
        edgeTable.resize();
      }
      Entry edgeNode = edgeTable.remove(new VertexPair(u, v));
      if (edgeNode != null) {
        if (((DListNode) edgeNode.value()).partnerEdge() != null) {
          ((DListNode) edgeNode.value()).partnerEdge().remove();
          edgeTable.remove(new VertexPair(u, v));
        }
        ((DListNode) edgeNode.value()).remove();
        edgeTable.remove(new VertexPair(u, v));
        numEdges--;
      }
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v){
    Entry edgeNode = edgeTable.find(new VertexPair(u, v));
    if (edgeNode == null) {
      return false;
    }
    return true;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
    try {
      Entry edgeNode = edgeTable.find(new VertexPair(u, v));
      if (edgeNode != null) {
        return ((Edge) ((DListNode) edgeNode.value()).item()).weight();
      }
      return 0;
    }
    catch (InvalidNodeException e) {
      System.out.println(e);
    }
    return 0;
  }
}
