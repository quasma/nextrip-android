package com.quasma.mynextrip.android.activity;

import com.quasma.mynextrip.android.service.NexTripService;
import com.quasma.mynextrip.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class StopNumberActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);

		setContentView(R.layout.stopnumber);

		Intent intent = getIntent();
		String title = intent.getExtras().getString(NexTripService.TITLE);
		if (title != null)
			setTitle(title);
		
		Button button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) 
		    {
		        Intent intent = new Intent();
		        intent.setClass(StopNumberActivity.this, (Class) MyNexTripActivity.class);
		        startActivity(intent);
		    }
		  });
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
	}
}
