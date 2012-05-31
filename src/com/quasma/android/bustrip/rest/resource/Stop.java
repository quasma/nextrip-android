package com.quasma.android.bustrip.rest.resource;

import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.content.ContentValues;

public class Stop
{
	private String route;
	private String direction;
	private String desc;
	private String stop;
	
	public Stop(String route, String direction, String stop, String desc)
	{
		this.route = route;
		this.direction = direction;
		this.desc = desc;
		this.stop = stop;
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
		rowData.put(StopTable.ROUTE, this.route);
		rowData.put(StopTable.DIRECTION, this.direction);
		rowData.put(StopTable.STOP, this.stop);
		rowData.put(StopTable.DESC, this.desc);
		rowData.put(StopTable.CREATED, time);
		return rowData;
	}
	
}
