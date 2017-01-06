package entity;

import java.util.HashMap;

import engine.impl.BasicSimEngine;

public abstract class Node extends Entity{
	protected HashMap<Integer, Line> lines;
	
	
	public Node(int ID, BasicSimEngine engine, Environment env)
	{
		super(ID,engine,env);
	}

	
	
}
