package entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import algo.algo.PathFinder;

public class Environment {
	private HashMap<Integer,Line> lineList;
	private HashMap<Integer,Node> nodeList;
	
	private int nbCarGen = 0;
	private int nbCarEnd = 0;
	
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

	public void carGen() {
		this.nbCarGen++;
	}

	public void carEnd() {
		this.nbCarEnd++;
	}

	public int getNbCarGen() {
		return nbCarGen;
	}

	public int getNbCarEnd() {
		return nbCarEnd;
	}
	
	
	
	
	
}
