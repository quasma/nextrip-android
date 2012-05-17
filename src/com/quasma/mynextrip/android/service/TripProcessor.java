package com.quasma.mynextrip.android.service;

import com.quasma.mynextrip.android.rest.RestMethodResult;
import com.quasma.mynextrip.android.rest.TripRestMethod;
import com.quasma.mynextrip.android.rest.resource.TripList;

import android.content.Context;
import android.os.Bundle;

public class TripProcessor
{
	protected static final String TAG = TripProcessor.class.getSimpleName();

	private Context context;
	private String uri;

	public TripProcessor(Context context, String uri) 
	{
		this.context = context;
		this.uri = uri;
	}

	
	void getTrips(ProcessorCallback callback) 
	{
		final RestMethodResult<TripList> result = new TripRestMethod(context, uri).execute();
		Bundle bundle = new Bundle();
		if (result.getStatusCode() < 300)
			bundle.putParcelable(NexTripService.RESOURCE_DATA_EXTRA, result.getResource());

		bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, result.getStatusMsg());
		callback.send(result.getStatusCode(), bundle);
	}
}
