package entity;

import java.util.LinkedList;

import engine.ISimEntity;

public class Car  implements ISimEntity {
	
	public final int longueur = 4;
	public final int distSecu = 1;
	private Frontier begin, end;
	private LinkedList<Line> path;

}
