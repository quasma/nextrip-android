package com.quasma.android.bustrip.activity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.service.NexTripService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyNexTripActivity extends Activity
{
	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);

	        	((ImageButton)findViewById(R.id.findstop)).setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						doStopNumber((EditText) findViewById(R.id.stopnumber));
					}
				});

				((Button)findViewById(R.id.routelisting)).setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						doRouteList();
					}
				});

				((Button)findViewById(R.id.favoritelisting)).setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						doFavorites();
					}
				});

				((Button)findViewById(R.id.about)).setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						doAbout();
					}
				});	    
				

		}

	    
	    
		public static final int menu_SETTINGS   = Menu.FIRST + 1;

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
		private void doStopNumber(EditText stopnumber)
		{
	    	try
	    	{
		    	if (Integer.parseInt(stopnumber.getText().toString()) > 0)
		    	{
    		        Intent intent = new Intent();
    				intent.putExtra(NexTripService.STOPNUMBER_EXTRA, stopnumber.getText().toString());
    		        intent.setClass(this, (Class) StopTripActivity.class);
    		        startActivity(intent);
		    	}
	    	}
	    	catch (NumberFormatException e) {}
		}
		
		private void doRouteList()
		{
	        Intent intent = new Intent();
	        intent.setClass(this, RouteActivity.class);
	        startActivity(intent);
		}
		
		private void doFavorites()
		{
	        Intent intent = new Intent();
	        intent.setClass(this, FavoriteActivity.class);
	        startActivity(intent);
		}
		
		private void doAbout()
		{
			Dialog about = new Dialog(this, R.style.AboutTheme);
			about.setContentView(R.layout.about);
			TextView view = (TextView) about.findViewById(R.id.abouttext);
			try
			{
				Reader reader = new InputStreamReader(getAssets().open("about.html"));
				Writer writer = new StringWriter();
				char[] buffer = new char[2048];
				try
				{
					int n;
					while ((n = reader.read(buffer)) != -1)
					{
						writer.write(buffer, 0, n);
					}
				}
				finally
				{
					reader.close();
				}
				view.setText(Html.fromHtml(writer.toString()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			about.show();
		}
}
