package com.quasma.android.bustrip.rest;

import java.net.URI;

import com.quasma.android.bustrip.rest.resource.DirectionList;

import android.content.Context;

public class DirectionRestMethod extends AbstractRestMethod<DirectionList>
{
	private Context context;
	private String route;
	private URI uri;
	
	private static String ROUTE_ARG = "{ROUTE}";
	private static String DIRECTION_URI = "http://svc.metrotransit.org/NexTrip/Directions/" + ROUTE_ARG + "?format=json";

	public DirectionRestMethod(Context context, String route)
	{
		this.context = context.getApplicationContext();
		this.route = route;
		this.uri = URI.create(DIRECTION_URI.replace("{ROUTE}", route));
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(uri);
		return request;
	}

	@Override
	protected DirectionList parseResponseBody(String responseBody) throws Exception 
	{
		return new DirectionList(route, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
