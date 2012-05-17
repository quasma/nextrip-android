package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.providers.RouteProviderContract.RouteTable;
import com.quasma.mynextrip.android.rest.resource.RouteList;
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

public class RouteActivity extends BaseRouteActivity 
{
	private Long requestId;
	private BroadcastReceiver requestReceiver;
	private NexTripServiceHelper nexTripServiceHelper;
	private IntentFilter filter = new IntentFilter(NexTripServiceHelper.ACTION_REQUEST_RESULT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);

		setContentView(R.layout.routes);
		View view =  LayoutInflater.from(this).inflate(R.layout.empty, this.getListView(), false);
		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		getListView().setEmptyView(view.findViewById(R.id.empty));

		this.requestReceiver = new RouteReceiver();
		
		Cursor cursor = getContentResolver().query(RouteList.CONTENT_URI,
				RouteTable.DISPLAY_COLUMNS, null, null, RouteTable.ROUTE);

		Intent intent = getIntent();
		String title = intent.getExtras().getString(NexTripService.TITLE);
		if (title != null)
			setTitle(title);
		
		startManagingCursor(cursor);

		RouteCursorAdapter mAdapter = new RouteCursorAdapter(this, cursor);

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
			requestId = nexTripServiceHelper.getRoutes();
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

	class RouteReceiver extends BroadcastReceiver 
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
				RouteActivity.this.setProgressBarIndeterminateVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);

				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? RouteActivity.this.getString(R.string.nodata) : resultMessage);
				((RouteCursorAdapter) RouteActivity.this.getListAdapter()).notifyDataSetChanged();
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
		String route = cursor.getString(cursor.getColumnIndex(RouteTable.ROUTE));
		String title = cursor.getString(cursor.getColumnIndex(RouteTable.DESC));
		String url = cursor.getString(cursor.getColumnIndex(RouteTable.URL));
		Intent intent = new Intent();
		intent.putExtra(NexTripService.URL_EXTRA, url);
		intent.putExtra(NexTripService.ROUTE_EXTRA, route);
		intent.putExtra(NexTripService.TITLE, title);
	    intent.setClass(this, DirectionActivity.class);
	    startActivity(intent);
	}
}
