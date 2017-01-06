package entity;

import java.util.LinkedList;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Car extends Entity implements ISimEntity {
	
	public double longueur;
	public double distSecu;
	private Frontier begin, end;
	private LinkedList<Line> path;
	
	public Car(int id, BasicSimEngine engine, Environment env, double l, double d, Frontier begin, Frontier end){
		super(id,engine,env);
		this.longueur = l;
		this.distSecu = d;
		this.begin = begin;
		this.end = end;
		
		engine.log(this, "generation Car");
	}
	
	private void findDestination(){
		
	}
	
	public String toString(){
		return "Car("+this.getID()+") : "+begin.getID()+"->"+end.getID();
	}

}
