package com.quasma.android.bustrip.activity;

import com.quasma.android.bustrip.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class BaseListActivity extends ListActivity
{
	public static final int menu_REFRESH 	= Menu.FIRST + 1;
	public static final int menu_HOME		= Menu.FIRST + 2;
	public static final int menu_SETTINGS   = Menu.FIRST + 3;

	private ProgressBar progressBar; 
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		currentTheme = getThemePreference(this);
		setTheme(currentTheme);
		super.onCreate(savedInstanceState);
	}

	static int getThemePreference(Context context)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
   	 	if (prefs.getString("theme_selector", "Light").equals("Light"))
   	 	{
   	 		return android.R.style.Theme_Light_NoTitleBar;
   	 	}
   	 	else
   	 	{
   	 		return android.R.style.Theme_Black_NoTitleBar;
   	 	}
	}
	
	protected void setProgressBar(ProgressBar progressBar)
	{
		this.progressBar = progressBar;
	}
	
	private Integer currentTheme = null;
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		int preferenceTheme = getThemePreference(this); 
		if (currentTheme != preferenceTheme)
		{
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	@Override
	public void setTitle(CharSequence title)
	{
		this.title.setText(title);
	}
	protected void setUp(int layout, boolean favorite)
	{
		setContentView(layout);
		progressBar = (ProgressBar) findViewById(android.R.id.progress);
		title = (TextView) findViewById(android.R.id.title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(menu_REFRESH, menu_REFRESH, menu.size(), getString(R.string.refresh)).setIcon(R.drawable.ic_menu_refresh);
		menu.add(menu_SETTINGS, menu_SETTINGS, menu.size(), getString(R.string.settings)).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(menu_HOME, menu_HOME, menu.size(), getString(R.string.home)).setIcon(R.drawable.ic_menu_home);
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
				
			case menu_REFRESH:
				refresh();
				return true;
				
			case menu_HOME:
				Intent homeActivity = new Intent(getBaseContext(), MyNexTripActivity.class);
				startActivity(homeActivity);
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public abstract void refresh();
	
	protected void setProgressSpinnerVisibility(boolean flag)
	{
		progressBar.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
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
