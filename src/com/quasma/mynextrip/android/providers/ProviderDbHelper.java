package com.quasma.mynextrip.android.providers;

import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;
import com.quasma.mynextrip.android.providers.RouteProviderContract.RouteTable;
import com.quasma.mynextrip.android.providers.StopProviderContract.StopTable;

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

	public ProviderDbHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + RouteTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + DirectionTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + StopTable.TABLE_NAME + ";");
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + RouteTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + DirectionTable.TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + StopTable.TABLE_NAME + ";");
		createTables(db);
	}

	public void createTables(SQLiteDatabase db) 
	{		
		// CREATE ROUTE TABLE
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + RouteTable.TABLE_NAME + " (");
		sqlBuilder.append(RouteTable._ID + " INTEGER, ");
		sqlBuilder.append(RouteTable.ROUTE + " INTEGER, ");
		sqlBuilder.append(RouteTable.DESC + " TEXT, ");
		sqlBuilder.append(RouteTable.URL + " TEXT, ");
		sqlBuilder.append(RouteTable.CREATED + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + RouteTable.ROUTE + ") ON CONFLICT REPLACE");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);

		// CREATE DIRECTION TABLE
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + DirectionTable.TABLE_NAME + " (");
		sqlBuilder.append(DirectionTable._ID + " INTEGER, ");
		sqlBuilder.append(DirectionTable.ROUTE + " TEXT, ");
		sqlBuilder.append(DirectionTable.DIRECTION + " TEXT, ");
		sqlBuilder.append(DirectionTable.DESC + " TEXT, ");
		sqlBuilder.append(DirectionTable.URL + " TEXT, ");
		sqlBuilder.append(DirectionTable.CREATED + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + DirectionTable.ROUTE + ", " + DirectionTable.DIRECTION + ") ON CONFLICT REPLACE");
		sqlBuilder.append(");");
		sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);

		// CREATE STOP TABLE
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + StopTable.TABLE_NAME + " (");
		sqlBuilder.append(StopTable._ID + " INTEGER, ");
		sqlBuilder.append(StopTable.ROUTE + " TEXT, ");
		sqlBuilder.append(StopTable.DIRECTION + " TEXT, ");
		sqlBuilder.append(StopTable.STOP + " TEXT, ");
		sqlBuilder.append(StopTable.STOPNUMBER + " INTEGER, ");
		sqlBuilder.append(StopTable.DESC + " TEXT, ");
		sqlBuilder.append(StopTable.URL + " TEXT, ");
		sqlBuilder.append(StopTable.CREATED + " INTEGER, ");
		sqlBuilder.append("UNIQUE (" + StopTable.ROUTE + ", " + StopTable.DIRECTION + ", " + StopTable.STOP + ") ON CONFLICT REPLACE");
		sqlBuilder.append(");");
		sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);

	}
}
