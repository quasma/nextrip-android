package com.quasma.android.bustrip.rest.resource;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable
{
	private boolean actual;
	private String route;
	private String terminal;
	private String direction;
	private String description;
	private String departure;
	private String departureTime;
	
	public Trip(Parcel parcel)
	{
		actual = parcel.readInt() == 1;
		route = parcel.readString();
		direction = parcel.readString();
		description = parcel.readString();
		departure = parcel.readString();
		departureTime = parcel.readString();
		terminal = parcel.readString();
	}
	
	public Trip(String route, String direction, String description, boolean actual, String departure, String departureTime, String terminal)
	{
		this.route = route;
		this.direction = direction;
		this.description = description;
		this.departure = departure;
		this.departureTime = departureTime;
		this.actual = actual;
		this.terminal = terminal;
	}
	
	public String getTerminal()
	{
		return terminal;
	}
	
	public String getRoute()
	{
		return route;
	}
	public String getDescription()
	{
		return description;
	}
	public String getDeparture()
	{
		return departure;
	}

	public String toString()
	{
		return departure;
	}
	public boolean isActual()
	{
		return actual;
	}
	
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(actual ? 1 : 0);
		dest.writeString(route);
		dest.writeString(direction);
		dest.writeString(description);
		dest.writeString(departure);
		dest.writeString(departureTime);
		dest.writeString(terminal);
	}
	
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() 
	{
	    public Trip createFromParcel(Parcel in) 
	    {
	        return new Trip(in);
	    }

	    public Trip[] newArray(int size) 
	    {
	        return new Trip[size];
	    }
	};
}
