package com.quasma.android.bustrip.activity;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;
import com.quasma.android.bustrip.rest.resource.RouteList;
import com.quasma.android.bustrip.service.NexTripService;
import com.quasma.android.bustrip.service.NexTripServiceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;

public class RouteActivity extends BaseListActivity 
{
	private Long requestId;
	private BroadcastReceiver requestReceiver;
	private NexTripServiceHelper nexTripServiceHelper;
	private IntentFilter filter = new IntentFilter(NexTripServiceHelper.ACTION_REQUEST_RESULT);
	private RouteCursorAdapter adapter;
	private EditText filterText;
	private Cursor listCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		super.setUp(R.layout.routes, false);
		
		this.requestReceiver = new RouteReceiver();

		setTitle(getString(R.string.choose_route));
		
		listCursor = getContentResolver().query(RouteList.CONTENT_URI,
				RouteTable.DISPLAY_COLUMNS, null, null, RouteTable.ROUTE);

		startManagingCursor(listCursor);

		adapter = new RouteCursorAdapter(this, listCursor);
		setListAdapter(adapter);
		filterText = (EditText) findViewById(R.id.search_box);
		filterText.addTextChangedListener(filterTextWatcher);
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher()
	{
		public void afterTextChanged(Editable s) {}

	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	    public void onTextChanged(CharSequence s, int start, int before, int count) 
	    {
	    	final Cursor oldCursor = adapter.getCursor();
	    	adapter.setFilterQueryProvider(filterQueryProvider);
	    	
	    	adapter.getFilter().filter(s, new FilterListener() 
	    	{
		        public void onFilterComplete(int count) 
		        {
		            stopManagingCursor(oldCursor);
		            final Cursor newCursor = adapter.getCursor();
		            startManagingCursor(newCursor);
		            if (oldCursor != null && !oldCursor.isClosed()) 
		                oldCursor.close();
		        }
	    		
	    	});
	    }
	};
	
	private FilterQueryProvider filterQueryProvider = new FilterQueryProvider() 
	{
	    public Cursor runQuery(CharSequence constraint) 
	    {
	    	if (constraint.toString().length() == 0)
	    		return getContentResolver().query(RouteList.CONTENT_URI,
	    				RouteTable.DISPLAY_COLUMNS, null, null, RouteTable.ROUTE);
	    	else
	    		return getContentResolver().query(RouteList.CONTENT_URI,
					RouteTable.DISPLAY_COLUMNS, RouteTable.DESC + " LIKE ?", new String[] { "%" + constraint.toString() + "%" }, RouteTable.ROUTE);
	    }
	};

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
			setProgressSpinnerVisibility(true);
		} 
		else 
		if (nexTripServiceHelper.isRequestPending(requestId)) 
		{
			setProgressSpinnerVisibility(true);
		} 
		else 
		{
			setProgressSpinnerVisibility(false);
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
			if (requestId != null && resultRequestId == requestId) 
			{

				// This was our request, stop the progress spinner
				RouteActivity.this.setProgressSpinnerVisibility(false);
				debug("Result is for our request ID");

				// What was the result of our request?
				int resultCode = intent.getIntExtra(NexTripServiceHelper.EXTRA_RESULT_CODE, 0);

				debug("Result code = " + resultCode);

				String resultMessage = intent.getStringExtra(NexTripServiceHelper.EXTRA_RESULT_MSG);
				((TextView) getListView().getEmptyView()).setText(resultMessage == null ? RouteActivity.this.getString(R.string.noroutes) : resultMessage);
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
		Intent intent = new Intent();
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(NexTripService.ROUTE_EXTRA, route);
		intent.putExtra(NexTripService.TITLE, title);
	    intent.setClass(this, DirectionActivity.class);
	    startActivity(intent);
	}
	
}
