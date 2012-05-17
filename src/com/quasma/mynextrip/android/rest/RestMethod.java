package com.quasma.mynextrip.android.rest;

import com.quasma.mynextrip.android.rest.resource.Resource;

public interface RestMethod<T extends Resource>
{
	public RestMethodResult<T> execute();
}
