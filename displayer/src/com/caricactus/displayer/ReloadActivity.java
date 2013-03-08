package com.caricactus.displayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class ReloadActivity extends Activity
{
	int _waitingTime = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reload);
		
		
		// Organize the update of the progress bar
		ImageView progressBar = (ImageView)findViewById(R.id.progressBar);
		final Runnable r = new Runnable()
		{
		    public void run()
		    {
		        finishLoading();
		    }
		};
		progressBar.postDelayed(r, _waitingTime);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reload, menu);
		return true;
	}
	
	void finishLoading()
	{
		finish();
	}
}
