package com.quasma.android.bustrip.rest;

import java.net.URI;

import com.quasma.android.bustrip.rest.resource.StopList;

import android.content.Context;

public class StopRestMethod extends AbstractRestMethod<StopList>
{
	private static String ROUTE_ARG = "{ROUTE}";
	private static String DIRECTION_ARG = "{DIRECTION}";
	private static String STOP_URI = "/NexTrip/Stops/" + ROUTE_ARG + "/" + DIRECTION_ARG;

	private Context context;
	private URI uri;
	private String route;
	private String direction;
	
	public StopRestMethod(Context context, String route, String direction)
	{
		this.context = context.getApplicationContext();
		this.uri = buildURI(STOP_URI.replace(ROUTE_ARG, route).replace(DIRECTION_ARG, direction));;
		this.route = route;
		this.direction = direction;
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(uri);
		return request;
	}

	@Override
	protected StopList parseResponseBody(String responseBody) throws Exception 
	{
		return new StopList(route, direction, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
