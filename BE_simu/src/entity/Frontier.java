package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import algo.algo.PathFinder;
import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Frontier extends Node implements ISimEntity {

	//Contient la liste des horaires de debut des plages
	private LinkedList<LocalDateTime> rawTime = new LinkedList<LocalDateTime>();
	//Contient le nombre de voiture a spawn pour chaque plage
	private LinkedList<Integer> rawNum = new LinkedList<Integer>();
	//Contient l'id d'une frontier et la proba associé à celle-ci
	private HashMap<Integer, Integer> rawProba = new HashMap<Integer, Integer>();
	
	private double longueur = 4.5;
	private double distSecu = 0.5;
	private PathFinder path;

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

	public void generation(){
		LocalDateTime beginSim = engine.getCurrentTime();
		LocalDateTime endSim = engine.getCurrentTime().plus(engine.getSimuDuration());
		LocalDateTime l = engine.getCurrentTime();
		Duration d = Duration.ZERO;
		int numPlage = 0;

		//Determination de la plage de départ de la simulation
		for(int i = 0;i<rawTime.size()-1;i++){
			if(beginSim.isAfter(rawTime.get(i)) || beginSim.isEqual(rawTime.get(i))){
				if(beginSim.isBefore(rawTime.get(i+1))){
					numPlage = i;
				}
			}
		}
		
		while(l.isBefore(endSim)){
			//Generation des voitures pour chaque plage pour un jour
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
			//decalage des plages horaires pour le jour suivant
			for(int k = 0;k<rawTime.size();k++){
				rawTime.set(k, rawTime.get(k).plusDays(1));
			}
			numPlage = 0;
		}

	}


	private void genCar(ISimEngine engine){
		engine.log(this, "generation d'une voiture");
		if(lines != null)
			lines.forEach((id,l) -> l.addCar(new Car(1, longueur, distSecu, this)));
		else
			engine.log(this, "pas de line connectée");
	}

	
	
	public void setLongueur(double l){
		longueur = l;
	}
	public void setDistSecu(double d){
		distSecu = d;
	}
	public void addRawTime(LocalDateTime l){
		rawTime.add(l);
	}
	public void addRawNum(int i){
		rawNum.add(i);
	}
	public void addLine(int id, Line l){
		if(lines.isEmpty())
			lines.put(id, l);
		else
			engine.log(this, ">>>>>>>>>>>>>>Problème d'ajout de line pour Frontier");
	}
	public void setPathFinder(PathFinder p){
		this.path = p;
	}

}
