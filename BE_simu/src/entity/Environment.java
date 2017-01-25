package entity;

import java.time.Duration;
import java.util.HashMap;

import algo.algo.PathFinder;
import utils.SortedList;

public class Environment {
	private HashMap<Integer,Line> lineList;
	private HashMap<Integer,Node> nodeList;

	private int nbCarGen = 0;
	private int nbCarEnd = 0;
	private Duration minTripTime, moyTripTime, maxTripTime;
	private PathFinder path;
	public SortedList<Duration> tripTimeList;

	public Environment()
	{
		tripTimeList = new SortedList<>();
		tripTimeList.add(Duration.ofHours(15));
		tripTimeList.add(Duration.ofHours(51));
		tripTimeList.add(Duration.ofHours(5));

		lineList =new HashMap<>();
		nodeList = new HashMap<>();

		path = new PathFinder();
	}

	public void addLine(Line line)
	{
		lineList.put(line.getID(), line);
	}

	public void addNode (Node node)
	{
		nodeList.put(node.getID(), node);
	}
	public HashMap<Integer,Line> getLines(){
		return lineList;
	}
	public HashMap<Integer,Node> getNodes(){
		return nodeList;
	}

	public Line getLine(int id)
	{
		return lineList.get(id);
	}

	public Node getNode(int id)
	{
		return nodeList.get(id);
	}

	public PathFinder getPathFinder(){
		return path;
	}

	public void carGen() {
		this.nbCarGen++;
	}

	public void carEnd() {
		this.nbCarEnd++;
	}

	public int getNbCarGen() {
		return nbCarGen;
	}

	public int getNbCarEnd() {
		return nbCarEnd;
	}

	public void stats()
	{
		System.out.println(mean());
	}

	public Duration mean()
	{
		moyTripTime=Duration.ZERO;
		tripTimeList.getList().forEach((d)->moyTripTime=moyTripTime.plus((Duration) d));

		moyTripTime=moyTripTime.dividedBy(tripTimeList.size());
		return 	moyTripTime;
	}



}
