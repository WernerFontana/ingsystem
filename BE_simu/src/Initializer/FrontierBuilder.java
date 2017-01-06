package Initializer;

import org.json.JSONArray;
import org.json.JSONObject;

import config.DataManager;
import engine.impl.BasicSimEngine;
import entity.Environment;
import entity.Frontier;

public class FrontierBuilder {
		
		public FrontierBuilder(DataManager bdd,Environment env,BasicSimEngine engine)
		{
			JSONArray jsonArray = bdd.getFrontiers(); 
			if (jsonArray != null) { 
			   int len = jsonArray.length();
			   for (int i=0;i<len;i++){ 
				   Frontier f = new Frontier(((JSONObject)jsonArray.get(i)).getInt("id"),engine,env);
				   //donne sur l'attractivite de chque frontier
				   bdd.getAttract(f.getID()).forEach((p) -> f.addProba((Integer)p));
				   
				   env.addNode(f);
			   } 
			} 
		}
}
