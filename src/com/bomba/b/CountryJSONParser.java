package com.bomba.b;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bomba.R;

import android.util.Log;

/** A class to parse json data */
public class CountryJSONParser {
	
	// Receives a JSONObject and returns a list
	public List<HashMap<String,Object>> parse(JSONObject jObject){		
		
		JSONArray jCountries = null;
		try {		
			// Retrieves all the elements in the 'countries' array 
			jCountries = jObject.getJSONArray("PAYLOAD");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		 // Invoking getCountries with the array of json object
		 // where each json object represent a country
		return getCountries(jCountries);
	}
	
	
	private List<HashMap<String, Object>> getCountries(JSONArray jCountries){
		int countryCount = jCountries.length();
		List<HashMap<String, Object>> countryList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> country = null;	

		// Taking each country, parses and adds to list object 
		for(int i=0; i<countryCount;i++){
			try {
				// Call getCountry with country JSON object to parse the country 
				country = getCountry((JSONObject)jCountries.get(i));
				//Log.v("COUNTRYJSONPARSER",((JSONObject)jCountries.get(i)).toString() );
				countryList.add(country);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return countryList;
	}
	
	// Parsing the Country JSON object 
	private HashMap<String, Object> getCountry(JSONObject jCountry){

		HashMap<String, Object> country = new HashMap<String, Object>();
		String countryName = "";
		String flag="";
		String language = "";
		String capital = "";
				
		
		try {
			countryName = jCountry.getString("image_url");
			flag = jCountry.getString("image_url");
			language = jCountry.getString("image_url");
			capital = jCountry.getString("image_url");
			
			
			String details =        "Language : " + language + "\n" +
                    "Capital : " + capital;
			
			country.put("country", countryName);
			country.put("flag", R.drawable.ic_launcher);
			country.put("flag_path", "http://semasoftltd.com/artist_images/"+flag);
			country.put("details", details);
			
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return country;
	}
}