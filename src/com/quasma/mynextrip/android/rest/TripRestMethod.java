package com.quasma.mynextrip.android.rest;

import java.net.URI;
import java.net.URL;

import com.quasma.mynextrip.android.rest.resource.TripList;

import android.content.Context;

public class TripRestMethod extends AbstractRestMethod<TripList>
{
	private Context context;
	private String uri;
	
	public TripRestMethod(Context context, String uri)
	{
		this.context = context.getApplicationContext();
		this.uri = uri;
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(URI.create(uri));
		return request;
	}

	@Override
	protected TripList parseResponseBody(URL base, String responseBody) throws Exception 
	{
		return new TripList(base, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
