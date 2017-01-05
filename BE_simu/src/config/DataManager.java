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
	
	public int getFreq(int idFront, int idHor) {
		/* idHor : 0h-7h -> 0
		 * 		   7h-9h -> 1
		 * 		   9h-17h -> 2
		 * 		   17h-19h -> 3
		 * 		   19h-0h -> 4
		 */
		
		return bdd.getJSONArray("frequence").getJSONArray(idFront-1).getInt(idHor);
	}
	
	public int getAttract(int idSource, int idCible) {
		return bdd.getJSONArray("attrait").getJSONArray(idSource-1).getInt(idCible-1);
	}
}
