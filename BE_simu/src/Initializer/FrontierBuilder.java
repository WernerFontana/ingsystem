package Initializer;

import org.json.JSONArray;

import config.DataManager;
import entity.Environment;
import entity.Frontier;
import entity.Line;

public class FrontierBuilder {
		
		public FrontierBuilder(DataManager bdd,Environment env)
		{
			JSONArray jsonArray = (JSONArray)bdd.getLanes(); 
			if (jsonArray != null) { 
			   int len = jsonArray.length();
			   for (int i=0;i<len;i++){ 
			  env.addNode(new Frontier((Integer) bdd.getLaneItem("id", i)));
			   } 
			} 
		}
}
