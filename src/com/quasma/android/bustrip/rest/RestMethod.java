package com.quasma.android.bustrip.rest;

import com.quasma.android.bustrip.rest.resource.Resource;

public interface RestMethod<T extends Resource>
{
	public RestMethodResult<T> execute();
}
