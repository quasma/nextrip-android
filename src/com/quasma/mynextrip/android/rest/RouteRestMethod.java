package com.quasma.mynextrip.android.rest;

import java.net.URI;
import java.net.URL;

import com.quasma.mynextrip.android.rest.resource.RouteList;

import android.content.Context;

public class RouteRestMethod extends AbstractRestMethod<RouteList>
{
	private Context context;
	
	private static final URI ROUTE_URI = URI.create("http://metrotransit.org/Mobile/NexTripText.aspx");
	
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
	protected RouteList parseResponseBody(URL base, String responseBody) throws Exception 
	{
		return new RouteList(base, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
