package com.quasma.android.bustrip.activity;

import com.quasma.android.bustrip.R;
import com.quasma.android.bustrip.providers.RouteProviderContract.RouteTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class StopCursorAdapter extends CursorAdapter 
{
	public StopCursorAdapter(Context context, Cursor c) 
	{
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) 
	{
		String name = cursor.getString(cursor.getColumnIndex(StopTable.DESC));
		int id 		= cursor.getInt(cursor.getColumnIndex(RouteTable._ID));

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.nameView.setText(name);
	}
	static class ViewHolder {
		TextView nameView;
	}

	@Override
	public View newView(Context context, Cursor cursor, final ViewGroup parent) 
	{
		View listItemView =  LayoutInflater.from(context).inflate(R.layout.selection_list_item, parent, false);
		
		ViewHolder holder = new ViewHolder();
		holder.nameView = (TextView) listItemView.findViewById(R.id.text);
		listItemView.setTag(holder);
		return listItemView;
	}

}
