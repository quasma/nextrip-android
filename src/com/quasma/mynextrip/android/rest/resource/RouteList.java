package com.quasma.mynextrip.android.rest.resource;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.quasma.mynextrip.android.providers.RouteProviderContract.RouteTable;

import android.net.Uri;

public class RouteList implements Resource
{
	public static final Uri CONTENT_URI = RouteTable.CONTENT_URI;

	private List<Route> routes;
	
	public RouteList(URL url, String html) 
	{
		routes = new ArrayList<Route>();
		
		String selectExpr = "<a.*?class=\"listlink\".*?href=\"(.+?)\">(.+?)</a>";
		Pattern selectPattern = Pattern.compile(selectExpr, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
		Matcher selectMatcher = selectPattern.matcher(html);
		while (selectMatcher.find())
		{
			if (selectMatcher.group(1).contains("/NexTripText.aspx"))
			{
				String baseUrl;
				if (url.getPort() > 0)
					baseUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
				else
					baseUrl = url.getProtocol() + "://" + url.getHost();
				
				routes.add(new Route(
						selectMatcher.group(1).substring(selectMatcher.group(1).lastIndexOf('=') + 1), 
						selectMatcher.group(2), 
						URLDecoder.decode(baseUrl + selectMatcher.group(1))));
			}
		}			
	}

	public List<Route> getRoutes() 
	{
		return routes;
	}
}
