package container;

import java.util.LinkedList;

import entity.Car;
import entity.Line;

public class CrossPathContainer {
	private LinkedList<Car> crossPath;
	private LinkedList<Line> toObserve;
	
	public CrossPathContainer()
	{
		crossPath = new LinkedList<>();
		toObserve = new LinkedList<>();
	}
	
	public void addCar(Car...cars)
	{
		for(Car c:cars)
		{
			crossPath.add(c);
		}
	}
	
	public void addLine(Line...lines)
	{
		for(Line l:lines)
		{
			toObserve.add(l);
		}
	}

}
