package com.quasma.android.bustrip.providers;

import java.util.HashMap;

import com.quasma.android.bustrip.providers.DirectionProviderContract.DirectionTable;
import com.quasma.android.bustrip.providers.FavoriteProviderContract.FavoriteTable;
import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;
import com.quasma.android.bustrip.providers.StopNumberProviderContract.StopNumberTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NexTripProvider extends ContentProvider
{
	private static final int ROUTE 		= 1;
	private static final int DIRECTION 	= 2;
	private static final int STOP 		= 3;
	private static final int STOPNUMBER = 4;
	private static final int FAVORITE	= 5;
	
	private static HashMap<String, String> routeProjectionMap;
	private static HashMap<String, String> directionProjectionMap;
	private static HashMap<String, String> stopProjectionMap;
	private static HashMap<String, String> stopNumberProjectionMap;
	private static HashMap<String, String> favoriteProjectionMap;
	
	private ProviderDbHelper dbHelper;
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static
	{
		
		uriMatcher.addURI(RouteProviderContract.AUTHORITY, RouteTable.TABLE_NAME, ROUTE);
		uriMatcher.addURI(DirectionProviderContract.AUTHORITY, DirectionTable.TABLE_NAME, DIRECTION);
		uriMatcher.addURI(StopProviderContract.AUTHORITY, StopTable.TABLE_NAME, STOP);
		uriMatcher.addURI(StopNumberProviderContract.AUTHORITY, StopNumberTable.TABLE_NAME, STOPNUMBER);
		uriMatcher.addURI(FavoriteProviderContract.AUTHORITY, FavoriteTable.TABLE_NAME, FAVORITE);
		

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
		stopNumberProjectionMap = new HashMap<String, String>();
		for (String column : StopNumberTable.ALL_COLUMNS) 
		{
			stopNumberProjectionMap.put(column, column);
		}
		favoriteProjectionMap = new HashMap<String, String>();
		for (String column : FavoriteTable.ALL_COLUMNS) 
		{
			favoriteProjectionMap.put(column, column);
		}
	}
	
	private static final String ROUTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.route";
	private static final String ROUTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.route";
	private static final String DIRECTION_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.direction";
	private static final String DIRECTION_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.direction";
	private static final String STOP_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.stop";
	private static final String STOP_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.stop";
	private static final String STOPNUMBER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.stopnumber";
	private static final String STOPNUMBER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.stopnumber";
	private static final String FAVORITE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mynextrip.favorite";
	private static final String FAVORITE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mynextrip.favorite";
	
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
			
			case STOPNUMBER:
				deletedRowsCount = db.delete(StopNumberTable.TABLE_NAME, whereClause, whereValues);
				break;
				
			case FAVORITE:
				deletedRowsCount = db.delete(FavoriteTable.TABLE_NAME, whereClause, whereValues);
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return deletedRowsCount;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		long newRowId = -1;
		Uri  uriBase;
		
		switch (uriMatcher.match(uri))
		{
			case ROUTE:
				newRowId = db.insert(RouteTable.TABLE_NAME, null, values);
				uriBase = RouteTable.CONTENT_ID_URI_BASE;
				break;
				
			case DIRECTION:
				newRowId = db.insert(DirectionTable.TABLE_NAME, null, values);
				uriBase = DirectionTable.CONTENT_ID_URI_BASE;
				break;
				
			case STOP:
				try
				{
					newRowId = db.insertWithOnConflict(StopTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
				}
				catch (SQLException e)
				{
					db.update(StopTable.TABLE_NAME, values, 
							StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? AND " + StopTable.STOP + " LIKE ?",
							new String[] { values.getAsString(StopTable.ROUTE), values.getAsString(StopTable.DIRECTION), values.getAsString(StopTable.STOP) });
					
					Cursor cursor = null;
					try
					{
						cursor = db.query(StopTable.TABLE_NAME, 
							new String[] { StopTable._ID },
							StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? AND " + StopTable.STOP + " LIKE ?",
							new String[] { values.getAsString(StopTable.ROUTE), values.getAsString(StopTable.DIRECTION), values.getAsString(StopTable.STOP) }, 
							null, null, null);
					
						if (cursor.moveToFirst())
							newRowId = cursor.getLong(0);
					}
					finally
					{
						if (cursor != null && !cursor.isClosed())
							cursor.close();
					}
				}
				uriBase = StopTable.CONTENT_ID_URI_BASE;
				break;
				
			case STOPNUMBER:
				try
				{
					newRowId = db.insertWithOnConflict(StopNumberTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
				}
				catch (SQLException e)
				{
					db.update(StopNumberTable.TABLE_NAME, values, 
							StopNumberTable.STOPNUMBER + " LIKE ?", new String[] { values.getAsString(StopNumberTable.STOPNUMBER) });

					Cursor cursor = null;
					try
					{
						cursor = db.query(StopNumberTable.TABLE_NAME, 
							new String[] { StopNumberTable._ID }, StopNumberTable.STOPNUMBER + " LIKE ?", new String[] { values.getAsString(StopNumberTable.STOPNUMBER) }, 
							null, null, null);
					
						if (cursor.moveToFirst())
							newRowId = cursor.getLong(0);
					}
					finally
					{
						if (cursor != null && !cursor.isClosed())
							cursor.close();
					}
				}
				uriBase = StopNumberTable.CONTENT_ID_URI_BASE;
				break;
				
			case FAVORITE:
				newRowId = db.insert(FavoriteTable.TABLE_NAME, null, values);
				uriBase = FavoriteTable.CONTENT_ID_URI_BASE;
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (newRowId > 0) 
		{ 
			Uri rtnUri = ContentUris.withAppendedId(uriBase, newRowId);
			getContext().getContentResolver().notifyChange(rtnUri, null);
			return rtnUri;
		}
		throw new SQLException("Failed to insert row into " + uri); // Insert failed: halt and catch fire.
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
				
			case STOPNUMBER:
				qb.setTables(StopNumberTable.TABLE_NAME);
				qb.setProjectionMap(stopNumberProjectionMap);
				break;
				
			case FAVORITE:
				qb.setTables(FavoriteTable.TABLE_NAME);
				qb.setProjectionMap(favoriteProjectionMap);
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
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
				
			case STOPNUMBER:
				updatedRowsCount = db.update(StopNumberTable.TABLE_NAME, updateValues, whereClause, whereValues);
				break;

			case FAVORITE:
				updatedRowsCount = db.update(FavoriteTable.TABLE_NAME, updateValues, whereClause, whereValues);
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
				this.insert(uri, cv, db);
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
					
				case STOPNUMBER:
					Uri stopNumberNodeUri = ContentUris.withAppendedId(StopNumberTable.CONTENT_ID_URI_BASE, newRowId);
					// Notify observers that our data changed.
					getContext().getContentResolver().notifyChange(stopNumberNodeUri, null);
					return insertedCount;
					
				case FAVORITE:
					Uri favoriteNodeUri = ContentUris.withAppendedId(FavoriteTable.CONTENT_ID_URI_BASE, newRowId);
					// Notify observers that our data changed.
					getContext().getContentResolver().notifyChange(favoriteNodeUri, null);
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
	private void insert(Uri uri, ContentValues values, SQLiteDatabase writableDb) 
	{
		if (values == null) 
		{
			throw new SQLException("ContentValues arg for .insert() is null, cannot insert row.");
		}
		long newRowId = -1;
		switch (uriMatcher.match(uri)) 
		{
			case ROUTE:
				try
				{
					newRowId = writableDb.insertWithOnConflict(RouteTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
				}
				catch (SQLException e)
				{
					writableDb.update(RouteTable.TABLE_NAME, values,
						RouteTable.ROUTE + " LIKE ?", 
						new String[] { values.getAsString(RouteTable.ROUTE) });
				}
				break;
				
			case DIRECTION:
				try
				{
					newRowId = writableDb.insertWithOnConflict(DirectionTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
				}
				catch (SQLException e)
				{
					writableDb.update(DirectionTable.TABLE_NAME, values,
						DirectionTable.ROUTE + " LIKE ? AND " + DirectionTable.DIRECTION + " LIKE ?", 
						new String[] { values.getAsString(DirectionTable.ROUTE), values.getAsString(DirectionTable.DIRECTION) });
				}
				break;
				
			case STOP:
				
				try
				{
					newRowId = writableDb.insertWithOnConflict(StopTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
				}
				catch (SQLException e)
				{
					writableDb.update(StopTable.TABLE_NAME, values,
						StopTable.ROUTE + " LIKE ? AND " + StopTable.DIRECTION + " LIKE ? AND " + StopTable.STOP + " LIKE ?", 
						new String[] { values.getAsString(StopTable.ROUTE), values.getAsString(StopTable.DIRECTION), values.getAsString(StopTable.STOP) });
				}
				break;
				
			case STOPNUMBER:
				try
				{
					newRowId = writableDb.insertWithOnConflict(StopNumberTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
				}
				catch (SQLException e)
				{
					writableDb.update(StopNumberTable.TABLE_NAME, values,
							StopNumberTable.STOPNUMBER + " LIKE ?", 
							new String[] { values.getAsString(StopNumberTable.STOPNUMBER) });
				}
				break;
				
			case FAVORITE:
				newRowId = writableDb.insert(FavoriteTable.TABLE_NAME, null, values);
				break;

			default:
				// Incoming URI pattern is invalid: halt & catch fire.
				throw new IllegalArgumentException("Unknown URI " + uri);

		}					
	}

	private void validateOrThrow(Uri uri) 
	{
		if (uriMatcher.match(uri) != ROUTE
		&&  uriMatcher.match(uri) != DIRECTION
		&&  uriMatcher.match(uri) != STOP
		&&  uriMatcher.match(uri) != STOPNUMBER
		&&  uriMatcher.match(uri) != FAVORITE)
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
				
			case STOPNUMBER:
				return STOPNUMBER_CONTENT_TYPE;
				
			case FAVORITE:
				return FAVORITE_CONTENT_TYPE;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
}
