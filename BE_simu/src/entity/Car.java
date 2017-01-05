package entity;

import java.util.LinkedList;

import engine.ISimEntity;

public class Car  implements ISimEntity {
	
	public double longueur;
	public double distSecu;
	private Frontier begin, end;
	private LinkedList<Line> path;
	private int ID;
	
	public Car(int id, double l, double d, Frontier b){
		this.longueur = l;
		this.distSecu = d;
		this.begin = b;
		this.ID = id;
	}
	
	private void findDestination(){
		
	}

}
