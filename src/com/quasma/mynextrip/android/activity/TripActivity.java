package com.quasma.mynextrip.android.activity;

import java.util.ArrayList;

import com.quasma.mynextrip.android.R;
import com.quasma.mynextrip.android.rest.resource.Trip;
import com.quasma.mynextrip.android.rest.resource.TripList;
import com.quasma.mynextrip.android.service.NexTripService;
import com.quasma.mynextrip.android.service.NexTripServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TripActivity extends BaseRouteActivity
{
	private Long requestId;
	private BroadcastReceiver requestReceiver;
	private NexTripServiceHelper nexTripServiceHelper;
	private TextView route;
	private TextView description;
	private TextView direction;
	private TextView location;
	
	private IntentFilter filter = new IntentFilter(NexTripServiceHelper.ACTION_REQUEST_RESULT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);

		setContentView(R.layout.trips);
		View view =  LayoutInflater.from(this).inflate(R.layout.empty, this.getListView(), false);
		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		getListView().setEmptyView(view.findViewById(R.id.empty));
		
		route 		= (TextView) findViewById(R.id.trip_route);
		description = (TextView) findViewById(R.id.trip_description);
		direction 	= (TextView) findViewById(R.id.trip_direction);
		location 	= (TextView) findViewById(R.id.trip_location);
		
		this.requestReceiver = new TripReceiver();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		this.registerReceiver(this.requestReceiver, this.filter);
		nexTripServiceHelper = NexTripServiceHelper.getInstance(this);
		refresh();
	}

	@Override
	public void refresh()
	{
		if (requestId == null) 
		{
			String url = getIntent().getExtras().getString(NexTripService.URL_EXTRA);
			requestId = nexTripServiceHelper.getTrips(url);
			setProgressBarIndeterminateVisibility(true);
		} 
		else 
		if (nexTripServiceHelper.isRequestPending(requestId)) 
		{
			setProgressBarIndeterminateVisibility(true);
		} 
		else 
		{
			String url = getIntent().getExtras().getString(NexTripService.URL_EXTRA);
			requestId = nexTripServiceHelper.getTrips(url);
			setProgressBarIndeterminateVisibility(true);
		}		
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();

		// Unregister for broadcast
		try 
		{
			this.unregisterReceiver(requestReceiver);
		} 
		catch (IllegalArgumentException e) 
		{
			warn("Likely receiver wasn't registered, ok to ignore");
		}
	}

	class TripReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// Returns the id of original request
			long resultRequestId = intent.getLongExtra(NexTripServiceHelper.EXTRA_REQUEST_ID, 0);

			debug("Received intent " + intent.getAction() + ", request ID " + resultRequestId);

			// check if this was OUR request
			if (resultRequestId == requestId) 
			{

				// This was our request, stop the progress spinner
				TripActivity.this.setProgressBarIndeterminateVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);

				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? TripActivity.this.getString(R.string.nodata) : resultMessage);
				
				TripList tripList = (TripList) intent.getExtras().getParcelable(NexTripService.RESOURCE_DATA_EXTRA);
				if (tripList != null)
				{
					route.setText(tripList.getRoute());
					description.setText(tripList.getRouteDescription());
					direction.setText(tripList.getDirection());
					location.setText(tripList.getLocation());
					TripActivity.this.setListAdapter(new TripArrayAdapter(TripActivity.this, tripList.getTrips()));
				}
				else
				{
					TripActivity.this.setListAdapter(new TripArrayAdapter(TripActivity.this, new ArrayList<Trip>(0)));
				}
				requestId = null;
} 
			else 
			{
				// IGNORE, wasn't for our request
				debug("Result is NOT for our request ID");
			}

		}
	}
}
