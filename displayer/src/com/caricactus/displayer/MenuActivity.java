package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MenuActivity extends Activity
{
	boolean isFirstDisplay = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		if(isFirstDisplay)
		{
			isFirstDisplay = false;
			Intent intent = new Intent(this, ReloadActivity.class);
			startActivity(intent);
		}
	}
	
	// User taps on a button
	public void onClick(View view)
	{
		if (view.getId() == R.id.allImagesButton)
		{
			Intent intent = new Intent(this, BigPicActivity.class);
			intent.putExtra("SELECTION_TYPE", "ALL");
			intent.putExtra("SELECTION_ARG", "");
			startActivity(intent);
		}
		
		if (view.getId() == R.id.tagsButton)
		{
			Intent intent = new Intent(this, TagActivity.class);
			startActivity(intent);
		}
	}
}