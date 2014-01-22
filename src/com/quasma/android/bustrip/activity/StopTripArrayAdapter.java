package com.quasma.android.bustrip.activity;

import java.text.MessageFormat;
import java.util.List;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.rest.resource.Trip;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopTripArrayAdapter extends ArrayAdapter 
{
	private final Context context;
	private final List<Trip> values;

	public StopTripArrayAdapter(Context context, List<Trip> values) 
	{
		super(context, R.layout.main, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View listItemView;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (values.get(position).isActual())
		{
			listItemView =  LayoutInflater.from(context).inflate(R.layout.stoptrips_actual_list_item, parent, false);
			if (prefs.getBoolean("lightbackground", true))
				listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.green));
			else
				listItemView.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_dark));				
		}			
		else
		{
			listItemView =  LayoutInflater.from(context).inflate(R.layout.stoptrips_list_item, parent, false);
		}			
		((TextView) listItemView.findViewById(R.id.route)).setText(
				MessageFormat.format(context.getString(R.string.RouteNumber), 
						values.get(position).getRoute() + values.get(position).getTerminal()));
		
		((TextView) listItemView.findViewById(R.id.description)).setText(values.get(position).getDescription());
		
		TextView departure = (TextView) listItemView.findViewById(R.id.time);
		if (values.get(position).isActual())
		{
			if (prefs.getBoolean("lightbackground", true))
				departure.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
			else
				departure.setTextColor(getContext().getResources().getColor(R.color.red));
		}
		departure.setText(values.get(position).getDeparture());
		return listItemView;
	}
}
