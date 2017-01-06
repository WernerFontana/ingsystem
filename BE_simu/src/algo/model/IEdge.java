package algo.model;

public interface IEdge  {
  
  public String getId();

  public IVertex getDestination();
  
  public IVertex getSource();
  
  public double getWeight();


}