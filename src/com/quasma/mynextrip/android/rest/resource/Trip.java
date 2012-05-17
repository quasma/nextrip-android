package com.quasma.mynextrip.android.rest.resource;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable
{
	private String time;
	private boolean enroute;
	
	public Trip(Parcel parcel)
	{
		time = parcel.readString();
		enroute = parcel.readInt() == 1;
	}
	
	public Trip(String time, boolean enroute)
	{
		this.time = time;
		this.enroute = enroute;
	}
	
	public String getTime()
	{
		return time;
	}

	public String toString()
	{
		return time;
	}
	public boolean isEnroute()
	{
		return enroute;
	}
	
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(time);
		dest.writeInt(enroute ? 1 : 0);
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
