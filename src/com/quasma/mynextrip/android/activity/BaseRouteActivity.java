package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.R;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseRouteActivity extends BaseActivity
{
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(menu_REFRESH, menu_REFRESH, menu.size(), getString(R.string.refresh)).setIcon(R.drawable.ic_menu_refresh);
		menu.add(menu_HOME, menu_HOME, menu.size(), getString(R.string.home)).setIcon(R.drawable.ic_menu_home);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
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
}
