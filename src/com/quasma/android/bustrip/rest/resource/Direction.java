package com.quasma.android.bustrip.rest.resource;

import com.quasma.android.bustrip.providers.DirectionProviderContract.DirectionTable;

import android.content.ContentValues;

public class Direction
{
	private String route;
	private String direction;
	private String desc;
	
	public Direction(String route, String direction, String desc)
	{
		this.route = route;
		this.direction = direction;
		this.desc = desc.substring(0,  1).toUpperCase() + desc.substring(1).toLowerCase();
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
		rowData.put(DirectionTable.ROUTE, this.route);
		rowData.put(DirectionTable.DIRECTION, this.direction);
		rowData.put(DirectionTable.DESC, this.desc);
		rowData.put(DirectionTable.CREATED, time);
		return rowData;
	}
}
