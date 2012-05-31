package com.quasma.android.bustrip.activity;

import java.text.MessageFormat;
import java.util.ArrayList;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.rest.resource.StopTripList;
import com.quasma.android.bustrip.rest.resource.Trip;
import com.quasma.android.bustrip.service.FavoriteProcessor;
import com.quasma.android.bustrip.service.NexTripService;
import com.quasma.android.bustrip.service.NexTripServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class StopTripActivity extends BaseListActivity
{
	private Long requestId;
	private BroadcastReceiver requestReceiver;
	private NexTripServiceHelper nexTripServiceHelper;
	private FavoriteProcessor favoriteProcessor;
	private CheckBox favorite;
	
	private IntentFilter filter = new IntentFilter(NexTripServiceHelper.ACTION_REQUEST_RESULT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		super.setUp(R.layout.stoptrips, true);

		this.requestReceiver = new StopTripReceiver();
		favoriteProcessor = new FavoriteProcessor(this);
		favorite = (CheckBox) findViewById(R.id.favorite);
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
			String stopnumber = getIntent().getExtras().getString(NexTripService.STOPNUMBER_EXTRA);
			this.setTitle("Stop Number: " + stopnumber);
			requestId = nexTripServiceHelper.getTrips(stopnumber);
			setProgressSpinnerVisibility(true);
		} 
		else 
		if (nexTripServiceHelper.isRequestPending(requestId)) 
		{
			setProgressSpinnerVisibility(true);
		} 
		else 
		{
			String stopnumber = getIntent().getExtras().getString(NexTripService.STOPNUMBER_EXTRA);
			this.setTitle("Stop Number: " + stopnumber);
			requestId = nexTripServiceHelper.getTrips(stopnumber);
			setProgressSpinnerVisibility(true);
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

	class StopTripReceiver extends BroadcastReceiver 
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
				StopTripActivity.this.setProgressSpinnerVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);

				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? StopTripActivity.this.getString(R.string.notrips) : resultMessage);
				
				final StopTripList tripList = (StopTripList) intent.getExtras().getParcelable(NexTripService.RESOURCE_DATA_EXTRA);
				if (tripList != null)
				{
					StopTripActivity.this.setListAdapter(new StopTripArrayAdapter(StopTripActivity.this, tripList.getTrips()));
					
					favorite.setChecked(favoriteProcessor.isFavorite(tripList.getStopNumber()));
					
					favorite.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
					    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
					    {
					        if (isChecked)
					        {
					        	favoriteProcessor.setFavorite(tripList.getStopNumber(),
					        			MessageFormat.format(StopTripActivity.this.getString(R.string.stopnumber), tripList.getStopNumber()));
					        }
					        else
					        {
					        	favoriteProcessor.removeFavorite(tripList.getStopNumber());
					        }
					    }
					});

				}
				else
				{
					StopTripActivity.this.setListAdapter(new StopTripArrayAdapter(StopTripActivity.this, new ArrayList<Trip>(0)));
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
