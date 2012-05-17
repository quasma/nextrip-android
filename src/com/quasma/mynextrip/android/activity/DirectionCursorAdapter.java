package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.R;
import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DirectionCursorAdapter extends CursorAdapter 
{
	private Application app;

	public DirectionCursorAdapter(Context context, Cursor c) 
	{
		super(context, c);
		app = (Application) context.getApplicationContext();
	}

	static class ViewHolder {
		TextView nameView;		
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) 
	{
		String name = cursor.getString(cursor.getColumnIndex(DirectionTable.DESC));
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.nameView.setText(name.substring(0,  1) + name.substring(1).toLowerCase());	
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) 
	{
		View listItemView =  LayoutInflater.from(context).inflate(R.layout.selection_list_item, parent, false);

		ViewHolder holder = new ViewHolder();
		holder.nameView = (TextView) listItemView.findViewById(R.id.text);

		listItemView.setTag(holder);

		return listItemView;
	}
}
