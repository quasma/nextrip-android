package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.R;

import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseActivity extends ListActivity
{
	public static final int menu_REFRESH 	= Menu.FIRST + 1;
	public static final int menu_HOME		= Menu.FIRST + 2;
	public static final int menu_SETTINGS   = Menu.FIRST + 3;
	public static final int menu_ADD		= Menu.FIRST + 4;
	public static final int menu_DELETE 	= Menu.FIRST + 5;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(menu_SETTINGS, menu_SETTINGS, menu.size(), getString(R.string.settings)).setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case menu_SETTINGS:
				Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
				startActivity(settingsActivity);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void debug(String msg)
	{
		Log.d(this.getClass().getSimpleName(), msg);
	}
	
	protected void error(String msg, Throwable t)
	{
		Log.e(this.getClass().getSimpleName(), msg, t);
	}
	
	protected void warn(String msg)
	{
		Log.w(this.getClass().getSimpleName(), msg);
	}
	
	protected void info(String msg)
	{
		Log.i(this.getClass().getSimpleName(), msg);
	}

}
