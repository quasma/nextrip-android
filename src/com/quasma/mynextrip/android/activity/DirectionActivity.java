package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;
import com.quasma.mynextrip.android.rest.resource.DirectionList;
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
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class DirectionActivity extends BaseRouteActivity 
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

		setContentView(R.layout.directions);
		View view =  LayoutInflater.from(this).inflate(R.layout.empty, this.getListView(), false);
		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		getListView().setEmptyView(view.findViewById(R.id.empty));

		this.requestReceiver = new DirectionReceiver();
		
		String url = getIntent().getExtras().getString(NexTripService.URL_EXTRA);
		String route = getIntent().getExtras().getString(NexTripService.ROUTE_EXTRA);
		String title = getIntent().getExtras().getString(NexTripService.TITLE);
		cursor = getContentResolver().query(DirectionList.CONTENT_URI,
				DirectionTable.DISPLAY_COLUMNS, DirectionTable.ROUTE + " = '" + route + "'" , null, DirectionTable.DIRECTION);

		if (title != null)
			this.setTitle(title);
		
		startManagingCursor(cursor);

		DirectionCursorAdapter mAdapter = new DirectionCursorAdapter(this, cursor);
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
			requestId = nexTripServiceHelper.getDirections(url, route);
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

	class DirectionReceiver extends BroadcastReceiver 
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
				DirectionActivity.this.setProgressBarIndeterminateVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);
				
				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? DirectionActivity.this.getString(R.string.nodata) : resultMessage);
				((DirectionCursorAdapter) DirectionActivity.this.getListAdapter()).notifyDataSetChanged();
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
		String route = cursor.getString(cursor.getColumnIndex(DirectionTable.ROUTE));
		String direction = cursor.getString(cursor.getColumnIndex(DirectionTable.DIRECTION));
		String title = cursor.getString(cursor.getColumnIndex(DirectionTable.DESC));
		String url = cursor.getString(cursor.getColumnIndex(DirectionTable.URL));
		Intent intent = new Intent();
		intent.putExtra(NexTripService.URL_EXTRA, url);
		intent.putExtra(NexTripService.ROUTE_EXTRA, route);
		intent.putExtra(NexTripService.DIRECTION_EXTRA, direction);
		intent.putExtra(NexTripService.TITLE, title.charAt(0) + title.substring(1).toLowerCase() + " - " + getTitle());
	    intent.setClass(this, StopActivity.class);
	    startActivity(intent);
	}
}
