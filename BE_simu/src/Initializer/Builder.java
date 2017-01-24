package Initializer;

import org.json.JSONArray;
import org.json.JSONObject;

import algo.algo.PathFinder;
import config.DataManager;
import engine.impl.BasicSimEngine;
import entity.Cross;
import entity.CrossEntry;
import entity.Environment;
import entity.Frontier;
import entity.Line;

public class Builder {
	
	private DataManager bdd;
	private BasicSimEngine engine;
	private Environment env;

	public Builder(DataManager bdd, Environment env, BasicSimEngine engine){
		this.bdd = bdd;
		this.engine = engine;
		this.env = env;
	}
	
	public void build(){
		JSONArray jsonArray;
		int len;
		
		System.out.println("Construction des Frontier ...");
		jsonArray = bdd.getFrontiers(); 
		if (jsonArray != null) { 
		   len = jsonArray.length();
		   for (int i=0;i<len;i++){ 
			   Frontier f = new Frontier(((JSONObject)jsonArray.get(i)).getInt("id"),engine,env);
			   //donne sur l'attractivite de chaque frontier
			   bdd.getAttract(f.getID()).forEach((p) -> f.addProba((Integer)p));
			   
			   env.addNode(f);
		   } 
		} 
		
		System.out.println("Construction des Cross ...");
		jsonArray = (JSONArray)bdd.getCrosses(); 
		if (jsonArray != null) { 
			len = jsonArray.length();
			for (int i=0;i<len;i++){
				CrossEntry top = new CrossEntry(((JSONObject)jsonArray.get(i)).getInt("topIn"), ((JSONObject)jsonArray.get(i)).getInt("topOut"), ((JSONObject)jsonArray.get(i)).getInt("top"));
				CrossEntry right = new CrossEntry(((JSONObject)jsonArray.get(i)).getInt("rightIn"), ((JSONObject)jsonArray.get(i)).getInt("rightOut"), ((JSONObject)jsonArray.get(i)).getInt("right"));
				CrossEntry bottom = new CrossEntry(((JSONObject)jsonArray.get(i)).getInt("bottomIn"), ((JSONObject)jsonArray.get(i)).getInt("bottomOut"), ((JSONObject)jsonArray.get(i)).getInt("bottom"));
				CrossEntry left = new CrossEntry(((JSONObject)jsonArray.get(i)).getInt("leftIn"), ((JSONObject)jsonArray.get(i)).getInt("leftOut"), ((JSONObject)jsonArray.get(i)).getInt("left"));
				
				env.addNode(new Cross(((JSONObject)jsonArray.get(i)).getInt("id"),engine,env,top,right,bottom,left));
			} 
		} 
		
		System.out.println("Construction des Line ...");
		jsonArray = (JSONArray)bdd.getLanes(); 
		if (jsonArray != null) { 
			len = jsonArray.length();
		   	for (int i=0;i<len;i++){ 
		   		Line l=new Line((Integer) bdd.getLaneItem("id", i),engine, env,(Integer)bdd.getLaneItem("longueur", i),env.getNode((Integer) bdd.getLaneItem("begin", i)),env.getNode((Integer) bdd.getLaneItem("end", i)));
		   		//ajout de la line au frontier
		   		if(l.getBegin() instanceof Frontier)
		   			((Frontier)l.getBegin()).addLine(l);
		   		
		   		env.addLine(l);
		   	} 
		} 
		
		System.out.println("Construction du PathFinder ...");
		PathFinder path = env.getPathFinder();
		env.getNodes().forEach(
				(id,n) -> path.addLocation(String.valueOf(n.getID()))
				);
		env.getLines().forEach(
				(id,l) -> path.addLane(String.valueOf(id), l.getBegin().getID(), l.getEnd().getID(), l.getLongueur())
				);
		//construction du graph
		path.build();
	}
}
