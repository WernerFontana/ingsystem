package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import engine.DijkstraAlgorithm;
import model.Edge;
import model.Graph;
import model.IEdge;
import model.IVertex;
import model.Vertex;


public class TestDijkstraAlgorithm {
	private List<IVertex> nodes;
	private List<IEdge> edges;

	public static void main(String[] args) {
		TestDijkstraAlgorithm tda = new TestDijkstraAlgorithm();
		tda.testExcute(1,3);
	}

	public void addLocation(String nodeName)
	{
		Vertex location = new Vertex(nodeName, nodeName);
		nodes.add(location); 
	}
	public LinkedList<IVertex> testExcute(int begin, int end) {
		nodes = new ArrayList<IVertex>();
		edges = new ArrayList<IEdge>();
		for(LaneNode route: LaneNode.values())
		{
			addLocation(route.name());	
		}
		for(Lane lane:Lane.values())
		{
			addLane(lane.name, lane.source, lane.dest, lane.poid);
		}

		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(nodes.get(begin));
		LinkedList<IVertex> path = dijkstra.getNodePath(nodes.get(end));
		LinkedList<Integer> pathId = new LinkedList<Integer>();
		
		for (IVertex vertex : path) {
			//pathId.add(vertex.getId());
			System.out.println(vertex.getId());
		}
		
		return path;

	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo,
			int duration) {
		Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);

		lane = new Edge(laneId+"rev",nodes.get(destLocNo), nodes.get(sourceLocNo),duration);
		edges.add(lane);
	}
}