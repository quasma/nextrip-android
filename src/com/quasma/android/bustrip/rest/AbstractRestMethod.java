package com.quasma.android.bustrip.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.quasma.android.bustrip.rest.resource.Resource;

import android.content.Context;

public abstract class AbstractRestMethod<T extends Resource> implements RestMethod<T> 
{
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String HTTP = "http";
	private static final String SERVER = "svc.metrotransit.org";
	private static final String FORMAT = "format=json";
	
	public RestMethodResult<T> execute() 
	{
		Request request = buildRequest();
		Response response = doRequest(request);
		return buildResult(response);
	}
	
	public URI buildURI(String path)
	{
		try
		{
			return new URI(HTTP, SERVER, path, FORMAT, null);
		}
		catch (URISyntaxException e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	protected abstract Context getContext();

	protected RestMethodResult<T> buildResult(Response response) 
	{
		String statusMsg = null;
		String responseBody = null;
		T resource = null;
		int status = response.status;
		
		try 
		{
			if (status < 400)
			{
				responseBody = new String(response.body, getCharacterEncoding(response.headers));
				resource = parseResponseBody(responseBody);
			}
			else
			{
				statusMsg = response.statusMsg;
			}
		} 
		catch (Exception ex) 
		{
			status = 600; // spec only defines up to 505
			ex.printStackTrace();
			statusMsg = ex.getMessage();
		}
		return new RestMethodResult<T>(status, statusMsg, resource);
	}

	protected abstract Request buildRequest();
	
	protected abstract T parseResponseBody(String responseBody) throws Exception;

	private Response doRequest(Request request) 
	{
		RestClient client = new RestClient(getContext());
		return client.execute(request);
	}

	private String getCharacterEncoding(Map<String, List<String>> headers) 
	{
		// TODO get value from headers
		return DEFAULT_ENCODING;
	}

}
