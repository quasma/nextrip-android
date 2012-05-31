package com.quasma.android.bustrip.providers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class NexTripItem implements Parcelable, Serializable
{
	static final long serialVersionUID =-7191211590843971627L;
	
	private NexTripItem parent = null;
	private String parentKey;
	private String name;
	private String key;
	private Type type;
	
	public enum Type { ROUTE, DIRECTION, STOP};
	
	private NexTripItem() {};
	
	public NexTripItem(Type type, String key, String name)
	{
		this(type, null, key, name);
	}
	public NexTripItem(Type type, NexTripItem parent, String key, String name)
	{
		this.type 			= type;
		this.parent			= parent;
		this.parentKey 		= parent == null ? "" : parent.getKey();
		this.key 			= key;
		this.name 			= name;
	}
	public NexTripItem getParent()
	{
		return parent;
	}
	
	public String toString()
	{
		return name;
	}
	public String getKey()
	{
		String key;
		if (parentKey != null
		&&  parentKey.length() > 0)
			key = parentKey + "&";
		else
			key = "";

		switch (type)
		{
			case ROUTE:
				key += "route=";
				break;
			case DIRECTION:
				key += "direction=";
				break;
			case STOP:
				key += "stop=";
				break;
		}
		return key + this.key;
	}
		
	public Type getType()
	{
		return type;
	}
	
	public String getId()
	{
		return this.key;
	}

	private void writeObject(ObjectOutputStream os) throws IOException
	{
		
	}
	
	@Override
	public int describeContents() 
	{
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(name);
		dest.writeString(key);
		dest.writeString(type.name());
		dest.writeString(parentKey);
		dest.writeParcelable(parent, flags);
	}
	public static final Parcelable.Creator<NexTripItem> 
			CREATOR = new Parcelable.Creator<NexTripItem>() 
			{
				public NexTripItem createFromParcel(Parcel in) 
				{
					NexTripItem item = new NexTripItem();
					item.name = in.readString();
					item.key		 = in.readString();
					item.type		 = Type.valueOf(in.readString());
					item.parentKey	 = in.readString();
					item.parent		 = in.readParcelable(NexTripItem.class.getClassLoader());
					return item;
				}

				public NexTripItem[] newArray(int size) 
				{
					return new NexTripItem[size];
				}
			};

}
