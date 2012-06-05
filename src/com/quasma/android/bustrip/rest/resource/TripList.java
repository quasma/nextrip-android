package com.quasma.android.bustrip.rest.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;

public class TripList implements Resource, Parcelable
{
	private List<Trip> trips;
	private String route = null;
	private String direction = null;
	private String stop = null;
	
	public TripList(Parcel source)
	{
		route 		= source.readString();
		direction 	= source.readString();
		stop        = source.readString();
		
		trips = new ArrayList<Trip>();
		source.readTypedList(trips, Trip.CREATOR);
	}
	
	public TripList(String route, String direction, String stop, String json) throws JSONException 
	{
		this.route 		= route;
		this.direction 	= direction;
		this.stop 		= stop;
		
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
	
	public String getRoute()
	{
		return route;
	}
	
	public String getDirection()
	{
		return direction;
	}
	
	public String getStop()
	{
		return stop;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(route);
		dest.writeString(direction);
		dest.writeString(stop);
		dest.writeTypedList(getTrips());
	}
	
	public static final Parcelable.Creator<TripList> CREATOR = new Parcelable.Creator<TripList>() 
	{
	    public TripList createFromParcel(Parcel in) 
	    {
	        return new TripList(in);
	    }

	    public TripList[] newArray(int size) 
	    {
	        return new TripList[size];
	    }
	};

}
