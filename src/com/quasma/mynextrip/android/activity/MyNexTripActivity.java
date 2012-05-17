package com.quasma.mynextrip.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quasma.mynextrip.android.R;
import com.quasma.mynextrip.android.service.NexTripService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MyNexTripActivity extends BaseActivity
{
	   @Override
	    public void onCreate(Bundle savedInstanceState) 
	   {
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.main);
	        setListAdapter(new ActivityArrayAdapter(this, getActivities()));
	    }

	    protected List getActivities() 
	    {
	        List<Map> activities = new ArrayList<Map>();
	        { // Routes
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("title", getString(R.string.choose_route));
	        	map.put("class", RouteActivity.class);
	        	activities.add(map);
	        }
	        { // Stops
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("title", getString(R.string.choose_by_stop));
	        	map.put("class", StopNumberActivity.class);
	        	activities.add(map);
	        }
	        { // Favorites
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("title", getString(R.string.choose_favorite));
//	        	map.put("class", RouteActivity.class);
	        	activities.add(map);
	        }
	        { // Location
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("title", getString(R.string.choose_location));
//	        	map.put("class", RouteActivity.class);
	        	activities.add(map);
	        }
	        return activities;
	    }
	    
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) 
		{
	        Map map = (Map) l.getItemAtPosition(position);
	        if (map.containsKey("class") == false)
	        	return;

	        Intent intent = new Intent();
	        intent.setClass(this, (Class) map.get("class"));
	        intent.putExtra(NexTripService.TITLE, (String) map.get("title")); 
	        startActivity(intent);
	    }

}
