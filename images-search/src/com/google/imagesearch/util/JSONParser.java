package com.google.imagesearch.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.imagesearch.model.Image;

public class JSONParser {

	public List<Image> parse(String json) {
		List<Image> images = new ArrayList<Image>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONObject responseObj = jsonObj.getJSONObject("responseData");
			JSONArray results = responseObj.getJSONArray("results");

			for (int i = 0; i < results.length(); i++) {
				Image image = new Image();
				image.setTitle(results.getJSONObject(i).getString(
						"titleNoFormatting"));
				image.setWidth(results.getJSONObject(i).getString("width"));
				image.setHeight(results.getJSONObject(i).getString("height"));
				image.setUrl(results.getJSONObject(i).getString("url"));
				images.add(image);
			}
			return images;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
