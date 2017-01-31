package engine.impl;

import entity.Car;
import entity.Cross;
import entity.Environment;
import entity.Line;
import entity.Node;

public class Checker {
	
	private Environment env;
	private BasicSimEngine engine;
	
	public Checker(BasicSimEngine engine, Environment e){
		this.env = e;
		this.engine = engine;
	}
	
	public boolean check(){
		System.out.println("end/gen : "+env.getNbCarEnd()+"/"+env.getNbCarGen());
		boolean r=false;
		
		boolean b = false;
		for(Line l : env.getLines().values()){
			if(!l.getCars().isEmpty()){
				//System.out.println("Car in Line : KO");
				//System.out.println(l+"            "+l.getCars().size());
				b = true;
				r = true;
			}
		}
		if(!b)
			//System.out.println("Car in Line : OK");
		
		b = false;
		for(Node n : env.getNodes().values()){
			if(n instanceof Cross){
				for(Car c : ((Cross) n).getIsOccupied()){
					if(c != null){
						b = true;
						break;
					}
				}
			}
		}
		if(!b){
			//System.out.println("Car in Cross : OK");
		}
		else{
			//System.out.println("Car in Cross : KO");
			r = true;
		}
		if(engine.getEventQueue().isEmpty()){
			//System.out.println("Event to process : OK");
		}
		else
		{
		//System.out.println("Event to process : KO");
		r=true;
		}
		return r;
	}

}
