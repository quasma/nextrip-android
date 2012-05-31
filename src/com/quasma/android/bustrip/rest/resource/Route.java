package com.quasma.android.bustrip.rest.resource;

import org.json.JSONException;
import org.json.JSONObject;

import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;

import android.content.ContentValues;

public class Route
{
	private String route;
	private String desc;

	public Route(JSONObject obj) throws JSONException
	{
		desc = obj.getString("Description");
		route = obj.getString("Route");
	}
	
	public String getRoute()
	{
		return route;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public ContentValues toContentValues(long time) 
	{
		ContentValues rowData = new ContentValues();
		rowData.put(RouteTable.ROUTE, this.route);
		rowData.put(RouteTable.DESC, this.desc);
		rowData.put(RouteTable.CREATED, time);
		return rowData;
	}
}
