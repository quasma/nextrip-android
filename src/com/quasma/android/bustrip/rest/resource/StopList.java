package com.quasma.android.bustrip.rest.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.net.Uri;

public class StopList implements Resource
{
	public static final Uri CONTENT_URI = StopTable.CONTENT_URI;

	private List<Stop> stops;
	
	public StopList(String route, String direction, String html) throws JSONException 
	{
		JSONArray jsonArray = new JSONArray(html);
		stops = new ArrayList<Stop>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++)
			stops.add(new Stop(route, direction, 
					jsonArray.getJSONObject(i).getString("Value"),
					jsonArray.getJSONObject(i).getString("Text")));
	}

	public List<Stop> getStops() 
	{
		return stops;
	}
}
