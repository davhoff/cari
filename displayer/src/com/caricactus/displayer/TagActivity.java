package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class TagActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		
		// Screen width
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		
		// Barbel layout
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		barbel.setImageResource(R.drawable.tag_title_barbel);
		barbel.getLayoutParams().height = screenWidth/5;

		
		// Tile canevas layout
		LinearLayout canevas = (LinearLayout)findViewById(R.id.tile_canevas);
		int margin = 50;
		LinearLayout.LayoutParams canevasLayout = new LinearLayout.LayoutParams(screenWidth-2*margin, screenWidth-2*margin);
		canevasLayout.setMargins(margin, margin, margin, margin);
		canevas.setLayoutParams(canevasLayout);
		canevas.requestLayout();
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
			Intent intent = new Intent(this, FullscreenActivity.class);
			intent.putExtra("IMAGE_ID", Gallery.Singleton().getRandomId());
			startActivity(intent);
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
			Intent intent = new Intent(this, ViewerActivity.class);
			intent.putExtra("SELECTION_TYPE", "TAG");
			intent.putExtra("SELECTION_ARG", tag);
			startActivity(intent);
		}
	}
}
