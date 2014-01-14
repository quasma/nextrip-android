package com.quasma.android.bustrip.activity;

import com.quasma.android.bustrip.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class BaseListActivity extends ListActivity
{
	public static final int menu_REFRESH 	= Menu.FIRST + 1;
	public static final int menu_HOME		= Menu.FIRST + 2;
	public static final int menu_SETTINGS   = Menu.FIRST + 3;

	private ProgressBar progressBar; 
	private TextView title;

	protected void setProgressBar(ProgressBar progressBar)
	{
		this.progressBar = progressBar;
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
	
	private void finish(Activity activity)
	{
		if (activity == null)
			return;
		
		if (activity.getClass() == MyNexTripActivity.class)
			return;
		
		finish(activity.getParent());
		activity.finish();
	}
	
	private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		 return gestureDetector.onTouchEvent(event);
	}
	
    private final class GestureListener extends SimpleOnGestureListener 
    {
		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) 
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
        {
            boolean result = false;
            try 
            {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                
                if (Math.abs(diffX) > Math.abs(diffY)) 
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) 
                    {
                        if (diffX > 0) 
                        {
                            onSwipeRight();
                        } 
                        else 
                        {
                            onSwipeLeft();
                        }
                    }
                } 
                else 
                {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) 
                    {
                        if (diffY > 0) 
                        {
                            onSwipeBottom();
                        } 
                        else 
                        {
                            onSwipeTop();
                        }
                    }
                }
            } 
            catch (Exception exception) 
            {
                exception.printStackTrace();
            }
            return result;
        }
    }

	public void onSwipeRight() 
	{
		finish();
	}
	
	public void onSwipeLeft() 
	{
	}
	
	public void onSwipeTop() 
	{
	}
	
	public void onSwipeBottom() 
	{
	}

}
