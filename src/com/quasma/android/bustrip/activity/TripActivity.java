package com.quasma.android.bustrip.activity;

import java.util.ArrayList;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.providers.DirectionProviderContract.DirectionTable;
import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;
import com.quasma.android.bustrip.rest.resource.Trip;
import com.quasma.android.bustrip.rest.resource.TripList;
import com.quasma.android.bustrip.service.FavoriteProcessor;
import com.quasma.android.bustrip.service.NexTripService;
import com.quasma.android.bustrip.service.NexTripServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TripActivity extends BaseListActivity
{
	private Long requestId;
	private BroadcastReceiver requestReceiver;
	private NexTripServiceHelper nexTripServiceHelper;
	private TextView route;
	private TextView direction;
	private TextView stop;
	private FavoriteProcessor favoriteProcessor;
	private CheckBox favorite;
	private TripList tripList = null;
	private IntentFilter filter = new IntentFilter(NexTripServiceHelper.ACTION_REQUEST_RESULT);
	
	private Long routeRequestId;
	private Long directionRequestId;
	private Long stopRequestId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips);
			
		setProgressBar((ProgressBar) findViewById(android.R.id.progress));

		route 		= (TextView) findViewById(R.id.trip_route);
		direction 	= (TextView) findViewById(R.id.trip_direction);
		stop 		= (TextView) findViewById(R.id.trip_stop);
		
		favoriteProcessor = new FavoriteProcessor(this);
		
		favorite = (CheckBox) findViewById(R.id.favorite);
		
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
			String route 		= getIntent().getExtras().getString(NexTripService.ROUTE_EXTRA);
			String direction 	= getIntent().getExtras().getString(NexTripService.DIRECTION_EXTRA);
			String stop 		= getIntent().getExtras().getString(NexTripService.STOP_EXTRA);
			
			favorite.setChecked(favoriteProcessor.isFavorite(route, direction, stop));
			requestId = nexTripServiceHelper.getTrips(route, direction, stop);
			setProgressSpinnerVisibility(true);
		} 
		else 
		if (nexTripServiceHelper.isRequestPending(requestId)) 
		{
			setProgressSpinnerVisibility(true);
		} 
		else 
		{
			String route 		= getIntent().getExtras().getString(NexTripService.ROUTE_EXTRA);
			String direction 	= getIntent().getExtras().getString(NexTripService.DIRECTION_EXTRA);
			String stop 		= getIntent().getExtras().getString(NexTripService.STOP_EXTRA);
			
			favorite.setChecked(favoriteProcessor.isFavorite(route, direction, stop));
			requestId = nexTripServiceHelper.getTrips(route, direction, stop);
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

	class TripReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// Returns the id of original request
			long resultRequestId = intent.getLongExtra(NexTripServiceHelper.EXTRA_REQUEST_ID, 0);

			debug("Received intent " + intent.getAction() + ", request ID " + resultRequestId);

			// check if this was OUR request
			if (requestId != null && resultRequestId == requestId) 
			{

				// This was our request, stop the progress spinner
				TripActivity.this.setProgressSpinnerVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);

				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? TripActivity.this.getString(R.string.notrips) : resultMessage);
				
				final TripList tripList = (TripList) intent.getExtras().getParcelable(NexTripService.RESOURCE_DATA_EXTRA);
				
				if (tripList != null)
				{					
					TripActivity.this.tripList = tripList;
					
					favorite.setChecked(favoriteProcessor.isFavorite(tripList.getRoute(), tripList.getDirection(), tripList.getStop()));
					
					TripActivity.this.setListAdapter(new TripArrayAdapter(TripActivity.this, tripList.getTrips()));
					
					favorite.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
					    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
					    {
					        if (isChecked)
					        {
					        	favoriteProcessor.setFavorite(tripList.getRoute(), tripList.getDirection(), tripList.getStop(),
					        			TripActivity.this.route.getText().toString() + " - "
					        			+ TripActivity.this.direction.getText().toString() + " - "
					        			+ TripActivity.this.stop.getText().toString());
					        }
					        else
					        {
					        	favoriteProcessor.removeFavorite(tripList.getRoute(), tripList.getDirection(), tripList.getStop());
					        }
					    }
					});
					setHeader(tripList);
				}
				else
				{
					TripActivity.this.tripList = null;
					TripActivity.this.setListAdapter(new TripArrayAdapter(TripActivity.this, new ArrayList<Trip>(0)));
				}
				requestId = null;
			} 
			else
			if (routeRequestId != null && resultRequestId == routeRequestId)
			{
				if (tripList != null)
				{
					TripActivity.this.setRouteHeader(tripList.getRoute());
				}
				routeRequestId = null;
			}
			else
			if (directionRequestId != null && resultRequestId == directionRequestId)
			{
				if (tripList != null)
				{
					TripActivity.this.setDirectionHeader(tripList.getRoute(), tripList.getDirection());
				}
				directionRequestId = null;
			}
			else
			if (stopRequestId != null && resultRequestId == stopRequestId)
			{
				if (tripList != null)
				{
					TripActivity.this.setStopHeader(tripList.getRoute(), tripList.getDirection(), tripList.getStop());
				}
				stopRequestId = null;
			}
			else
			{
				// IGNORE, wasn't for our request
				debug("Result is NOT for our request ID");
			}
		}
	}
	
	private void setHeader(TripList tripList)
	{
		if (!setRouteHeader(tripList.getRoute()))
			routeRequestId = nexTripServiceHelper.getRoutes();

		if (!setDirectionHeader(tripList.getRoute(), tripList.getDirection()))
			directionRequestId = nexTripServiceHelper.getDirections(tripList.getRoute());
		
		if (!setStopHeader(tripList.getRoute(), tripList.getDirection(), tripList.getStop()))
			stopRequestId = nexTripServiceHelper.getStops(tripList.getRoute(), tripList.getDirection());
	}
	
	private boolean setRouteHeader(String route)
	{
		Cursor cursor = getContentResolver().query(RouteTable.CONTENT_URI,
				new String[] { RouteTable.DESC }, RouteTable.ROUTE + " LIKE ?", new String[] { route }, null);

		try
		{
			if (cursor.moveToNext())
				this.route.setText(cursor.getString(0));
			else
				return false;
		
			return true;
		}
		finally
		{
			if (!cursor.isClosed()) cursor.close();
		}
	}
	
	private boolean setDirectionHeader(String route, String direction)
	{
		Cursor cursor = getContentResolver().query(DirectionTable.CONTENT_URI,
				new String[] { DirectionTable.DESC }, DirectionTable.ROUTE + " LIKE ? AND " + DirectionTable.DIRECTION + " LIKE ? " , 
				new String[] { route, direction }, null);

		try
		{
			if (cursor.moveToNext())
				this.direction.setText(cursor.getString(0));
			else
				return false;
		
			return true;
		}
		finally
		{
			if (!cursor.isClosed()) cursor.close();
		}
	}
	
	private boolean setStopHeader(String route, String direction, String stop)
	{
		Cursor cursor = getContentResolver().query(StopTable.CONTENT_URI,
				new String[] { StopTable.DESC }, StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? AND " + StopTable.STOP + " LIKE ?" , 
				new String[] { route, direction, stop }, null);

		try
		{
			if (cursor.moveToNext()
			&&  cursor.getString(0) != null
			&&  cursor.getString(0).trim().length() != 0)
				this.stop.setText(cursor.getString(0));
			else
				return false;
		
			return true;
		}
		finally
		{
			if (!cursor.isClosed()) cursor.close();
		}
	}
}
