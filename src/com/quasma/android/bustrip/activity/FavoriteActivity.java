package com.quasma.android.bustrip.activity;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.providers.FavoriteProviderContract.FavoriteTable;
import com.quasma.android.bustrip.providers.StopNumberProviderContract.StopNumberTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;
import com.quasma.android.bustrip.service.FavoriteProcessor;
import com.quasma.android.bustrip.service.NexTripService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;

public class FavoriteActivity extends BaseListActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		super.setUp(R.layout.favorite, false);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View v, int position, long id) 
			{
				Cursor cursor = (Cursor) getListAdapter().getItem(position);
				String desc = cursor.getString(cursor.getColumnIndex(FavoriteTable.DESC));
				final String key = cursor.getString(cursor.getColumnIndex(FavoriteTable.KEY));
				AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
				final EditText input = new EditText(v.getContext());
				input.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_CLASS_TEXT);
				input.setText(desc);
				input.selectAll();
				input.setOnFocusChangeListener(new OnFocusChangeListener() 
				{
			        @Override
			        public void onFocusChange(View v, boolean hasFocus) 
			        {
			            input.post(new Runnable() 
			            {
			                @Override
			                public void run() 
			                {
			                    InputMethodManager inputMethodManager= (InputMethodManager) FavoriteActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
			                    inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
			                }
			            });
			        }
			    });
			    input.requestFocus();
				alert.setView(input);
				alert.setTitle("Edit Description");
				
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						String value = input.getText().toString();
						FavoriteProcessor favoriteProcessor = new FavoriteProcessor(v.getContext());
						favoriteProcessor.updateFavorite(key, value);
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
				    // Canceled.
					}
				});
				alert.show();				
				return true;
			}
		});
		getListView().setOnItemClickListener( new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,long id) 
			{
				Cursor cursor = (Cursor) getListAdapter().getItem(position);
				String object = cursor.getString(cursor.getColumnIndex(FavoriteTable.TABLE));
				long key      = cursor.getLong(cursor.getColumnIndex(FavoriteTable.KEY));
				if (StopNumberTable.TABLE_NAME.equals(object))
				{
					doStopTrip(key);
				}
				else
				if (StopTable.TABLE_NAME.equals(object))
				{
					doRouteTrip(key);
				}
			}			
		});
		setTitle(getString(R.string.choose_favorite));
		
		Cursor cursor = getContentResolver().query(FavoriteTable.CONTENT_URI,
				FavoriteTable.DISPLAY_COLUMNS, null, null, null);

		startManagingCursor(cursor);

		FavoriteCursorAdapter mAdapter = new FavoriteCursorAdapter(this, cursor);

		setListAdapter(mAdapter);

	}
	@Override
	protected void onResume() 
	{
		super.onResume();
	}

	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void doStopTrip(long key)
	{
		Cursor cursor = null;
		try
		{
			cursor = getContentResolver().query(StopNumberTable.CONTENT_URI,
					StopNumberTable.ALL_COLUMNS, StopNumberTable._ID + " LIKE ?", new String[] { String.valueOf(key) }, null); 
			
			if (cursor.moveToFirst())
			{
				Intent intent = new Intent();
				intent.putExtra(NexTripService.STOPNUMBER_EXTRA, cursor.getString(cursor.getColumnIndex(StopNumberTable.STOPNUMBER)));
				intent.setClass(this, (Class) StopTripActivity.class);
				startActivity(intent);
			}
		}
		finally
		{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}

	private void doRouteTrip(long key)
	{
		Cursor cursor = null;
		try
		{
			cursor = getContentResolver().query(StopTable.CONTENT_URI,
					StopTable.ALL_COLUMNS, StopTable._ID + " LIKE ?", new String[] { String.valueOf(key) }, null); 
			
			if (cursor.moveToFirst())
			{
				Intent intent = new Intent();
				String route = cursor.getString(cursor.getColumnIndex(StopTable.ROUTE));
				String direction = cursor.getString(cursor.getColumnIndex(StopTable.DIRECTION));
				String stop = cursor.getString(cursor.getColumnIndex(StopTable.STOP));
				intent.putExtra(NexTripService.ROUTE_EXTRA, route);
				intent.putExtra(NexTripService.DIRECTION_EXTRA, direction);
				intent.putExtra(NexTripService.STOP_EXTRA, stop);
			    intent.setClass(this, TripActivity.class);
			    startActivity(intent);
			}
		}
		finally
		{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}
	
	
}
