package entity;

import java.util.HashMap;

import engine.ISimEngine;
import engine.impl.BasicSimEngine;

public class Node {
	protected HashMap<Integer, Line> lines;
	private int ID;
	protected BasicSimEngine engine;
	
	public Node(BasicSimEngine engine, int ID)
	{
		this.ID=ID;
		this.engine = engine;
	}
	
	public int getID(){
		return ID;
	}
	
}
