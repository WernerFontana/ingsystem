package entity;

import java.util.LinkedList;

import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Line extends Entity implements ISimEntity {

	private LinkedList<Car> cars;
	private int longueur;
	public final double largeur = 3;
	private Node begin, end;
	public final static int FEU=0,STOP=1,END=2,FREE=3;
	private boolean outFree=true;
	
	public Line(int ID, BasicSimEngine engine, Environment env, int longueur, Node begin, Node end){
		super(ID,engine,env);
		this.longueur = longueur;
		this.begin = begin;
		this.end = end;
		cars = new LinkedList<Car>();
	}
	
	public void addCar(Car c){
		cars.add(c);
	}
	public void removeCar(Car c){
		cars.remove(c);
		setChanged();
		notifyObservers();
	}
	
	public boolean isFull(){
		double l = 0.0;
		for(Car c : cars){
			l+= c.longueur;
			l+= c.distSecu;
		}
		//System.out.println(l+"   "+longueur);
		if(l > longueur*2){
			return true;}
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
	
	public String toString(){
		String s = "";
		s += "Line("+getID()+") = begin : "+begin+", end : "+end;
		return s;
	}
	
	public boolean isOutFree()
	{
		return outFree;
	}
	
	public void setOutFree(boolean b)
	{
		this.outFree=b;
		this.setChanged();
		this.notifyObservers();		
	}
	
	public void notifyStop()
	{
		this.setChanged();
		this.notifyObservers();
	}
}
