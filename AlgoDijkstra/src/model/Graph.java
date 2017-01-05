package model;

import java.util.List;


public class Graph {
  private final List<? extends IVertex> vertexes;
  private final List<? extends IEdge> edges;

  public Graph(List<? extends IVertex> vertexes, List<? extends IEdge> edges) {
    this.vertexes = vertexes;
    this.edges = edges;
  }

  public List<? extends IVertex> getVertexes() {
    return vertexes;
  }

  public List<? extends IEdge> getEdges() {
    return edges;
  }
  
  
  
}