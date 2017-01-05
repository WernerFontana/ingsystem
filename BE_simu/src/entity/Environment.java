package entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Environment {
	private HashMap<Integer,Line> lineList;
	private HashMap<Integer,Node> nodeList;
	
	public Environment()
	{
		lineList =new HashMap<>();
		nodeList = new HashMap<>();
	}
	
	public void addLine(Line line)
	{
		lineList.put(line.getID(), line);
	}
	
	public void addNode (Node node)
	{
		nodeList.put(node.getID(), node);
	}
	
	public Line getLine(int id)
	{
		return lineList.get(id);
	}
	
	public Node getNode(int id)
	{
		return nodeList.get(id);
	}
	
}
