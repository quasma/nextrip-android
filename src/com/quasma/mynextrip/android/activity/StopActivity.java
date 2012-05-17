package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;
import com.quasma.mynextrip.android.providers.StopProviderContract.StopTable;
import com.quasma.mynextrip.android.rest.resource.StopList;
import com.quasma.mynextrip.android.service.NexTripService;
import com.quasma.mynextrip.android.service.NexTripServiceHelper;
import com.quasma.mynextrip.android.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class StopActivity extends BaseRouteActivity 
{
	private Long requestId;
	private BroadcastReceiver requestReceiver;
	private NexTripServiceHelper nexTripServiceHelper;

	private IntentFilter filter = new IntentFilter(NexTripServiceHelper.ACTION_REQUEST_RESULT);
	
	private Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);

		setContentView(R.layout.stops);
		View view =  LayoutInflater.from(this).inflate(R.layout.empty, this.getListView(), false);
		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		getListView().setEmptyView(view.findViewById(R.id.empty));

		this.requestReceiver = new StopReceiver();
		
		String url = getIntent().getExtras().getString(NexTripService.URL_EXTRA);
		String route = getIntent().getExtras().getString(NexTripService.ROUTE_EXTRA);
		String direction = getIntent().getExtras().getString(NexTripService.DIRECTION_EXTRA);
		String title = getIntent().getExtras().getString(NexTripService.TITLE);
		
		cursor = getContentResolver().query(StopList.CONTENT_URI,
				StopTable.DISPLAY_COLUMNS, StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? " , new String[] { route, direction }, StopTable.DIRECTION);

		if (title != null)
			setTitle(title);
		
		startManagingCursor(cursor);

		StopCursorAdapter mAdapter = new StopCursorAdapter(this, cursor);
		setListAdapter(mAdapter);
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
			String route = getIntent().getExtras().getString(NexTripService.ROUTE_EXTRA);
			String direction = getIntent().getExtras().getString(NexTripService.DIRECTION_EXTRA);
			requestId = nexTripServiceHelper.getStops(url, route, direction);
			setProgressBarIndeterminateVisibility(true);
		} 
		else 
		if (nexTripServiceHelper.isRequestPending(requestId)) 
		{
			setProgressBarIndeterminateVisibility(true);
		} 
		else 
		{
			setProgressBarIndeterminateVisibility(false);
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

	class StopReceiver extends BroadcastReceiver 
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
				StopActivity.this.setProgressBarIndeterminateVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);

				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? StopActivity.this.getString(R.string.nodata) : resultMessage);
				((StopCursorAdapter) StopActivity.this.getListAdapter()).notifyDataSetChanged();
				requestId = null;
} 
			else 
			{
				// IGNORE, wasn't for our request
				debug("Result is NOT for our request ID");
			}

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		Cursor cursor = (Cursor) getListAdapter().getItem(position);
		String url = cursor.getString(cursor.getColumnIndex(DirectionTable.URL));
		Intent intent = new Intent();
		intent.putExtra(NexTripService.URL_EXTRA, url);
	    intent.setClass(this, TripActivity.class);
	    startActivity(intent);
	}
}
