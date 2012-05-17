package com.quasma.mynextrip.android.rest.resource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

public class TripList implements Resource, Parcelable
{
	private List<Trip> trips;
	private String route = null;
	private String desc = null;
	private String direction = null;
	private String location = null;
	private String stopnumber = null;
	
	public TripList(Parcel source)
	{
		route 		= source.readString();
		desc 		= source.readString();
		direction 	= source.readString();
		location 	= source.readString();
		stopnumber 	= source.readString();
		
		trips = new ArrayList<Trip>();
		source.readTypedList(trips, Trip.CREATOR);
	}
	
	public TripList(URL url, String html) 
	{
		trips = new ArrayList<Trip>();
		
		{
			String selectExpr = "<span id=\".+?lblRouteNumber\".+?>(.+?)</span>";
			Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher selectMatcher = selectPattern.matcher(html);
			if (selectMatcher.find())
				route = selectMatcher.group(1);
		}
		{
			String selectExpr = "<span id=\".+?lblRouteDescription\".+?>(.+?)</span>";
			Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher selectMatcher = selectPattern.matcher(html);
			if (selectMatcher.find())
				desc = selectMatcher.group(1);
		}
		{
			String selectExpr = "<span id=\".+?lblDirection\".+?>(.+?)</span>";
			Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher selectMatcher = selectPattern.matcher(html);
			if (selectMatcher.find())
				direction = selectMatcher.group(1);
		}
		{
			String selectExpr = "<span id=\".+?lblLocation\".+?>(.+?)</span>";
			Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher selectMatcher = selectPattern.matcher(html);
			if (selectMatcher.find())
				location = selectMatcher.group(1).replace("&amp;", "&");;
		}
		{
			String selectExpr = "<a id=\".+?lnkStopNumber\".+?>(.+?)</a>";
			Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher selectMatcher = selectPattern.matcher(html);
			if (selectMatcher.find())
				stopnumber = selectMatcher.group(1);
		}
				
		String selectExpr = "<span class=\"col3( red)?\">(.+?)</span>";
		Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
		Matcher selectMatcher = selectPattern.matcher(html);
		while (selectMatcher.find())
		{
			trips.add(new Trip(selectMatcher.group(2), selectMatcher.group(1) == null));
		}			

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
	
	public String getRouteDescription()
	{
		return desc;
	}
	public String getLocation()
	{
		return location;
	}

	public String getStopNumber()
	{
		return stopnumber;
	}
	
	public String getDirection()
	{
		return direction;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(route);
		dest.writeString(desc);
		dest.writeString(direction);
		dest.writeString(location);
		dest.writeString(stopnumber);
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
