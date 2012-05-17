package com.quasma.mynextrip.android.activity;

import java.util.List;

import com.quasma.mynextrip.android.R;
import com.quasma.mynextrip.android.rest.resource.Trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TripArrayAdapter extends ArrayAdapter 
{
	private final Context context;
	private final List<Trip> values;
 
	public TripArrayAdapter(Context context, List<Trip> values) 
	{
		super(context, R.layout.main, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		if (values.get(position).isEnroute())
		{
			View rowView = inflater.inflate(R.layout.enroute_list_item, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.text);
			textView.setText(values.get(position).getTime());
			return rowView;
		}			
		else
		{
			View rowView = inflater.inflate(R.layout.time_list_item, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.text);
			textView.setText(values.get(position).getTime());
			return rowView;
		}			

	}
}
