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
		rawTime.add(LocalDateTime.of(2017, 1, 1, 0, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 7, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 9, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 17, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 19, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 2, 0, 0));
		
		rawNum.add(5);
		rawNum.add(2);
		rawNum.add(3);
		rawNum.add(4);
		rawNum.add(5);
		
		
		generation();
	}
	
	public void addRawTime(LocalDateTime l){
		rawTime.add(l);
	}
	public void addRawNum(int i){
		rawNum.add(i);
	}

	public void generation(){
		LocalDateTime beginSim = engine.getCurrentTime();
		LocalDateTime l = engine.getCurrentTime();
		Duration d = Duration.ZERO;
		int numPlage = 0;
		
		//Determination de la plage de d�part de la simulation
		for(int i = 0;i<rawTime.size()-1;i++){
			if(beginSim.isAfter(rawTime.get(i)) || beginSim.isEqual(rawTime.get(i))){
				if(beginSim.isBefore(rawTime.get(i+1))){
					numPlage = i;
				}
			}
		}
		
		//Generation des voitures pour chaque plage
		for(int i = numPlage;i<rawTime.size()-1;i++){
			d = Duration.between(rawTime.get(i), rawTime.get(i+1));
			d = d.dividedBy(rawNum.get(i));
			
			l = rawTime.get(i);
			for(int j = 0;j<rawNum.get(i);j++){
				l = l.plus(d);
				if(l.isAfter(beginSim))
					engine.scheduleEventAt(this, l, this::genCar);
			}
			
		}	
		
	}
	
	
	public void genCar(ISimEngine engine){
		engine.log(this, "generation d'une voiture");
		
	}

	

}
