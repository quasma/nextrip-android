package com.quasma.android.bustrip.service;

import com.quasma.android.bustrip.R;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class NexTripService extends IntentService 
{
	public static final String METHOD_EXTRA = "com.quasma.mynextrip.android.service.METHOD_EXTRA";
	public static final String METHOD_GET = "GET";
	public static final String RESOURCE_TYPE_EXTRA = "com.quasma.mynextrip.android.service.RESOURCE_TYPE_EXTRA";

	public static final int RESOURCE_TYPE_ROUTE = 1;
	public static final int RESOURCE_TYPE_DIRECTION = 2;
	public static final int RESOURCE_TYPE_STOP = 3;
	public static final int RESOURCE_TYPE_TRIP = 4;
	public static final int RESOURCE_TYPE_STOPTRIP = 5;
	
	public static final String RESOURCE_DATA_EXTRA = "com.quasma.mynextrip.android.service.RESOURCE_DATA_EXTRA";

	public static final String SERVICE_CALLBACK = "com.quasma.mynextrip.android.service.SERVICE_CALLBACK";

	public static final String ORIGINAL_INTENT_EXTRA = "com.quasma.mynextrip.android.service.ORIGINAL_INTENT_EXTRA";

	public static final String ROUTE_EXTRA = "com.quasma.mynextrip.android.service.ROUTE_EXTRA";
	public static final String DIRECTION_EXTRA = "com.quasma.mynextrip.android.service.DIRECTION_EXTRA";
	public static final String STOP_EXTRA = "com.quasma.mynextrip.android.service.STOP_EXTRA";
	public static final String STOPNUMBER_EXTRA = "com.quasma.mynextrip.android.service.STOPNUMBER_EXTRA";
	public static final String TITLE = "com.quasma.mynextrip.android.service.TITLE";
	
	private static final int REQUEST_INVALID = -1;

	private Intent originalRequestIntent;
	private ResultReceiver callback;
	
	public NexTripService()
	{
		super("NexTripService");
	}

	@Override
	protected void onHandleIntent(Intent requestIntent) 
	{
		originalRequestIntent = requestIntent;

		// Get request data from Intent
		String method = requestIntent.getStringExtra(METHOD_EXTRA);
		int resourceType = requestIntent.getIntExtra(RESOURCE_TYPE_EXTRA, -1);
		callback = requestIntent.getParcelableExtra(SERVICE_CALLBACK);

		switch (resourceType) 
		{
			case RESOURCE_TYPE_ROUTE:

				if (method.equalsIgnoreCase(METHOD_GET)) 
				{
					RouteProcessor processor = new RouteProcessor(getApplicationContext());
					processor.getRoutes(makeProcessorCallback());
				} 
				else 
				{
					Bundle bundle = new Bundle();
					bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, getString(R.string.request_invalid));
					callback.send(REQUEST_INVALID, getOriginalIntentBundle(bundle));
				}
				break;
			
			case RESOURCE_TYPE_DIRECTION:

				if (method.equalsIgnoreCase(METHOD_GET)) 
				{
					String route = requestIntent.getExtras().getString(NexTripService.ROUTE_EXTRA);
					DirectionProcessor processor = new DirectionProcessor(getApplicationContext(), route);
					processor.getDirections(makeProcessorCallback());
				} 
				else 
				{
					Bundle bundle = new Bundle();
					bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, getString(R.string.request_invalid));
					callback.send(REQUEST_INVALID, getOriginalIntentBundle(bundle));
				}
				break;

			case RESOURCE_TYPE_STOP:
				if (method.equalsIgnoreCase(METHOD_GET)) 
				{
					String route = requestIntent.getExtras().getString(NexTripService.ROUTE_EXTRA);
					String direction = requestIntent.getExtras().getString(NexTripService.DIRECTION_EXTRA);
					StopProcessor processor = new StopProcessor(getApplicationContext(), route, direction);
					processor.getStops(makeProcessorCallback());
				} 
				else 
				{
					Bundle bundle = new Bundle();
					bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, getString(R.string.request_invalid));
					callback.send(REQUEST_INVALID, getOriginalIntentBundle(bundle));
				}
				break;

			case RESOURCE_TYPE_TRIP:
				if (method.equalsIgnoreCase(METHOD_GET)) 
				{
					String route = requestIntent.getExtras().getString(NexTripService.ROUTE_EXTRA);
					String direction = requestIntent.getExtras().getString(NexTripService.DIRECTION_EXTRA);
					String stop = requestIntent.getExtras().getString(NexTripService.STOP_EXTRA);
					TripProcessor processor = new TripProcessor(getApplicationContext(), route, direction, stop);
					processor.getTrips(makeProcessorCallback());
				} 
				else 
				{
					Bundle bundle = new Bundle();
					bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, getString(R.string.request_invalid));
					callback.send(REQUEST_INVALID, getOriginalIntentBundle(bundle));
				}
				break;

			case RESOURCE_TYPE_STOPTRIP:
				if (method.equalsIgnoreCase(METHOD_GET)) 
				{
					String stopnumber = requestIntent.getExtras().getString(NexTripService.STOPNUMBER_EXTRA);
					StopTripProcessor processor = new StopTripProcessor(getApplicationContext(), stopnumber);
					processor.getTrips(makeProcessorCallback());
				} 
				else 
				{
					Bundle bundle = new Bundle();
					bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, getString(R.string.request_invalid));
					callback.send(REQUEST_INVALID, getOriginalIntentBundle(bundle));
				}
				break;

			default:
				Bundle bundle = new Bundle();
				bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, getString(R.string.request_invalid));
				callback.send(REQUEST_INVALID, getOriginalIntentBundle(bundle));
				break;
		}	
	}
	
	private ProcessorCallback makeProcessorCallback() 
	{
		ProcessorCallback callback = new ProcessorCallback() 
		{
			@Override
			public void send(int resultCode, Bundle data) 
			{
				if (NexTripService.this.callback != null) 
				{
					NexTripService.this.callback.send(resultCode, getOriginalIntentBundle(data));
				}
			}
		};
		return callback;
	}

	protected Bundle getOriginalIntentBundle(Bundle data) 
	{
		Bundle originalRequest = new Bundle();
		originalRequest.putParcelable(ORIGINAL_INTENT_EXTRA, originalRequestIntent);
		if (data != null)
			originalRequest.putAll(data);

		return originalRequest;
	}
}
