package com.quasma.android.bustrip.rest;

import java.net.URI;
import java.net.URL;

import com.quasma.android.bustrip.rest.resource.RouteList;

import android.content.Context;

public class RouteRestMethod extends AbstractRestMethod<RouteList>
{
	private Context context;
	private URI uri;
	
	private static final String ROUTE_URI = "/NexTrip/Routes";
	
	public RouteRestMethod(Context context)
	{
		this.context = context.getApplicationContext();
		this.uri = buildURI(ROUTE_URI);
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(uri);
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
