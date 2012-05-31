package com.quasma.android.bustrip.service;

import com.quasma.android.bustrip.rest.RestMethodResult;
import com.quasma.android.bustrip.rest.TripRestMethod;
import com.quasma.android.bustrip.rest.resource.TripList;

import android.content.Context;
import android.os.Bundle;

public class TripProcessor
{
	private Context context;
	private String route;
	private String direction;
	private String stop;

	public TripProcessor(Context context, String route, String direction, String stop) 
	{
		this.context = context;
		this.route = route;
		this.direction = direction;
		this.stop = stop;
		
	}

	
	void getTrips(ProcessorCallback callback) 
	{
		final RestMethodResult<TripList> result = new TripRestMethod(context, route, direction, stop).execute();
		Bundle bundle = new Bundle();
		if (result.getStatusCode() < 300)
			bundle.putParcelable(NexTripService.RESOURCE_DATA_EXTRA, result.getResource());

		bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, result.getStatusMsg());
		callback.send(result.getStatusCode(), bundle);
	}
}
