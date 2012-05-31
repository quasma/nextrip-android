package com.quasma.android.bustrip.service;

import java.util.ArrayList;
import java.util.List;

import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;
import com.quasma.android.bustrip.rest.RestMethodResult;
import com.quasma.android.bustrip.rest.RouteRestMethod;
import com.quasma.android.bustrip.rest.resource.Route;
import com.quasma.android.bustrip.rest.resource.RouteList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class RouteProcessor
{
	protected static final String TAG = RouteProcessor.class.getSimpleName();

	private Context context;

	
	public RouteProcessor(Context context) 
	{
		this.context = context;
	}

	
	void getRoutes(ProcessorCallback callback) 
	{
		RestMethodResult<RouteList> result = new RouteRestMethod(context).execute();
		if (result.getStatusCode() < 300)
			updateContentProvider(result);
		
		Bundle bundle = new Bundle();
		bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, result.getStatusMsg());
		callback.send(result.getStatusCode(), bundle);
	}

	private void updateContentProvider(RestMethodResult<RouteList> result) 
	{				
		RouteList routeList = result.getResource();
		List<Route> routes = routeList.getRoutes();
		ContentResolver cr = this.context.getContentResolver();

		ArrayList<ContentValues> values = new ArrayList<ContentValues>(routes.size());
		for (Route route : routes) 
			values.add(route.toContentValues(result.getTime()));

		int created = cr.bulkInsert(RouteTable.CONTENT_URI, values.toArray(new ContentValues[0]));
		int deleted = cr.delete(RouteTable.CONTENT_URI, RouteTable.CREATED + " <> '" + result.getTime() + "'", null);
		
		Log.d(getClass().getSimpleName(), "Created " + created + " Routes");
		Log.d(getClass().getSimpleName(), "Deleted " + deleted + " Routes");
	}
}
