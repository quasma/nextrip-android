package com.quasma.mynextrip.android.rest;

import java.net.URI;
import java.net.URL;

import com.quasma.mynextrip.android.rest.resource.DirectionList;

import android.content.Context;

public class DirectionRestMethod extends AbstractRestMethod<DirectionList>
{
	private Context context;
	private String uri;
	private String route;
	
	public DirectionRestMethod(Context context, String uri, String route)
	{
		this.context = context.getApplicationContext();
		this.uri = uri;
		this.route = route;
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(URI.create(uri));
		return request;
	}

	@Override
	protected DirectionList parseResponseBody(URL base, String responseBody) throws Exception 
	{
		return new DirectionList(base, route, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
