package com.quasma.android.bustrip.service;

import android.os.Bundle;

public interface ProcessorCallback
{
	void send(int resultCode, Bundle data);
}
