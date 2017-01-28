package utils;

public class IDGenerator {
	private static IDGenerator self = new IDGenerator();
	private int i;
	public IDGenerator()
	{
		i=0;
	}
	
	public static IDGenerator getSelf()
	{
		return self;
	}
	
	public int getNext()
	{
		i++;
		return i;
	}

}
