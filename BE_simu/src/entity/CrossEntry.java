package entity;

public class CrossEntry {

	private final int entry;
	private final int exit;
	private final int type;
	
	public CrossEntry(int entry, int exit, int type){
		this.entry = entry;
		this.exit = exit;
		this.type = type;
	}

	public int in() {
		return entry;
	}

	public int out() {
		return exit;
	}

	public int getType() {
		return type;
	}
	
	
}
