package com.quasma.mynextrip.android.rest.resource;

import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;

import android.content.ContentValues;

public class Direction
{
	private String route;
	private String direction;
	private String desc;
	private String url;
	
	public Direction(String route, String direction, String desc, String url)
	{
		this.route = route;
		this.direction = direction;
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
		rowData.put(DirectionTable.ROUTE, this.route);
		rowData.put(DirectionTable.DIRECTION, this.direction);
		rowData.put(DirectionTable.DESC, this.desc);
		rowData.put(DirectionTable.URL, this.url);
		rowData.put(DirectionTable.CREATED, time);
		return rowData;
	}
}
