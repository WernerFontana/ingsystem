package entity;

import java.util.HashMap;

public class Node {
	private HashMap<Integer, Line> lines;
	private int ID;
	
	public Node(int ID)
	{
		this.ID=ID;
	}
	
	public int getID(){
		return ID;
	}
}
