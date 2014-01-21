package com.ynachon.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2169169108390497114L;
	private String fullUrl;
	private String thumbUrl;
	
	public String getFullUrl() {
		return fullUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}
	
	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumbUrl = null;
		}
	}
	
	public String toString() {
		return this.thumbUrl;
	}
	
	public static ArrayList<ImageResult> fromJSONArray(JSONArray imagesArray) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < imagesArray.length(); i++) {
			try {
				results.add(new ImageResult(imagesArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
}
