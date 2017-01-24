package config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;

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
	
	public JSONArray getFrontiers(){
		return bdd.getJSONArray("frontier");
	}
	
	public JSONArray getCrosses(){
		return bdd.getJSONArray("cross");
	}
	
	public LocalDateTime[] getHours() {
		int k = bdd.getJSONArray("horaires").length();
		LocalDateTime[] hours = new LocalDateTime[k];
		int h;
		for (int i = 0; i < k; i++) {
			h = bdd.getJSONArray("horaires").getInt(i);
			hours[i] = LocalDateTime.of(2017, 1, 1, h, 0);
		}
		
		return hours;
	}
	
	public int[] getFreq(int idFront) {
		int k = bdd.getJSONArray("frequence").getJSONArray(idFront-1).length();
		int[] freq = new int[k];
		for (int i = 0; i < k; i++) {
			freq[i] = bdd.getJSONArray("frequence").getJSONArray(idFront-1).getInt(i);
		}
		
		return freq;
	}
	
	public JSONArray getAttract(int idSource) {
		return bdd.getJSONArray("attrait").getJSONArray(idSource-1);
	}
	
	public boolean getTypeCross(int id) {
		JSONArray arr = bdd.getJSONArray("cross");
		for (int i = 0; i < arr.length(); i++) {
			if (arr.getJSONObject(i).getInt("id") == id) {
				if (arr.getJSONObject(i).getInt("type") == 0) {
					return true;
				}
				
				return false;
			}
		}
		
		return true; // jamais atteint. en théorie.
	}
}
