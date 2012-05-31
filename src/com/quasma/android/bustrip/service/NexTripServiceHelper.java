package com.quasma.android.bustrip.service;

import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class NexTripServiceHelper 
{
	public static String ACTION_REQUEST_RESULT = "REQUEST_RESULT";
	public static String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
	public static String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
	public static String EXTRA_RESULT_MSG = "EXTRA_RESULT_MSG";
	
	public static String EXTRA_RESULT_DATA = "EXTRA_RESULT_DATA";

	private static final String REQUEST_ID = "REQUEST_ID";
	
	private static final String routeHashkey = "ROUTE";
	private static final String directionHashKey = "DIRECTION";
	private static final String stopHashKey = "STOP";
	private static final String tripHashKey = "TRIP";
	
	private static NexTripServiceHelper instance = null;
	private static Object lock = new Object();
	private HashMap<String,Long> pendingRequests = new HashMap<String,Long>();

	private Context ctx;
	
	private NexTripServiceHelper(Context ctx)
	{
		this.ctx = ctx.getApplicationContext();
	}
	
	public static NexTripServiceHelper getInstance(Context ctx)
	{
		synchronized (lock) 
		{
			if(instance == null)
				instance = new NexTripServiceHelper(ctx);			

			return instance;		
		}
	}
	
	public long getRoutes()
	{
		long requestId = generateRequestID();
		pendingRequests.put(routeHashkey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetRouteResponse(resultCode, resultData);
			}
		};

		Intent intent = new Intent(this.ctx, NexTripService.class);
		intent.putExtra(NexTripService.METHOD_EXTRA, NexTripService.METHOD_GET);
		intent.putExtra(NexTripService.RESOURCE_TYPE_EXTRA, NexTripService.RESOURCE_TYPE_ROUTE);
		intent.putExtra(NexTripService.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		this.ctx.startService(intent);
		
		return requestId;		
	}
	
	public long getDirections(String route)
	{
		long requestId = generateRequestID();
		pendingRequests.put(directionHashKey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetDirectionResponse(resultCode, resultData);
			}
		};

		Intent intent = new Intent(this.ctx, NexTripService.class);
		intent.putExtra(NexTripService.METHOD_EXTRA, NexTripService.METHOD_GET);
		intent.putExtra(NexTripService.RESOURCE_TYPE_EXTRA, NexTripService.RESOURCE_TYPE_DIRECTION);
		intent.putExtra(NexTripService.ROUTE_EXTRA, route);
		intent.putExtra(NexTripService.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		this.ctx.startService(intent);
		
		return requestId;				
	}
	
	public long getStops(String route, String direction)
	{
		long requestId = generateRequestID();
		pendingRequests.put(stopHashKey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetStopResponse(resultCode, resultData);
			}
		};

		Intent intent = new Intent(this.ctx, NexTripService.class);
		intent.putExtra(NexTripService.METHOD_EXTRA, NexTripService.METHOD_GET);
		intent.putExtra(NexTripService.RESOURCE_TYPE_EXTRA, NexTripService.RESOURCE_TYPE_STOP);
		intent.putExtra(NexTripService.ROUTE_EXTRA, route);
		intent.putExtra(NexTripService.DIRECTION_EXTRA, direction);
		intent.putExtra(NexTripService.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		this.ctx.startService(intent);
		
		return requestId;				
	}

	public long getTrips(String route, String direction, String stop)
	{
		long requestId = generateRequestID();
		pendingRequests.put(tripHashKey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetTripResponse(resultCode, resultData);
			}
		};

		Intent intent = new Intent(this.ctx, NexTripService.class);
		intent.putExtra(NexTripService.METHOD_EXTRA, NexTripService.METHOD_GET);
		intent.putExtra(NexTripService.RESOURCE_TYPE_EXTRA, NexTripService.RESOURCE_TYPE_TRIP);
		intent.putExtra(NexTripService.ROUTE_EXTRA, route);
		intent.putExtra(NexTripService.DIRECTION_EXTRA, direction);
		intent.putExtra(NexTripService.STOP_EXTRA, stop);
		intent.putExtra(NexTripService.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		this.ctx.startService(intent);
		
		return requestId;				
	}

	public long getTrips(String stopnumber)
	{
		long requestId = generateRequestID();
		pendingRequests.put(tripHashKey, requestId);

		ResultReceiver serviceCallback = new ResultReceiver(null){
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				handleGetTripResponse(resultCode, resultData);
			}
		};

		Intent intent = new Intent(this.ctx, NexTripService.class);
		intent.putExtra(NexTripService.METHOD_EXTRA, NexTripService.METHOD_GET);
		intent.putExtra(NexTripService.RESOURCE_TYPE_EXTRA, NexTripService.RESOURCE_TYPE_STOPTRIP);
		intent.putExtra(NexTripService.STOPNUMBER_EXTRA, stopnumber);
		intent.putExtra(NexTripService.SERVICE_CALLBACK, serviceCallback);
		intent.putExtra(REQUEST_ID, requestId);

		this.ctx.startService(intent);
		
		return requestId;				
	}

	private void handleGetRouteResponse(int resultCode, Bundle resultData)
	{
		Intent origIntent = (Intent)resultData.getParcelable(NexTripService.ORIGINAL_INTENT_EXTRA);

		if(origIntent != null)
		{
			long requestId = origIntent.getLongExtra(REQUEST_ID, 0);

			pendingRequests.remove(routeHashkey);

			Intent resultBroadcast = new Intent(ACTION_REQUEST_RESULT);
			resultBroadcast.putExtra(EXTRA_REQUEST_ID, requestId);
			resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
			resultBroadcast.putExtras(resultData);
			ctx.sendBroadcast(resultBroadcast);
		}
	}	

	private void handleGetDirectionResponse(int resultCode, Bundle resultData)
	{
		Intent origIntent = (Intent)resultData.getParcelable(NexTripService.ORIGINAL_INTENT_EXTRA);

		if(origIntent != null)
		{
			long requestId = origIntent.getLongExtra(REQUEST_ID, 0);

			pendingRequests.remove(directionHashKey);

			Intent resultBroadcast = new Intent(ACTION_REQUEST_RESULT);
			resultBroadcast.putExtra(EXTRA_REQUEST_ID, requestId);
			resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
			resultBroadcast.putExtras(resultData);
			ctx.sendBroadcast(resultBroadcast);
		}
	}	

	private void handleGetStopResponse(int resultCode, Bundle resultData)
	{
		Intent origIntent = (Intent)resultData.getParcelable(NexTripService.ORIGINAL_INTENT_EXTRA);

		if(origIntent != null)
		{
			long requestId = origIntent.getLongExtra(REQUEST_ID, 0);

			pendingRequests.remove(stopHashKey);

			Intent resultBroadcast = new Intent(ACTION_REQUEST_RESULT);
			resultBroadcast.putExtra(EXTRA_REQUEST_ID, requestId);
			resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
			resultBroadcast.putExtras(resultData);
			ctx.sendBroadcast(resultBroadcast);
		}
	}	

	private void handleGetTripResponse(int resultCode, Bundle resultData)
	{
		Intent origIntent = (Intent)resultData.getParcelable(NexTripService.ORIGINAL_INTENT_EXTRA);

		if(origIntent != null)
		{
			long requestId = origIntent.getLongExtra(REQUEST_ID, 0);

			pendingRequests.remove(tripHashKey);

			Intent resultBroadcast = new Intent(ACTION_REQUEST_RESULT);
			resultBroadcast.putExtra(EXTRA_REQUEST_ID, requestId);
			resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
			resultBroadcast.putExtras(resultData);
			ctx.sendBroadcast(resultBroadcast);
		}
	}	

	private long generateRequestID() 
	{
		long requestId = UUID.randomUUID().getLeastSignificantBits();
		return requestId;
	}
	
	public boolean isRequestPending(long requestId)
	{
		return pendingRequests.containsValue(requestId);
	}
}
