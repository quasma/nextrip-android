package com.quasma.mynextrip.android.rest.resource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.quasma.mynextrip.android.providers.DirectionProviderContract.DirectionTable;

import android.net.Uri;

public class DirectionList implements Resource
{
	public static final Uri CONTENT_URI = DirectionTable.CONTENT_URI;

	private List<Direction> directions;
	
	public DirectionList(URL url, String route, String html) 
	{
		directions = new ArrayList<Direction>();
		
		String selectExpr = "<a.*?_lnkDirection.*?class=\"listlink\".*?href=\"(.+?)\">(.+?)</a>";
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
				
				directions.add(new Direction(
						route,
						selectMatcher.group(1).substring(selectMatcher.group(1).lastIndexOf('=') + 1), 
						selectMatcher.group(2), 
						baseUrl + new String(selectMatcher.group(1)).replace("&amp;", "&")));
			}
		}			
	}

	public List<Direction> getDirections() 
	{
		return directions;
	}

}
