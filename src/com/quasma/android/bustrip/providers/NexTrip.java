package com.quasma.android.bustrip.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class NexTrip implements Parcelable, Serializable
{
	static final long serialVersionUID = 5953977305504658034L;
	private List<Favorite> favorites = new ArrayList<Favorite>();
	
	private Context context;
	private File cache;
	
	public NexTrip(Context context, File cache)
	{
		this.context = context;
		this.cache   = cache;
		loadFromCache();
	}
	
	private NexTrip() {}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	public class Favorite implements Serializable
	{
		static final long serialVersionUID =-6396135591006774900L;
		
		String key;
		
		String routeNumber;
		String routeDirection;
		String stop;
		
		
		private Favorite() {}
		
		Favorite(NexTripItem stop)
		{
			this.key			= stop.getKey();
			this.stop			= stop.toString();
			this.routeDirection	= stop.getParent().toString();
			this.routeNumber	= stop.getParent().getParent().getId();
		}
		
		public String getRoute()
		{
			return getValue("route", this.key);
		}

		public String getDirection()
		{
			return getValue("direction", this.key);
		}
		public String getStop()
		{
			return getValue("stop", this.key);
		}
	}

	@Override
	public int describeContents() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	private static NexTrip getInstance(int version, ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		NexTrip nexTrip = new NexTrip();
		if (version == 4)
		{
			int number = ois.readInt();
			for (int i = 0; i < number; i++)
			{
				Favorite favorite 		= nexTrip.new Favorite();
				String description 		= (String) ois.readObject();
				favorite.stop 			= description.substring(0, description.indexOf('(')).trim();
				favorite.routeNumber 	= description.substring(description.indexOf('(') + 1, description.indexOf('/')).trim();
				favorite.routeNumber 	= description.substring(description.indexOf('/') + 1, description.indexOf(')')).trim();
				favorite.key		 	= (String) ois.readObject();
				nexTrip.favorites.add(favorite);
			}
		}
		else
		if (version == 5)
		{
			int nFavorites = ois.readInt();
			for (int i = 0; i < nFavorites; i++)
			{
				Favorite favorite 		= nexTrip.new Favorite();
				favorite.stop		 	= (String) ois.readObject();
				favorite.routeNumber 	= (String) ois.readObject();
				favorite.routeDirection	= (String) ois.readObject();
				favorite.key		 	= (String) ois.readObject();
				nexTrip.favorites.add(favorite);
			}
		}
		return nexTrip;
	}
	
	private String getValue(String key, String str)
	{
		String[] values = str.split("&");
		for (int i = 0; i < values.length; i++)
		{
			if (values[i].startsWith(key + "="))
				return values[i].substring(values[i].indexOf('=') + 1);
		}
		return null;
	}

	private void loadFromCache()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try 
		{
			if (cache.exists())
				fis = new FileInputStream(cache);
			else
				fis = new FileInputStream(new File(context.getCacheDir(), "cache"));
			
			ois = new ObjectInputStream(fis);
			int version = ois.readInt();
			switch (version)
			{
				case 1:
				case 2:
					break;
					
				case 3:
				{
					String lastTrip = (String) ois.readObject();
					NexTrip nexTrip = (NexTrip) ois.readObject();
					this.favorites = nexTrip.favorites;
					break;
				}	
				case 4:
				case 5:
				{
					NexTrip nexTrip = NexTrip.getInstance(version, ois);
					this.favorites = nexTrip.favorites;
					break;
				}	
				default:
				{
					int nFavorites = ois.readInt();
					for (int i = 0; i < nFavorites; i++)
					{
						Favorite favorite = new Favorite();
						favorite.stop	= (String) ois.readObject();
						favorite.routeNumber 	= (String) ois.readObject();
						favorite.routeDirection	= (String) ois.readObject();
						favorite.key		 	= (String) ois.readObject();
						favorites.add(favorite);
					}
					break;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (ois != null) ois.close();
				if (fis != null) fis.close();
			}
			catch (IOException ioe) {}
		}
	}
	
	public List<Favorite> getFavorites()
	{
		return favorites;
	}
}
