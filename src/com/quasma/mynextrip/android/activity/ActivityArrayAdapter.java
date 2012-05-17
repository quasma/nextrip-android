package com.quasma.mynextrip.android.activity;

import java.util.List;
import java.util.Map;

import com.quasma.mynextrip.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class ActivityArrayAdapter extends ArrayAdapter 
{
	private final Context context;
	private final List<Map> values;
 
	public ActivityArrayAdapter(Context context, List<Map> values) 
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
 
		View rowView = inflater.inflate(R.layout.main_list_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.text);
		textView.setText(values.get(position).get("title").toString());
		return rowView;
	}
}
