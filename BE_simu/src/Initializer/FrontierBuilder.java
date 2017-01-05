package Initializer;

import org.json.JSONArray;

import config.DataManager;
import engine.impl.BasicSimEngine;
import entity.Environment;
import entity.Frontier;
import entity.Line;

public class FrontierBuilder {
		
		public FrontierBuilder(DataManager bdd,Environment env,BasicSimEngine engine)
		{
			JSONArray jsonArray = (JSONArray)bdd.getLanes(); 
			if (jsonArray != null) { 
			   int len = jsonArray.length();
			   //for (int i=0;i<len;i++){ 
			   for (int i=0;i<1;i++){ 
			  env.addNode(new Frontier(engine,(Integer) bdd.getLaneItem("id", i)));
			   } 
			} 
		}
}
