package entity;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Car extends Entity implements ISimEntity,Observer {
	
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
		
		env.getPathFinder().execute(begin.getID(), end.getID());
	}
	
	public String toString(){
		return "Car("+this.getID()+") : "+begin.getID()+"->"+end.getID();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
