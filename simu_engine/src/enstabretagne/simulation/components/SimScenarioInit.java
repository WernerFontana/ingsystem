package enstabretagne.simulation.components;

public class SimScenarioInit extends SimInitParameters  {
	
	private long seed;
	private long repliqueNum;
	
	
	public long getSeed() {
		return seed;
	}



	public SimScenarioInit(long seed) {
		this(seed,0);
	}



	public long getRepliqueNum() {
		return repliqueNum;
	}



	public SimScenarioInit(long seed, long repliqueNum) {
		super();
		this.seed = seed;
		this.repliqueNum = repliqueNum;
	}
	
	
	

}
