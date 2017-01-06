package Initializer;

import org.json.JSONArray;
import org.json.JSONObject;

import config.DataManager;
import engine.impl.BasicSimEngine;
import entity.Cross;
import entity.Environment;

public class CrossBuilder {
	public CrossBuilder(DataManager bdd,Environment env, BasicSimEngine engine)
	{
		JSONArray jsonArray = (JSONArray)bdd.getCrosses(); 
		if (jsonArray != null) { 
			int len = jsonArray.length();
			
			for (int i=0;i<len;i++){ 
				env.addNode(new Cross(((JSONObject)jsonArray.get(i)).getInt("id"),engine,env,true));
			} 
		} 
	}
}
