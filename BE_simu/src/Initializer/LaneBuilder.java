package Initializer;

import org.json.JSONArray;

import config.DataManager;
import engine.impl.BasicSimEngine;
import entity.Environment;
import entity.Line;

public class LaneBuilder {
	
	public LaneBuilder(DataManager bdd, BasicSimEngine engine, Environment env)
	{
		JSONArray jsonArray = (JSONArray)bdd.getLanes(); 
		if (jsonArray != null) { 
			int len = jsonArray.length();
		   	for (int i=0;i<len;i++){ 
		   		Line l=new Line((Integer) bdd.getLaneItem("id", i),engine, env,(Integer)bdd.getLaneItem("longueur", i),env.getNode((Integer) bdd.getLaneItem("begin", i)),env.getNode((Integer) bdd.getLaneItem("end", i)));
		   		env.addLine(l);
		   	} 
		} 
	}
}
