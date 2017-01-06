package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import Initializer.CrossBuilder;
import Initializer.FrontierBuilder;
import Initializer.LaneBuilder;
import algo.algo.PathFinder;
import config.DataManager;
import config.Prop;
import engine.impl.BasicSimEngine;
import logger.impl.SysOutLogger;

public class Monitor {

	
	
	public static void main(String [] args) {
		final Random rand = new Random(LocalDateTime.now().getNano());
		
		BasicSimEngine engine = new BasicSimEngine();
		final LocalDateTime startTime = LocalDateTime.of(2017, 1, 1, 0, 0);
		final Duration duration = Duration.ofHours(36);
		
		
		engine.getLoggerHub().addLogger(new SysOutLogger());
		engine.initialize(startTime,duration);
		
		DataManager bdd = new DataManager();
		
		Environment env = new Environment();
		FrontierBuilder fb= new FrontierBuilder(bdd,env,engine);
		CrossBuilder cb= new CrossBuilder(bdd,env,engine);
		LaneBuilder lb= new LaneBuilder(bdd,engine, env);
		
		initPathFinder(env);
		
		engine.processEventsUntil(startTime.plus(duration));
		engine.getLoggerHub().terminate();
		
		//Fermeture du fichier de conf
		Prop.self.close();
		
	}
	
	
	private static void initPathFinder(Environment env){
		PathFinder path = env.getPathFinder();
		env.getNodes().forEach(
				(id,n) -> path.addLocation(String.valueOf(n.getID()))
				);
		env.getLines().forEach(
				(id,l) -> path.addLane(String.valueOf(id), l.getBegin().getID(), l.getEnd().getID(), l.getLongueur())
				);
		//construction du graph
		path.build();
		//on affecte le pathFinder a chaque frontier
		/*env.getNodes().forEach(
				(id,n) -> {if(n instanceof Frontier){
					((Frontier) n).setPathFinder(path);
				}}
				);*/
	}
	
}
