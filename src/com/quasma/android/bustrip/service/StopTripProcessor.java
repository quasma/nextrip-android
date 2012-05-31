package com.quasma.android.bustrip.service;

import java.util.ArrayList;
import java.util.List;

import com.quasma.android.bustrip.providers.StopNumberProviderContract.StopNumberTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;
import com.quasma.android.bustrip.rest.RestMethodResult;
import com.quasma.android.bustrip.rest.StopNumberRestMethod;
import com.quasma.android.bustrip.rest.resource.Stop;
import com.quasma.android.bustrip.rest.resource.StopList;
import com.quasma.android.bustrip.rest.resource.StopTripList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class StopTripProcessor
{
	private Context context;
	private String stopnumber;

	public StopTripProcessor(Context context, String stopnumber) 
	{
		this.context = context;
		this.stopnumber = stopnumber;
	}

	
	void getTrips(ProcessorCallback callback) 
	{
		final RestMethodResult<StopTripList> result = new StopNumberRestMethod(context, stopnumber).execute();
		Bundle bundle = new Bundle();
		if (result.getStatusCode() < 300)
		{
			updateContentProvider(result);
			bundle.putParcelable(NexTripService.RESOURCE_DATA_EXTRA, result.getResource());
		}
		bundle.putString(NexTripServiceHelper.EXTRA_RESULT_MSG, result.getStatusMsg());
		callback.send(result.getStatusCode(), bundle);
	}
	
	private void updateContentProvider(RestMethodResult<StopTripList> result) 
	{				
		StopTripList stopList = result.getResource();
		ContentResolver cr = this.context.getContentResolver();
		cr.insert(StopNumberTable.CONTENT_URI, stopList.toContentValues(result.getTime()));
		Log.d(getClass().getSimpleName(), "Created StopNumber");
	}

}
