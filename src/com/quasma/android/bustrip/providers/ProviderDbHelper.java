package com.quasma.android.bustrip.providers;

import java.io.File;

import com.quasma.android.bustrip.providers.DirectionProviderContract.DirectionTable;
import com.quasma.android.bustrip.providers.FavoriteProviderContract.FavoriteTable;
import com.quasma.android.bustrip.providers.NexTrip.Favorite;
import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;
import com.quasma.android.bustrip.providers.StopNumberProviderContract.StopNumberTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProviderDbHelper extends SQLiteOpenHelper
{
	public final String TAG = getClass().getSimpleName();

	//Name of the database file
	private static final String DATABASE_NAME = "mynextrip.db";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	
	public ProviderDbHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + RouteTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + DirectionTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + StopTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + StopNumberTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + FavoriteTable.TABLE_NAME + ";");
		createTables(db);
		upgradeFavorites(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + RouteTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + DirectionTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + StopTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + StopNumberTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + FavoriteTable.TABLE_NAME + ";");
		createTables(db);
	}

	public void createTables(SQLiteDatabase db) 
	{		
		// CREATE ROUTE TABLE
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + RouteTable.TABLE_NAME + " (");
		sqlBuilder.append(RouteTable._ID + " integer primary key, ");
		sqlBuilder.append(RouteTable.ROUTE + " INTEGER not null, ");
		sqlBuilder.append(RouteTable.DESC + " TEXT, ");
		sqlBuilder.append(RouteTable.CREATED + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + RouteTable.ROUTE + ")");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);

		// CREATE DIRECTION TABLE
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + DirectionTable.TABLE_NAME + " (");
		sqlBuilder.append(DirectionTable._ID + "  integer primary key, ");
		sqlBuilder.append(DirectionTable.ROUTE + " TEXT, ");
		sqlBuilder.append(DirectionTable.DIRECTION + " TEXT, ");
		sqlBuilder.append(DirectionTable.DESC + " TEXT, ");
		sqlBuilder.append(DirectionTable.CREATED + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + DirectionTable.ROUTE + ", " + DirectionTable.DIRECTION + ")");
		sqlBuilder.append(");");
		sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);

		// CREATE STOP TABLE
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + StopTable.TABLE_NAME + " (");
		sqlBuilder.append(StopTable._ID + "  integer primary key, ");
		sqlBuilder.append(StopTable.ROUTE + " TEXT, ");
		sqlBuilder.append(StopTable.DIRECTION + " TEXT, ");
		sqlBuilder.append(StopTable.STOP + " TEXT, ");
		sqlBuilder.append(StopTable.DESC + " TEXT, ");
		sqlBuilder.append(StopTable.CREATED + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + StopTable.ROUTE + ", " + StopTable.DIRECTION + ", " + StopTable.STOP + ")");
		sqlBuilder.append(");");
		sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);
		
		// CREATE STOPNUMBER TABLE
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + StopNumberTable.TABLE_NAME + " (");
		sqlBuilder.append(StopNumberTable._ID + "  integer primary key, ");
		sqlBuilder.append(StopNumberTable.STOPNUMBER + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + StopNumberTable.STOPNUMBER + ")");
		sqlBuilder.append(");");
		sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);

		// CREATE FAVORITE TABLE
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + FavoriteTable.TABLE_NAME + " (");
		sqlBuilder.append(FavoriteTable._ID + "  integer primary key, ");
		sqlBuilder.append(FavoriteTable.TABLE + " TEXT, ");
		sqlBuilder.append(FavoriteTable.KEY + " INTEGER, ");
		sqlBuilder.append(FavoriteTable.DESC + " TEXT, ");
		sqlBuilder.append("UNIQUE (" + FavoriteTable.TABLE + ", " + FavoriteTable.KEY + ")");
		sqlBuilder.append(");");
		sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);
	}
	
	private void upgradeFavorites(SQLiteDatabase db)
	{
		
		File cache = new File(context.getDir("cache", Context.MODE_PRIVATE), "favorites");
		if (!cache.exists())
			return;

		NexTrip nexTrip = new NexTrip(context, cache);
		for (int i = 0; i < nexTrip.getFavorites().size(); i++)
		{
			Favorite favorite = nexTrip.getFavorites().get(i);
			ContentValues stopData = new ContentValues();
			stopData.put(StopTable.ROUTE, favorite.getRoute());
			stopData.put(StopTable.DIRECTION, favorite.getDirection());
			stopData.put(StopTable.STOP, favorite.getStop());
			long rowId = db.insert(StopTable.TABLE_NAME, null, stopData);
			
			ContentValues favoriteData = new ContentValues();
			favoriteData.put(FavoriteTable.DESC, 
					favorite.routeNumber + " - " + (favorite.routeDirection.substring(0, 1) + favorite.routeDirection.toLowerCase().substring(1)) + " - " + favorite.stop);
			favoriteData.put(FavoriteTable.TABLE, StopTable.TABLE_NAME);
			favoriteData.put(FavoriteTable.KEY, rowId);
			db.insert(FavoriteTable.TABLE_NAME, null, favoriteData);
		}
		cache.delete();
	}
}
