package entity;

import java.util.LinkedList;

import engine.ISimEntity;

public class Line  implements ISimEntity {

	private LinkedList<Car> cars;
	private double longueur;
	public double getLongueur() {
		return longueur;
	}

	public final double largeur = 3;
	private Node begin, end;
	private int ID;
	
	public Line(int ID, double longueur, Node begin, Node end){
		this.ID=ID;
		this.longueur = longueur;
		this.begin = begin;
		this.end = end;
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
	public void addCar(Car c){
		cars.add(c);
	}

	public int getID() {
		return ID;
	}
	
	
}
