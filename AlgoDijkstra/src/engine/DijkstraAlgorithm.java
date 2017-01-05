package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Graph;
import model.IEdge;
import model.IVertex;

public class DijkstraAlgorithm {

  private final List<IVertex> nodes;
  private final List<IEdge> edges;
  private Set<IVertex> settledNodes;
  private Set<IVertex> unSettledNodes;
  private Map<IVertex, IVertex> predecessors;
  private Map<IVertex, Double> distance;

  public DijkstraAlgorithm(Graph graph) {
    // create a copy of the array so that we can operate on this array
    this.nodes = new ArrayList<IVertex>(graph.getVertexes());
    this.edges = new ArrayList<IEdge>(graph.getEdges());
  }

  public void execute(IVertex source) {
    settledNodes = new HashSet<IVertex>();
    unSettledNodes = new HashSet<IVertex>();
    distance = new HashMap<IVertex, Double>();
    predecessors = new HashMap<IVertex, IVertex>();
    distance.put(source, 0.0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0) {
      IVertex node = getMinimum(unSettledNodes);
      settledNodes.add(node);
      unSettledNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  private void findMinimalDistances(IVertex node) {
    List<IVertex> adjacentNodes = getNeighbors(node);
    for (IVertex target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node)
            + getDistance(node, target));
        predecessors.put(target, node);
        unSettledNodes.add(target);
      }
    }

  }

  private IEdge getEdge(IVertex node, IVertex target) {
	    for (IEdge edge : edges) {
	        if (edge.getSource().equals(node)
	            && edge.getDestination().equals(target)) {
	          return edge;
	        }
	      }	 
	    return null;
  }
  
  private double getDistance(IVertex node, IVertex target) {
    for (IEdge edge : edges) {
      if (edge.getSource().equals(node)
          && edge.getDestination().equals(target)) {
        return edge.getWeight();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<IVertex> getNeighbors(IVertex node) {
    List<IVertex> neighbors = new ArrayList<IVertex>();
    for (IEdge edge : edges) {
      if (edge.getSource().equals(node)
          && !isSettled(edge.getDestination())) {
        neighbors.add(edge.getDestination());
      }
    }
    return neighbors;
  }

  private IVertex getMinimum(Set<IVertex> vertexes) {
    IVertex minimum = null;
    for (IVertex vertex : vertexes) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }

  private boolean isSettled(IVertex vertex) {
    return settledNodes.contains(vertex);
  }

  private double getShortestDistance(IVertex destination) {
    Double d = distance.get(destination);
    if (d == null) {
      return Double.MAX_VALUE;
    } else {
      return d;
    }
  }

  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  public LinkedList<IVertex> getNodePath(IVertex target) {
    LinkedList<IVertex> path = new LinkedList<IVertex>();
    IVertex step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
  }
  
  public LinkedList<IEdge> getEdgePath(IVertex target) {
	  LinkedList<IEdge> path = new LinkedList<IEdge>();
	    IVertex step = target;
	    // check if a path exists
	    if (predecessors.get(step) == null) {
	      return null;
	    }
	    while (predecessors.get(step) != null) {
	    	IVertex previousStep = step;
	      step = predecessors.get(previousStep);
	      
	      path.add(getEdge(step, previousStep));
	    }
	    // Put it into the correct order
	    Collections.reverse(path);
	    return path;
  }

}