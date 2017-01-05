package config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DataManager {
	
	private JSONObject bdd;
	
	public DataManager(){
		try {
			bdd = new JSONObject(new JSONTokener(new BufferedReader(new FileReader("bdd.json"))));
		} catch (JSONException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public JSONArray getLanes(){
		return bdd.getJSONArray("line");
	}
	
	public Object getLaneItem(String type, int id)
	{
		return ((JSONObject) getLanes().get(id)).get(type);
	}
	
	public JSONArray getFrontier(){
		return bdd.getJSONArray("frontier");
	}
}
