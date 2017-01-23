package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;
import utils.IDGenerator;

public class Frontier extends Node implements ISimEntity {

	//Contient la liste des horaires de debut des plages
	private LinkedList<LocalDateTime> rawTime = new LinkedList<LocalDateTime>();
	//Contient le nombre de voiture a spawn pour chaque plage
	private LinkedList<Integer> rawNum = new LinkedList<Integer>();
	//Contient l'id d'une frontier et la proba associ� � celle-ci
	private ArrayList<Integer> rawProba = new ArrayList<Integer>();
	
	private double longueur = 4.5;
	private double distSecu = 0.5;
	private int dest = 0;
	

	public Frontier(int ID, BasicSimEngine engine, Environment e) {
		super(ID, engine, e);
		rawTime.add(LocalDateTime.of(2017, 1, 1, 0, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 7, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 9, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 17, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 1, 19, 0));
		rawTime.add(LocalDateTime.of(2017, 1, 2, 0, 0));

		rawNum.add(150);
		rawNum.add(100);
		rawNum.add(100);
		rawNum.add(100);
		rawNum.add(100);
		
		/*addProba(5);
		addProba(50);
		addProba(0);
		addProba(25);
		addProba(20);	*/	


		generation();
	}

	public void generation(){
		/*
		 * La variable aleaGen indique si la génération est
		 * déterministe (false) ou aléatoire (true)
		 */
		boolean aleaGen = false;
		
		LocalDateTime beginSim = engine.getCurrentTime();
		LocalDateTime endSim = engine.getCurrentTime().plus(engine.getSimuDuration());
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
		
		while(l.isBefore(endSim)){
			//Generation des voitures pour chaque plage pour un jour
			
			for(int i = numPlage;i<rawTime.size()-1;i++){
				d = Duration.between(rawTime.get(i), rawTime.get(i+1));
				if(rawNum.get(i)!=0) {
					d = d.dividedBy(rawNum.get(i));	
				} else {
					d=Duration.ZERO;
				}
				
				if (!aleaGen) {
					/* Génération déterministe des voitures */
					l = rawTime.get(i);
					for(int j = 0; j<rawNum.get(i); j++){
						l = l.plus(d);
						if(l.isAfter(beginSim) && l.isBefore(endSim)) {
							engine.scheduleEventAt(this, l, this::genCar);
						}
					}
				} else {
					/* Génération aléatoire des voitures */
					long ll;
					for (int j = 0; j < rawNum.get(i); j++) {
						l = rawTime.get(i);
						ll = nextLong(0, Duration.between(rawTime.get(i), rawTime.get(i+1)).getSeconds());
						if(l.plus(Duration.ofSeconds(ll)).isBefore(endSim))
							engine.scheduleEventAt(this, l.plus(Duration.ofSeconds(ll)), this::genCar);
					}
				}
			}
			
			//decalage des plages horaires pour le jour suivant
			for(int k = 0;k<rawTime.size();k++){
				rawTime.set(k, rawTime.get(k).plusDays(1));
			}
			numPlage = 0;
		}

	}
	
	long nextLong(long origin, long bound) {
		Random alea = new Random();
		long r = alea.nextLong();
		long n = bound - origin, m = n - 1;
		if ((n & m) == 0L) // power of two
			r = (r & m) + origin;
		else if (n > 0L) { // reject over-represented candidates
			for (long u = r >>> 1; // ensure nonnegative
					u + m - (r = u % n) < 0L; // rejection check
					u = alea.nextLong() >>> 1) // retry
				;
			r += origin;
		} else { // range not representable as long
			while (r < origin || r >= bound)
				r = alea.nextLong();
		}
		return r;
	}

	private void genCar(ISimEngine engine){
		//generation de la destination
		Random rand = new Random();
		int d =rand.nextInt(100);
		
		for(int i = 0;i<rawProba.size();i++){
			if(d < rawProba.get(i)){
				dest = i+1;
				break;
			}
		}
		
		//generation de la voiture
		new Car(IDGenerator.getSelf().getNext(), this.engine, env, longueur, distSecu, this,(Frontier)env.getNode(dest));
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
			engine.log(this, ">>>>>>>>>>>>>>Probl�me d'ajout de line pour Frontier");
	}
	/**
	 * Proba a ajouter dans l'ordre en tenant compte de la proba 0 d'allersur soi-meme
	 * @param p
	 */
	public void addProba(int p){
		if(rawProba.isEmpty())
			rawProba.add(p);
		else
			rawProba.add(rawProba.get(rawProba.size()-1)+p);
	}

}
