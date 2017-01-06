package algo.algo;

import java.util.LinkedList;
import java.util.Random;

import algo.model.IVertex;

public class Voiture {
	private LaneNode source,dest,currentLocation;
	private int id;
	private double nomSpeed;
	private String name;
	private LinkedList<IVertex> path;
	
	public Voiture(LaneNode source,LaneNode dest, int id,LinkedList<IVertex> path)
	{
		setSource(source);
		setDest(dest);
		setId(id);
		setCurrentLocation(source);
		setPath(path);
		setName("Voiture_"+id);
		Random rnd= new Random();
		setNomSpeed(rnd.nextDouble()*20+40);
	}

	public LaneNode getDest() {
		return dest;
	}

	public void setDest(LaneNode dest) {
		this.dest = dest;
	}

	public LaneNode getSource() {
		return source;
	}

	public void setSource(LaneNode source) {
		this.source = source;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNomSpeed() {
		return nomSpeed;
	}

	public void setNomSpeed(double nomSpeed) {
		this.nomSpeed = nomSpeed;
	}

	public LaneNode getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(LaneNode currentLocation) {
		this.currentLocation = currentLocation;
	}

	public LinkedList<IVertex> getPath() {
		return path;
	}

	public void setPath(LinkedList<IVertex> path) {
		this.path = path;
	}
}
