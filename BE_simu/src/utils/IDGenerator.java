package utils;

import java.util.LinkedList;

import config.DataManager;

public class IDGenerator {
	private static IDGenerator self = new IDGenerator();
	private LinkedList<Integer> IDList;
	
	public IDGenerator()
	{
		int carNumber=0;
		DataManager DM= new DataManager();
		for(int i=1;i<7;i++)
		{
			for(int k:DM.getFreq(i))
			{
				carNumber+=k;
			}
		}
		carNumber=(int) (carNumber*1.3);
		carNumber = 10000000;
		IDList = new LinkedList<>();
		for(int i =0;i<carNumber;i++)
		{
			IDList.add(i);
		}
	}
	
	public static IDGenerator getSelf()
	{
		return self;
	}
	
	public int getNext()
	{
		IDList.removeFirst();
		return IDList.getFirst();
	}

}
