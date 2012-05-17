package com.quasma.mynextrip.android.rest;

import java.util.List;
import java.util.Map;

import com.quasma.mynextrip.android.providers.RouteProviderContract;
import com.quasma.mynextrip.android.providers.RouteProviderContract.RouteTable;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;

public class RestMethodFactory
{
	private static RestMethodFactory instance;
	private static Object lock = new Object();
	private UriMatcher uriMatcher;
	private Context context;

	private static final int ROUTE = 1;

	public static enum Method { GET, POST, PUT, DELETE }

	private RestMethodFactory(Context context) 
	{
		context = context.getApplicationContext();
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(RouteProviderContract.AUTHORITY, RouteTable.TABLE_NAME, ROUTE);
	}

	public static RestMethodFactory getInstance(Context context) 
	{
		synchronized (lock) 
		{
			if (instance == null) 
				instance = new RestMethodFactory(context);
		}
		return instance;
	}

	public RestMethod getRestMethod(Uri resourceUri, Method method,
			Map<String, List<String>> headers, byte[] body) 
	{
		switch (uriMatcher.match(resourceUri)) 
		{
			case ROUTE:
				return new RouteRestMethod(context);
		}
		return null;
	}
}
