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
		tda.excute(1,3);
	}
	
	public void build(){
		nodes = new ArrayList<IVertex>();
		edges = new ArrayList<IEdge>();
		
		Graph graph = new Graph(nodes, edges);
		dijkstra = new DijkstraAlgorithm(graph);
	}

	
	public LinkedList<IVertex> excute(int begin, int end) {		
		dijkstra.execute(nodes.get(begin));
		LinkedList<IVertex> path = dijkstra.getNodePath(nodes.get(end));
		LinkedList<Integer> pathId = new LinkedList<Integer>();
		
		for (IVertex vertex : path) {
			//pathId.add(vertex.getId());
			System.out.println(vertex.getId());
		}
		
		return path;
	}

	public void addLane(String laneId, int sourceLocNo, int destLocNo,	int duration) {
		Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);

		lane = new Edge(laneId+"rev",nodes.get(destLocNo), nodes.get(sourceLocNo),duration);
		edges.add(lane);
	}
	public void addLocation(String nodeName)
	{
		Vertex location = new Vertex(nodeName, nodeName);
		nodes.add(location); 
	}
}