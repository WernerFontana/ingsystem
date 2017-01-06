package Initializer;

import org.json.JSONArray;

import config.DataManager;
import entity.Environment;
import entity.Line;

public class LaneBuilder {
	
	public LaneBuilder(DataManager bdd,Environment env)
	{
		JSONArray jsonArray = (JSONArray)bdd.getLanes(); 
		if (jsonArray != null) { 
			int len = jsonArray.length();
		   	for (int i=0;i<len;i++){ 
		   		Line l=new Line((Integer) bdd.getLaneItem("id", i),(Integer)bdd.getLaneItem("longueur", i),env.getNode((Integer) bdd.getLaneItem("id", i)),env.getNode((Integer) bdd.getLaneItem("id", i)));
		   		env.addLine(l);
		   	} 
		} 
	}
}
