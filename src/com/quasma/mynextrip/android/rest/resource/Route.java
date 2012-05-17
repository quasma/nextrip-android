package com.quasma.mynextrip.android.rest.resource;

import com.quasma.mynextrip.android.providers.RouteProviderContract.RouteTable;

import android.content.ContentValues;

public class Route
{
	private String route;
	private String desc;
	private String url;
	
	public Route(String route, String desc, String url)
	{
		this.route = route;
		this.desc = desc;
		this.url = url;
	}
	
	public String getRoute()
	{
		return route;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public ContentValues toContentValues(long time) 
	{
		ContentValues rowData = new ContentValues();
		rowData.put(RouteTable.ROUTE, this.route);
		rowData.put(RouteTable.DESC, this.desc);
		rowData.put(RouteTable.URL, this.url);
		rowData.put(RouteTable.CREATED, time);
		return rowData;
	}
}
