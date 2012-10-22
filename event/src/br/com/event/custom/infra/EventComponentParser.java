package br.com.event.custom.infra;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.event.custom.model.EventComponent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EventComponentParser <T extends EventComponent> {

	private static String EXCEPTION_MSG = "Malformed parse object.";
	
	public T parseObject(String result, TypeToken<T> token) {
		try {
			Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
			String name = token.getRawType().getSimpleName().toLowerCase();
			
			JSONObject jsonObj = new JSONObject(result).getJSONObject(name);
			return gson.fromJson(jsonObj.toString(), token.getType());
		} catch (JSONException e) {
			throw new IllegalArgumentException(EXCEPTION_MSG);
		}
	}
	
	public List<T> parseArray(String result, TypeToken<T> token) {
		List<T> results = new ArrayList<T>();
		
		try {
			String name = token.getRawType().getSimpleName().toLowerCase();
			Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();	
			
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
			    JSONObject row 		= array.getJSONObject(i);
			    JSONObject jsonObj 	= row.getJSONObject(name);
			    T element 			= gson.fromJson(jsonObj.toString(), 
			    									token.getType());
			    results.add(element);
			}
		} catch (JSONException e) {
			throw new IllegalArgumentException(EXCEPTION_MSG);
		}
		return results;
	}

}
