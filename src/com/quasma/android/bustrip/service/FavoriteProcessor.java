package com.quasma.android.bustrip.service;

import com.quasma.android.bustrip.providers.FavoriteProviderContract.FavoriteTable;
import com.quasma.android.bustrip.providers.StopNumberProviderContract.StopNumberTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class FavoriteProcessor
{
	private Context context;

	public FavoriteProcessor(Context context) 
	{
		this.context = context;
	}

	private long getId(String stopnumber)
	{
		Cursor cursor = null;
		try
		{
			cursor = context.getContentResolver().query(StopNumberTable.CONTENT_URI, new String[] { StopNumberTable._ID },
					StopNumberTable.STOPNUMBER + " LIKE ?", new String[] {stopnumber}, null);
			
			if (cursor.moveToNext())
				return cursor.getLong(0);
			
			return -1;
		}
		finally
		{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		
	}

	private long getId(String route, String direction, String stop)
	{
		Cursor cursor = null;
		try
		{
			cursor = context.getContentResolver().query(StopTable.CONTENT_URI, new String[] { StopTable._ID },
					StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? AND " + StopTable.STOP + " LIKE ?", 
					new String[] {route, direction, stop}, null);
			
			if (cursor.moveToFirst())
				return cursor.getLong(0);
			
			return -1;
		}
		finally
		{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		
	}

	public boolean isFavorite(String route, String direction, String stop)
	{
		long id = getId(route, direction, stop);
		if (id < 0)
			return false;
		
		return isFavorite(StopTable.TABLE_NAME, id);
	}
	
	public boolean isFavorite(String stopnumber)
	{
		long id = getId(stopnumber);
		if (id < 0)
			return false;
		
		return isFavorite(StopNumberTable.TABLE_NAME, id);
	}
	
	public boolean isFavorite(String table, long id)
	{
		Cursor cursor = null;
		try
		{
			cursor = context.getContentResolver().query( FavoriteTable.CONTENT_URI, new String[] { FavoriteTable._ID },
					FavoriteTable.TABLE + " LIKE ? AND " + FavoriteTable.KEY + " LIKE ?" , 
					new String[] { table, String.valueOf(id) }, null);

			if (cursor.moveToNext())
				return true;

			return false;
		}
		finally
		{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}
		
	public void setFavorite(String route, String direction, String stop, String desc)
	{
		Cursor cursor = null;
		long id;
		try
		{
			cursor = context.getContentResolver().query(StopTable.CONTENT_URI, StopTable.DISPLAY_COLUMNS,
					StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? AND " + StopTable.STOP + " LIKE ?", 
					new String[] {route, direction, stop}, null);
			
			if (cursor.moveToFirst())
			{
				id 		= cursor.getLong(cursor.getColumnIndex(StopTable._ID));
				setFavorite(StopTable.TABLE_NAME, id, desc);
			}			
		}
		finally
		{
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}
	
	public void setFavorite(String stopnumber, String desc)
	{
		long id = getId(stopnumber);
		if (id < 0)
			return;
		
		setFavorite(StopNumberTable.TABLE_NAME, id, desc);
	}
	
	public void setFavorite(String table, long id, String desc)
	{
		ContentValues values = new ContentValues();
		values.put(FavoriteTable.TABLE, table);
		values.put(FavoriteTable.KEY, id);
		values.put(FavoriteTable.DESC, desc);
		context.getContentResolver().insert(FavoriteTable.CONTENT_URI, values);
		
		Log.d(this.getClass().getSimpleName(), "Added favorite " + table + "/" + id);
	}
	
	public void removeFavorite(String route, String direction, String stop)
	{
		long id = getId(route, direction, stop);
		if (id < 0)
			return ;
		
		removeFavorite(StopTable.TABLE_NAME, id);
	}
	
	public void removeFavorite(String stopnumber)
	{
		long id = getId(stopnumber);
		if (id < 0)
			return;
		
		removeFavorite(StopNumberTable.TABLE_NAME, id);
	}
	
	public void removeFavorite(String table, long id)
	{
		context.getContentResolver().delete(FavoriteTable.CONTENT_URI, 
				FavoriteTable.TABLE + " LIKE ? AND " + FavoriteTable.KEY + " LIKE ?",
				new String[] { table, String.valueOf(id) });

		Log.d(this.getClass().getSimpleName(), "Removed favorite " + table + "/" + id);
	}
}
