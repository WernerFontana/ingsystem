package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import Initializer.Builder;
import algo.algo.PathFinder;
import config.DataManager;
import config.Prop;
import engine.impl.BasicSimEngine;
import engine.impl.Checker;
import logger.impl.SysOutLogger;

public class Monitor {

	
	
	public static void main(String [] args) {
		final Random rand = new Random(LocalDateTime.now().getNano());
		final LocalDateTime debut = LocalDateTime.now();
		
		BasicSimEngine engine = new BasicSimEngine();
		final LocalDateTime startTime = LocalDateTime.of(2017, 1, 1, 0, 0);
		final Duration duration = Duration.ofHours(24);
		
		
		engine.getLoggerHub().addLogger(new SysOutLogger());
		engine.initialize(startTime,duration);
		
		DataManager bdd = new DataManager();
		
		Environment env = new Environment();
		Builder builder = new Builder(bdd,env,engine);
		builder.build();
		
		
		
		engine.processEventsUntil(startTime.plus(duration));
		engine.getLoggerHub().terminate();
		
		Checker c = new Checker(engine,env);
		c.check();
		
		System.out.println("Date de fin de simulation : "+engine.getCurrentTime());
		
		System.out.println("Dur�e de simulation : "+Duration.between(debut, LocalDateTime.now()));
		
		//env.getLines().forEach((i,l) -> System.out.println(i+" : "+l.getCars()));
		
		/*env.getNodes().forEach((i,n) -> {
			if(n instanceof Cross){
				System.out.println(((Cross)n).countObservers());
				for(int j = 0;j<((Cross)n).getIsOccupied().length;j++){
					System.out.println(((Cross)n).getIsOccupied()[j]);
					
				}
			}
		});*/
		
		//Fermeture du fichier de conf
		Prop.self.close();
		
	}
	
}
