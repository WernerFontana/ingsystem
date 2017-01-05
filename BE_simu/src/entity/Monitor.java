package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

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
		
		Environment env = new Environment();
		
		env.addNode(new Cross(true,8));
		env.addNode(new Cross(true,9));
		env.addNode(new Cross(true,10));
		env.addNode(new Cross(true,11));
		env.addNode(new Frontier(1));
		env.addNode(new Frontier(2));
		env.addNode(new Frontier(3));
		env.addNode(new Frontier(4));
		env.addNode(new Frontier(5));
		env.addNode(new Frontier(6));
		env.addNode(new Frontier(7));
		
		for(Lane lane : Lane.values()){
			env.addLine(new Line(lane.getID(),lane.getLongueur(),env.getNode(lane.getBegin()),env.getNode(lane.getEnd())));
		}
		
		
		
		final LocalDateTime startTime = LocalDateTime.of(0, 1, 1, 0, 0);
		final Duration duration = Duration.ofHours(6);
		
		BasicSimEngine engine = new BasicSimEngine();
		engine.getLoggerHub().addLogger(new SysOutLogger());
		engine.initialize(startTime);
		
		engine.processEventsUntil(startTime.plus(duration));
		engine.getLoggerHub().terminate();
		
		//Fermeture du fichier de conf
		Prop.self.close();
		
	}
	
}
