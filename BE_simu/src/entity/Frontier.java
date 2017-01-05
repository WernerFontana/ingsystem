package entity;

import java.time.LocalDateTime;

import engine.ISimEngine;
import engine.ISimEntity;
import engine.impl.BasicSimEngine;

public class Frontier extends Node implements ISimEntity {
	
	public Frontier(BasicSimEngine engine, int ID) {
		super(engine, ID);
		// TODO Auto-generated constructor stub
		generation();
	}
	
	

	public void generation(){
		LocalDateTime l = LocalDateTime.of(2017, 1, 1, 2, 0);
		engine.scheduleEventAt(this, l, this::genCar);
		
	}
	
	
	public void genCar(ISimEngine engine){
		System.out.println("ok");
		
	}

	

}
