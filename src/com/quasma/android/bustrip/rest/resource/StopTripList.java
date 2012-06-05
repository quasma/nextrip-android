package com.quasma.android.bustrip.rest.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.quasma.android.bustrip.providers.StopNumberProviderContract.StopNumberTable;
import com.quasma.android.bustrip.providers.StopProviderContract.StopTable;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class StopTripList implements Resource, Parcelable
{
	private List<Trip> trips;
	private String stopNumber = null;
	
	public StopTripList(Parcel source)
	{
		stopNumber 		= source.readString();
		trips = new ArrayList<Trip>();
		source.readTypedList(trips, Trip.CREATOR);
	}
	
	public StopTripList(String stopNumber, String json) throws JSONException 
	{
		this.stopNumber 		= stopNumber;
		
		JSONArray jsonArray = new JSONArray(json);
		trips = new ArrayList<Trip>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++)
			trips.add(new Trip( 
					jsonArray.getJSONObject(i).getString("Route"),
					jsonArray.getJSONObject(i).getString("RouteDirection"),
					jsonArray.getJSONObject(i).getString("Description"),
					jsonArray.getJSONObject(i).getBoolean("Actual"),
					jsonArray.getJSONObject(i).getString("DepartureText"),
					jsonArray.getJSONObject(i).getString("DepartureTime"),
					jsonArray.getJSONObject(i).getString("Terminal")));
		
	}

	public List<Trip> getTrips() 
	{
		return trips;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
	
	public String getStopNumber()
	{
		return stopNumber;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(stopNumber);
		dest.writeTypedList(getTrips());
	}
	
	public static final Parcelable.Creator<StopTripList> CREATOR = new Parcelable.Creator<StopTripList>() 
	{
	    public StopTripList createFromParcel(Parcel in) 
	    {
	        return new StopTripList(in);
	    }

	    public StopTripList[] newArray(int size) 
	    {
	        return new StopTripList[size];
	    }
	};
	
	public ContentValues toContentValues(long time) 
	{
		ContentValues rowData = new ContentValues();
		rowData.put(StopNumberTable.STOPNUMBER, this.stopNumber);
		return rowData;
	}


}
