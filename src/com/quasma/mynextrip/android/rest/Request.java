package com.quasma.mynextrip.android.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
	
	private URI requestUri;
	private Map<String, List<String>> headers;
	private byte[] body;
	
	public Request(URI requestUri) 
	{
		super();
		this.requestUri = requestUri;
	}
	
	public URI getRequestUri() 
	{
		return requestUri;
	}

	public Map<String, List<String>> getHeaders() 
	{
		return headers;
	}

	public byte[] getBody() 
	{
		return body;
	}

	public void addHeader(String key, List<String> value) 
	{
		
		if (headers == null) 
		{
			headers = new HashMap<String, List<String>>();
		}
		headers.put(key, value);
	}
}
