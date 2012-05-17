package com.quasma.mynextrip.android.rest;

import java.net.URI;
import java.net.URL;

import com.quasma.mynextrip.android.rest.resource.StopList;

import android.content.Context;

public class StopRestMethod extends AbstractRestMethod<StopList>
{
	private Context context;
	private String uri;
	private String route;
	private String direction;
	
	public StopRestMethod(Context context, String uri, String route, String direction)
	{
		this.context = context.getApplicationContext();
		this.uri = uri;
		this.route = route;
		this.direction = direction;
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(URI.create(uri));
		return request;
	}

	@Override
	protected StopList parseResponseBody(URL base, String responseBody) throws Exception 
	{
		return new StopList(base, route, direction, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
