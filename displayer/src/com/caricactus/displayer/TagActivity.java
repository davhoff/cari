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
			String tag = "REPLACE BY TAG NAME";
			switch(view.getId())
			{
				case R.id.tagTile11:
					tag = "INTERNATIONAL";
					break;
				case R.id.tagTile12:
					tag = "SOCIETE";
					break;
				case R.id.tagTile13:
					tag = "ECONOMIE";
					break;
				case R.id.tagTile21:
					tag = "SCIENCES";
					break;
				case R.id.tagTile23:
					tag = "CULTURE";
					break;
				case R.id.tagTile31:
					tag = "POLITIQUE";
					break;
				case R.id.tagTile32:
					tag = "SPORT";
					break;
			}
			Intent intent = new Intent(this, BigPicActivity.class);
			intent.putExtra("SELECTION_TYPE", "TAG");
			intent.putExtra("SELECTION_ARG", tag);
			startActivity(intent);
		}
	}
}
