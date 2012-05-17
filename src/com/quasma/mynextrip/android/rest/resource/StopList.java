package com.quasma.mynextrip.android.rest.resource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.quasma.mynextrip.android.providers.StopProviderContract.StopTable;

import android.net.Uri;

public class StopList implements Resource
{
	public static final Uri CONTENT_URI = StopTable.CONTENT_URI;

	private List<Stop> stops;
	
	public StopList(URL url, String route, String direction, String html) 
	{
		stops = new ArrayList<Stop>();
		
		String selectExpr = "<a.*?_lnkStop.*?class=\"listlink\".*?href=\"(.+?)\">(.+?)</a>";
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
				
				stops.add(new Stop(
						route,
						direction,
						selectMatcher.group(1).substring(selectMatcher.group(1).lastIndexOf('=') + 1),
						null, // stop number
						selectMatcher.group(2), 
						baseUrl + new String(selectMatcher.group(1)).replace("&amp;", "&")));
			}
		}			
	}

	public List<Stop> getStops() 
	{
		return stops;
	}
}
