package Initializer;

import org.json.JSONArray;

import config.DataManager;
import entity.Cross;
import entity.Environment;
import entity.Line;

public class CrossBuilder {
	public CrossBuilder(DataManager bdd,Environment env)
	{
		JSONArray jsonArray = (JSONArray)bdd.getLanes(); 
		if (jsonArray != null) { 
		   int len = jsonArray.length();
		   for (int i=0;i<len;i++){ 
		  env.addNode(new Cross(true,(Integer) bdd.getLaneItem("id", i)));
		  } 
		} 
	}
}
