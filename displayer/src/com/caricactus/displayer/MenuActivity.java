package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		
		//ImageDbHelper dbHelper = new ImageDbHelper(this);
		//dbHelper.addImageList("20,20_79058678o,jpg,Bar,SOCIETE-ECONOMIE,2012-09-15 15:09:17,Encore du tabac;");
	}
	
	// User taps on a button
	public void onClick(View view)
	{
		if (view.getId() == R.id.allImagesButton)
		{
			Intent intent = new Intent(this, BigPicActivity.class);
			intent.putExtra("SELECTION_TYPE", "ALL");
			startActivity(intent);
		}
		
		if (view.getId() == R.id.tagsButton)
		{
			Intent intent = new Intent(this, TagActivity.class);
			startActivity(intent);
		}
	}
}