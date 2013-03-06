package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class TagActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		barbel.setImageResource(R.drawable.tag_title_barbel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tag, menu);
		return true;
	}
	
	public void backButton(View view)
	{
		finish();
	}
	
	// User clicks on a button to choose a tag
	public void tagClick(View view)
	{
		if(view.getId() == R.id.tagTile33)
		{
			// Choose a random pic and go fullscreen
		}
		else
		{
			switch(view.getId())
			{
				case R.id.tagTile11:
					break;
				case R.id.tagTile12:
					break;
				case R.id.tagTile13:
					break;
				case R.id.tagTile21:
					break;
				case R.id.tagTile23:
					break;
				case R.id.tagTile31:
					break;
				case R.id.tagTile32:
					break;
			}
			Intent intent = new Intent(this, BigPicActivity.class);
			intent.putExtra("SELECTION_TYPE", "TAG");
			startActivity(intent);
		}
	}
}
