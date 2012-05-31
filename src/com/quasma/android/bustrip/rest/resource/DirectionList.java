package com.quasma.android.bustrip.rest.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.quasma.android.bustrip.providers.DirectionProviderContract.DirectionTable;

import android.net.Uri;

public class DirectionList implements Resource
{
	public static final Uri CONTENT_URI = DirectionTable.CONTENT_URI;

	private List<Direction> directions;
	
	public DirectionList(String route, String html) throws JSONException 
	{
		JSONArray jsonArray = new JSONArray(html);
		directions = new ArrayList<Direction>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++)
		{
			directions.add(new Direction(route,
					jsonArray.getJSONObject(i).getString("Value"), jsonArray.getJSONObject(i).getString("Text")));
		}
	}

	public List<Direction> getDirections() 
	{
		return directions;
	}

}
