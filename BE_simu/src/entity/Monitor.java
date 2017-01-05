package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import Initializer.CrossBuilder;
import Initializer.FrontierBuilder;
import Initializer.LaneBuilder;
import config.DataManager;
import config.Prop;
import data.Lane;
import engine.impl.BasicSimEngine;
import logger.impl.SysOutLogger;

public class Monitor {

	
	
	public static void main(String [] args) {
		final Random rand = new Random(LocalDateTime.now().getNano());
		
		/*PingEntity ping = new PingEntity("Alice");
		ping.setLatencySupplier(() -> Duration.ofMinutes(15));
		PingEntity pong = new PingEntity("Bob");
		pong.setLatencySupplier(() -> Duration.ofMinutes(5 + rand.nextInt(25)));
		ping.setTarget(pong);
		pong.setTarget(ping);
		
		final LocalDateTime startTime = LocalDateTime.of(0, 1, 1, 0, 0);
		final Duration duration = Duration.ofHours(6);
		
		BasicSimEngine engine = new BasicSimEngine();
		engine.getLoggerHub().addLogger(new SysOutLogger());
		engine.initialize(startTime);
		ping.sendPing(engine);
		
		engine.processEventsUntil(startTime.plus(duration));
		engine.getLoggerHub().terminate();*/
		
		BasicSimEngine engine = new BasicSimEngine();
		final LocalDateTime startTime = LocalDateTime.of(2017, 1, 1, 1, 0);
		final Duration duration = Duration.ofHours(6);
		
		
		engine.getLoggerHub().addLogger(new SysOutLogger());
		engine.initialize(startTime);
		
		DataManager bdd = new DataManager();
		
		Environment env = new Environment();
		FrontierBuilder fb= new FrontierBuilder(bdd,env,engine);
		CrossBuilder cb= new CrossBuilder(bdd,env,engine);
		LaneBuilder lb= new LaneBuilder(bdd,env);
		
		env.addNode(new Cross(engine,true,8));
		env.addNode(new Cross(engine,true,9));
		env.addNode(new Cross(engine,true,10));
		env.addNode(new Cross(engine,true,11));
		env.addNode(new Frontier(engine,1));
		env.addNode(new Frontier(engine,2));
		env.addNode(new Frontier(engine,3));
		env.addNode(new Frontier(engine,4));
		env.addNode(new Frontier(engine,5));
		env.addNode(new Frontier(engine,6));
		env.addNode(new Frontier(engine,7));
		
		for(Lane lane : Lane.values()){
			env.addLine(new Line(lane.getID(),lane.getLongueur(),env.getNode(lane.getBegin()),env.getNode(lane.getEnd())));
		}
		
		engine.processEventsUntil(startTime.plus(duration));
		engine.getLoggerHub().terminate();
		
		//Fermeture du fichier de conf
		Prop.self.close();
		
	}
	
}
