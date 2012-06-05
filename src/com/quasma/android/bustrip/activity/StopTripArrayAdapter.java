package com.quasma.android.bustrip.activity;

import java.text.MessageFormat;
import java.util.List;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.rest.resource.Trip;

import android.content.Context;
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
		
		if (values.get(position).isActual())
		{
			listItemView =  LayoutInflater.from(context).inflate(R.layout.stoptrips_actual_list_item, parent, false);
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
		departure.setText(values.get(position).getDeparture());
		return listItemView;
	}
}
