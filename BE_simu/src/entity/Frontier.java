package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;

import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Frontier extends Node implements ISimEntity {
	
	//Contient la liste des horaires de debut des plages
	private LinkedList<LocalDateTime> rawTime = new LinkedList<LocalDateTime>();
	//Contient le nombre de voitures a spawn pour chaque plage
	private LinkedList<Integer> rawNum = new LinkedList<Integer>();
	
	
	public Frontier(BasicSimEngine engine, int ID) {
		super(engine, ID);
		generation();
	}
	
	public void addRawTime(LocalDateTime l){
		rawTime.add(l);
	}
	public void addRawNum(int i){
		rawNum.add(i);
	}

	public void generation(){
		LocalDateTime l = engine.getCurrentTime();
		Duration d = Duration.ZERO;
		
		
		
		for(int i = 0;i<rawTime.size()-1;i++){
			d = Duration.between(rawTime.get(i), rawTime.get(i+1));
			d = d.dividedBy(rawNum.get(i));
			
			for(int j = 0;j<rawNum.get(i);j++){
				l = l.plus(d);
				engine.scheduleEventAt(this, l, this::genCar);
			}
			System.out.println(d);
		}
		
		
		
	}
	
	
	public void genCar(ISimEngine engine){
		engine.log(this, "generation d'une voiture");
		
	}

	

}
