package entity;

import java.time.Duration;
import java.util.function.Supplier;

import engine.ISimEngine;
import engine.ISimEntity;

public class PingEntity implements ISimEntity {

	private final String name;
	private Supplier<Duration> latencySupplier;
	private PingEntity target;
	
	public PingEntity(String name) {
		this.name = name;
		latencySupplier = () -> Duration.ofHours(1);
	}
	
	public String getName() {
		return name;
	}
	
	public Supplier<Duration> getLatencySupplier() {
		return latencySupplier;
	}
	
	public void setLatencySupplier(Supplier<Duration> latencySupplier) {
		this.latencySupplier = latencySupplier;
	}
	
	public PingEntity getTarget() {
		return target;
	}
	
	public void setTarget(PingEntity target) {
		this.target = target;
	}
	
	private void log(ISimEngine engine, String message) {
		engine.getLoggerHub().setSpace(this.getClass().getPackage().toString());
		engine.getLoggerHub().setTime(engine.getCurrentTime());
		engine.getLoggerHub().setSource(name);
		engine.getLoggerHub().setComment(message);
		engine.getLoggerHub().log();
		engine.getLoggerHub().clear();
	}
	
	public void receivePing(ISimEngine engine) {
		final Duration latency = latencySupplier.get();
		engine.getLoggerHub().addValue("latency", latency.toString());
		log(engine, "receive");
		engine.scheduleEventIn(this, latency, this::sendPing);
	}
	
	public void sendPing(ISimEngine engine) {
		log(engine, "send");
		target.receivePing(engine);
	}
	
}
