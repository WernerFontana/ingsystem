package entity;

import java.util.LinkedList;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Line extends Entity implements ISimEntity {

	private LinkedList<Car> cars = new LinkedList<Car>();
	private int longueur;
	public final double largeur = 3;
	private Node begin, end;
	
	public Line(int ID, BasicSimEngine engine, Environment env, int longueur, Node begin, Node end){
		super(ID,engine,env);
		this.longueur = longueur;
		this.begin = begin;
		this.end = end;
	}
	
	public void addCar(Car c){
		cars.add(c);
	}
	
	public boolean isFull(){
		double l = 0.0;
		for(Car c : cars){
			l+= c.longueur;
			l+= c.distSecu;
		}
		if(l > longueur)
			return true;
		else
			return false;
	}
	
	public LinkedList<Car> getCars() {
		return cars;
	}
	public void setCars(LinkedList<Car> cars) {
		this.cars = cars;
	}
	public Node getBegin(){
		return begin;
	}
	public Node getEnd(){
		return end;
	}
	public int getLongueur() {
		return longueur;
	}
	
}
