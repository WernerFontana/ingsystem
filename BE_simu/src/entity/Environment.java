package entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import algo.algo.PathFinder;

public class Environment {
	private HashMap<Integer,Line> lineList;
	private HashMap<Integer,Node> nodeList;
	
	private PathFinder path;
	
	public Environment()
	{
		lineList =new HashMap<>();
		nodeList = new HashMap<>();
		
		path = new PathFinder();
	}
	
	public void addLine(Line line)
	{
		lineList.put(line.getID(), line);
	}
	
	public void addNode (Node node)
	{
		nodeList.put(node.getID(), node);
	}
	public HashMap<Integer,Line> getLines(){
		return lineList;
	}
	public HashMap<Integer,Node> getNodes(){
		return nodeList;
	}
	
	public Line getLine(int id)
	{
		return lineList.get(id);
	}
	
	public Node getNode(int id)
	{
		return nodeList.get(id);
	}
	
	public PathFinder getPathFinder(){
		return path;
	}
	
}
