package algo.model;

public class Edge implements IEdge {
  private final String id; 
  private final IVertex source;
  private final IVertex destination;
  private final int weight; 
  
  public Edge(String id, IVertex source, IVertex destination, int weight) {
    this.id = id;
    this.source = source;
    this.destination = destination;
    this.weight = weight;
  }
  
  public String getId() {
    return id;
  }
  public IVertex getDestination() {
    return destination;
  }

  public IVertex getSource() {
    return source;
  }
  public double getWeight() {
    return weight;
  }
  
  @Override
  public String toString() {
    return source + " " + destination;
  }
  
  
}