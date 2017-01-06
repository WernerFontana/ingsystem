package algo.algo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import algo.engine.DijkstraAlgorithm;
import algo.model.Edge;
import algo.model.Graph;
import algo.model.IEdge;
import algo.model.IVertex;
import algo.model.Vertex;


public class PathFinder {
	private List<IVertex> nodes;
	private List<IEdge> edges;
	
	private DijkstraAlgorithm dijkstra;

	public static void main(String[] args) {
		PathFinder tda = new PathFinder();
		tda.execute(1,3);
	}
	
	public PathFinder(){
		nodes = new ArrayList<IVertex>();
		edges = new ArrayList<IEdge>();
	}
	
	public void build(){
		Graph graph = new Graph(nodes, edges);
		dijkstra = new DijkstraAlgorithm(graph);
	}

	
	public LinkedList<Integer> execute(int begin, int end) {		
		dijkstra.execute(nodes.get(begin));
		LinkedList<IVertex> path = dijkstra.getNodePath(nodes.get(end));
		LinkedList<Integer> pathId = new LinkedList<Integer>();
		
		for (IVertex vertex : path) {
			pathId.add(Integer.valueOf(vertex.getId()));
		}
		
		return pathId;
	}

	public void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(laneId,nodes.get(sourceLocNo-1), nodes.get(destLocNo-1), duration);
		edges.add(lane);

		lane = new Edge(laneId+"rev",nodes.get(destLocNo-1), nodes.get(sourceLocNo-1),duration);
		edges.add(lane);
	}
	public void addLocation(String nodeName){
		Vertex location = new Vertex(nodeName, nodeName);
		nodes.add(location); 
	}
}