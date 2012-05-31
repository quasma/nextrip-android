package com.quasma.android.bustrip.rest;

import com.quasma.android.bustrip.rest.resource.Resource;

public class RestMethodResult<T extends Resource> 
{	
	private int statusCode = 0;
	private String statusMsg;
	private T resource;
	private long time;
	
	public RestMethodResult(int statusCode, String statusMsg, T resource) 
	{
		super();
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
		this.resource = resource;
		this.time = System.currentTimeMillis();
	}

	public int getStatusCode() 
	{
		return statusCode;
	}

	public String getStatusMsg() 
	{
		return statusMsg;
	}

	public T getResource() 
	{
		return resource;
	}
	
	public long getTime()
	{
		return time;
	}
}
