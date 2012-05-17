package com.quasma.mynextrip.android.service;

import android.os.Bundle;

public interface ProcessorCallback
{
	void send(int resultCode, Bundle data);
}
