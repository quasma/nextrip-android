package com.quasma.android.bustrip.rest;

import java.net.URI;

import com.quasma.android.bustrip.rest.resource.TripList;

import android.content.Context;

public class TripRestMethod extends AbstractRestMethod<TripList>
{
	private static String ROUTE_ARG = "{ROUTE}";
	private static String DIRECTION_ARG = "{DIRECTION}";
	private static String STOP_ARG = "{STOP}";
	
	
	private static String STOP_URI = "/NexTrip/" + ROUTE_ARG + "/" + DIRECTION_ARG + "/" + STOP_ARG;

	private Context context;
	private URI uri;
	private String route;
	private String direction;
	private String stop;
	
	public TripRestMethod(Context context, String route, String direction, String stop)
	{
		this.context = context.getApplicationContext();
		this.uri = buildURI(STOP_URI.replace(ROUTE_ARG, route).replace(DIRECTION_ARG, direction).replace(STOP_ARG, stop));;
		this.route = route;
		this.direction = direction;
		this.stop = stop;
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(uri);
		return request;
	}

	@Override
	protected TripList parseResponseBody(String responseBody) throws Exception 
	{
		return new TripList(route, direction, stop, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
