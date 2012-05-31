package com.quasma.android.bustrip.rest.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;

import android.net.Uri;

public class RouteList implements Resource
{
	public static final Uri CONTENT_URI = RouteTable.CONTENT_URI;

	private List<Route> routes;
	
	public RouteList(String json) throws JSONException
	{
		JSONArray jsonArray = new JSONArray(json);
		routes = new ArrayList<Route>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++)
		{
			JSONObject obj = jsonArray.getJSONObject(i);
			routes.add(new Route(obj));
		}
	}
	
	public List<Route> getRoutes() 
	{
		return routes;
	}
}
