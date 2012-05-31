package com.quasma.android.bustrip.rest;

import java.net.URI;
import java.net.URL;

import com.quasma.android.bustrip.rest.resource.RouteList;

import android.content.Context;

public class RouteRestMethod extends AbstractRestMethod<RouteList>
{
	private Context context;
	
	private static final URI ROUTE_URI = URI.create("http://svc.metrotransit.org/NexTrip/Routes?format=json");
	
	public RouteRestMethod(Context context)
	{
		this.context = context.getApplicationContext();
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(ROUTE_URI);
		return request;
	}

	@Override
	protected RouteList parseResponseBody(String responseBody) throws Exception 
	{
		return new RouteList(responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
