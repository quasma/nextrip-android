package com.quasma.mynextrip.android.providers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;
import com.quasma.mynextrip.android.providers.RouteProviderContract.RouteTable;
import com.quasma.mynextrip.android.providers.StopProviderContract.StopTable;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class NexTripProvider extends ContentProvider
{
	private static final int ROUTE = 1;
	private static final int DIRECTION = 2;
	private static final int STOP = 3;
	
	private static HashMap<String, String> routeProjectionMap;
	private static HashMap<String, String> directionProjectionMap;
	private static HashMap<String, String> stopProjectionMap;
	private ProviderDbHelper dbHelper;
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static
	{
		
		uriMatcher.addURI(RouteProviderContract.AUTHORITY, RouteTable.TABLE_NAME, ROUTE);
		uriMatcher.addURI(DirectionProviderContract.AUTHORITY, DirectionTable.TABLE_NAME, DIRECTION);
		uriMatcher.addURI(StopProviderContract.AUTHORITY, StopTable.TABLE_NAME, STOP);

		routeProjectionMap = new HashMap<String, String>();
		for (String column : RouteTable.ALL_COLUMNS) 
		{
			routeProjectionMap.put(column, column);
		}
		directionProjectionMap = new HashMap<String, String>();
		for (String column : DirectionTable.ALL_COLUMNS) 
		{
			directionProjectionMap.put(column, column);
		}
		stopProjectionMap = new HashMap<String, String>();
		for (String column : StopTable.ALL_COLUMNS) 
		{
			stopProjectionMap.put(column, column);
		}
	}
	
	private static final String ROUTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.route";
	private static final String ROUTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.route";
	private static final String DIRECTION_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.direction";
	private static final String DIRECTION_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.direction";
	private static final String STOP_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.stop";
	private static final String STOP_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.stop";
	
	@Override
	public boolean onCreate() 
	{
		this.dbHelper = new ProviderDbHelper(this.getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereValues) 
	{
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		String finalWhere;
		int deletedRowsCount;

		switch (uriMatcher.match(uri)) 
		{
			case ROUTE:
				// Delete all the rage comics matching the where column/value pairs
				deletedRowsCount = db.delete(RouteTable.TABLE_NAME, whereClause, whereValues);
				break;

			case DIRECTION:
				deletedRowsCount = db.delete(DirectionTable.TABLE_NAME, whereClause, whereValues);
				break;
				
			case STOP:
				deletedRowsCount = db.delete(StopTable.TABLE_NAME, whereClause, whereValues);
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Notify observers of the the change
		getContext().getContentResolver().notifyChange(uri, null);

		// Returns the number of rows deleted.
		return deletedRowsCount;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) 
	{
		
		// Validate the incoming URI.
		if (uriMatcher.match(uri) == ROUTE) 
		{
			ContentValues values;
			if (initialValues != null) {
				values = new ContentValues(initialValues);
			} else {
				throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
			}
			SQLiteDatabase db = this.dbHelper.getWritableDatabase();
			
			long newRowId = db.insert(RouteTable.TABLE_NAME, null, values);

			if (newRowId > 0) 
			{ // if rowID is -1, it means the insert failed
				// Build a new Timeline URI with the new tweet's ID appended to it.
				Uri routeUri = ContentUris.withAppendedId(RouteTable.CONTENT_ID_URI_BASE, newRowId);
				// Notify observers that our data changed.
				getContext().getContentResolver().notifyChange(routeUri, null);
				return routeUri;
			}

			throw new SQLException("Failed to insert row into " + uri); // Insert failed: halt and catch fire.
		}
		else
		if (uriMatcher.match(uri) == DIRECTION)
		{
			
			ContentValues values;
			if (initialValues != null) {
				values = new ContentValues(initialValues);
			} else {
				throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
			}
			SQLiteDatabase db = this.dbHelper.getWritableDatabase();
			
			long newRowId = db.insert(DirectionTable.TABLE_NAME, null, values);

			if (newRowId > 0) 
			{ // if rowID is -1, it means the insert failed
				// Build a new Timeline URI with the new tweet's ID appended to it.
				Uri directionUri = ContentUris.withAppendedId(DirectionTable.CONTENT_ID_URI_BASE, newRowId);
				// Notify observers that our data changed.
				getContext().getContentResolver().notifyChange(directionUri, null);
				return directionUri;
			}

			throw new SQLException("Failed to insert row into " + uri); // Insert failed: halt and catch fire.
		}
		else
		if (uriMatcher.match(uri) == STOP)
		{
			
			ContentValues values;
			if (initialValues != null) {
				values = new ContentValues(initialValues);
			} else {
				throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
			}
			SQLiteDatabase db = this.dbHelper.getWritableDatabase();
			
			long newRowId = db.insert(StopTable.TABLE_NAME, null, values);

			if (newRowId > 0) 
			{ // if rowID is -1, it means the insert failed
				// Build a new Timeline URI with the new tweet's ID appended to it.
				Uri stopUri = ContentUris.withAppendedId(StopTable.CONTENT_ID_URI_BASE, newRowId);
				// Notify observers that our data changed.
				getContext().getContentResolver().notifyChange(stopUri, null);
				return stopUri;
			}

			throw new SQLException("Failed to insert row into " + uri); // Insert failed: halt and catch fire.
		}
		else
		{
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] selectedColumns, String whereClause, String[] whereValues, String sortOrder) 
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		// Choose the projection and adjust the "where" clause based on URI pattern-matching.
		switch (uriMatcher.match(uri)) 
		{
			case ROUTE:
				qb.setTables(RouteTable.TABLE_NAME);
				qb.setProjectionMap(routeProjectionMap);
				break;

			case DIRECTION:
				qb.setTables(DirectionTable.TABLE_NAME);
				qb.setProjectionMap(directionProjectionMap);
				break;
				
			case STOP:
				qb.setTables(StopTable.TABLE_NAME);
				qb.setProjectionMap(stopProjectionMap);
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		// the two nulls here are 'grouping' and 'filtering by group'
		Cursor cursor = qb.query(db, selectedColumns, whereClause, whereValues, null, null, sortOrder);

		// Tell the Cursor about the URI to watch, so it knows when its source data changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues updateValues, String whereClause, String[] whereValues) 
	{
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		int updatedRowsCount;
		String finalWhere;

		switch (uriMatcher.match(uri)) 
		{
			case ROUTE:
				// Perform the update and return the number of rows updated.
				updatedRowsCount = db.update(RouteTable.TABLE_NAME, updateValues, whereClause, whereValues);
				break;

			case DIRECTION:
				updatedRowsCount = db.update(DirectionTable.TABLE_NAME, updateValues, whereClause, whereValues);
				break;
				
			case STOP:
				updatedRowsCount = db.update(StopTable.TABLE_NAME, updateValues, whereClause, whereValues);
				break;
				
			default:
				// Incoming URI pattern is invalid: halt & catch fire.
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (updatedRowsCount > 0) 
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}

		// Returns the number of rows updated.
		return updatedRowsCount;
	}

	//Default bulkInsert is terrible.  Make it better!
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) 
	{
		this.validateOrThrow(uri);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.beginTransaction();
		int insertedCount = 0;
		long newRowId = -1;
		try 
		{
			for (ContentValues cv : values) 
			{
				newRowId = this.insert(uri, cv, db);
				insertedCount++;
			}
			db.setTransactionSuccessful();
			// Build a new Node URI appended with the row ID of the last node to get inserted in the batch
			switch (uriMatcher.match(uri)) 
			{
				case ROUTE:
					Uri nodeUri = ContentUris.withAppendedId(RouteTable.CONTENT_ID_URI_BASE, newRowId);
					// Notify observers that our data changed.
					getContext().getContentResolver().notifyChange(nodeUri, null);
					return insertedCount;
					
				case DIRECTION:
					Uri directionNodeUri = ContentUris.withAppendedId(DirectionTable.CONTENT_ID_URI_BASE, newRowId);
					// Notify observers that our data changed.
					getContext().getContentResolver().notifyChange(directionNodeUri, null);
					return insertedCount;
					
				case STOP:
					Uri stopNodeUri = ContentUris.withAppendedId(StopTable.CONTENT_ID_URI_BASE, newRowId);
					// Notify observers that our data changed.
					getContext().getContentResolver().notifyChange(stopNodeUri, null);
					return insertedCount;
					
				default:
					// Incoming URI pattern is invalid: halt & catch fire.
					throw new IllegalArgumentException("Unknown URI " + uri);
			}					
		} 
		finally 
		{
			db.endTransaction();
		}
	}

	//Used by our implementation of builkInsert
	private long insert(Uri uri, ContentValues values, SQLiteDatabase writableDb) 
	{
		if (values == null) 
		{
			throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
		}
		long newRowId = -1;
		switch (uriMatcher.match(uri)) 
		{
			case ROUTE:
				newRowId = writableDb.insert(RouteTable.TABLE_NAME, null, values);
				break;
				
			case DIRECTION:
				newRowId = writableDb.insert(DirectionTable.TABLE_NAME, null, values);
				break;
				
			case STOP:
				newRowId = writableDb.insert(StopTable.TABLE_NAME, null, values);
				break;
				
			default:
				// Incoming URI pattern is invalid: halt & catch fire.
				throw new IllegalArgumentException("Unknown URI " + uri);

		}					
		if (newRowId == -1) 
		{
			throw new SQLException("Failed to insert row into " + uri); // Insert failed: halt and catch fire.
		}
		return newRowId;
	}

	private void validateOrThrow(Uri uri) 
	{
		if (uriMatcher.match(uri) != ROUTE
		&&  uriMatcher.match(uri) != DIRECTION
		&&  uriMatcher.match(uri) != STOP)
		{
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
	}

	@Override
	public String getType(Uri uri) 
	{
		switch (uriMatcher.match(uri)) 
		{
			case ROUTE:
				return ROUTE_CONTENT_TYPE;
				
			case DIRECTION:
				return DIRECTION_CONTENT_TYPE;
				
			case STOP:
				return STOP_CONTENT_TYPE;
				
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException 
	{
		File file = new File(this.getContext().getFilesDir(), uri.getPath());
		ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
		return parcel;
	}

}
