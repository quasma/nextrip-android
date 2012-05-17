package com.quasma.mynextrip.android.rest.resource;

import com.quasma.mynextrip.android.providers.StopProviderContract.StopTable;

import android.content.ContentValues;

public class Stop
{
	private String route;
	private String direction;
	private String desc;
	private String url;
	private String stop;
	private String stopNumber;
	
	public Stop(String route, String direction, String stop, String stopNumber, String desc, String url)
	{
		this.route = route;
		this.direction = direction;
		this.desc = desc.replace("&amp;", "&");;
		this.url = url;
		this.stop = stop.replace("&amp;", "&");;
		this.stopNumber = stopNumber;
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
		rowData.put(StopTable.ROUTE, this.route);
		rowData.put(StopTable.DIRECTION, this.direction);
		rowData.put(StopTable.STOP, this.stop);
		rowData.put(StopTable.STOPNUMBER, this.stopNumber);
		rowData.put(StopTable.DESC, this.desc);
		rowData.put(StopTable.URL, this.url);
		rowData.put(StopTable.CREATED, time);
		return rowData;
	}
}
