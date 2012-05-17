package com.quasma.mynextrip.android.rest;

import java.net.URL;
import java.util.List;
import java.util.Map;

import com.quasma.mynextrip.android.rest.resource.Resource;

import android.content.Context;

public abstract class AbstractRestMethod<T extends Resource> implements RestMethod<T> 
{
	private static final String DEFAULT_ENCODING = "UTF-8";

	public RestMethodResult<T> execute() 
	{
		Request request = buildRequest();
		Response response = doRequest(request);
		return buildResult(response);
	}
	
	protected abstract Context getContext();

	protected RestMethodResult<T> buildResult(Response response) 
	{
		String statusMsg = response.statusMsg;
		String responseBody = null;
		T resource = null;
		int status = response.status;
		
		try 
		{
			if (status < 400)
			{
				responseBody = new String(response.body, getCharacterEncoding(response.headers));
				resource = parseResponseBody(response.url, responseBody);
			}
		} 
		catch (Exception ex) 
		{
			status = 600; // spec only defines up to 505
			statusMsg = ex.getMessage();
		}
		return new RestMethodResult<T>(status, statusMsg, resource);
	}

	protected abstract Request buildRequest();
	
	protected abstract T parseResponseBody(URL base, String responseBody) throws Exception;

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
