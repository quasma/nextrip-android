package com.quasma.android.bustrip.rest;

import java.net.URI;

import com.quasma.android.bustrip.rest.resource.StopTripList;

import android.content.Context;

public class StopNumberRestMethod extends AbstractRestMethod<StopTripList>
{
	private static String STOP_ARG = "{STOPID}";
	private static String STOP_URI = "/NexTrip/" + STOP_ARG;

	private Context context;
	private URI uri;
	private String stopNumber;
	
	public StopNumberRestMethod(Context context, String stopnumber)
	{
		this.context = context.getApplicationContext();
		this.stopNumber = stopnumber;
		this.uri = buildURI(STOP_URI.replace(STOP_ARG, stopnumber));
	}

	@Override
	protected Request buildRequest() 
	{	
		Request request = new Request(uri);
		return request;
	}

	@Override
	protected StopTripList parseResponseBody(String responseBody) throws Exception 
	{
		return new StopTripList(stopNumber, responseBody);
	}

	@Override
	protected Context getContext() 
	{
		return context;
	}
}
