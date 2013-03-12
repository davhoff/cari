package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SmallPicActivity extends Activity
{
	String _selectionType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		
		// Display proper title and barbel
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		_selectionType = getIntent().getStringExtra("SELECTION_TYPE");
		if(_selectionType.equals("TAG"))
			barbel.setImageResource(R.drawable.tag_title_barbel);
		else if(_selectionType.equals("AUTHOR"))
			barbel.setImageResource(R.drawable.author_title_barbel);
		else
			barbel.setImageResource(R.drawable.all_title_barbel);
		
		// Resize top and bottom cactus
		adjustCactus();
		
		// Build the Scroller
		RelativeLayout scrollerBody = (RelativeLayout)findViewById(R.id.scrollerBody);
		Gallery.Singleton().buildScroller(scrollerBody, this, "SMALL");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_big_pic, menu);
		return true;
	}
	
	public void backButton(View view)
	{
		finish();
	}
	
	public void refreshButton(View view)
	{
		Intent intent = new Intent(this, ReloadActivity.class);
		startActivity(intent);
	}
	
	public void bigPicButton(View view)
	{
		finish();
	}

	public void smallPicButton(View view)
	{
		
	}
	
	private void adjustCactus()
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		findViewById(R.id.topCactus).getLayoutParams().height = size.x * 10 / 19;
		findViewById(R.id.bottomCactus).getLayoutParams().height = size.x * 10 / 28;
	}
	
	public void imageButton(View view)
	{
		
	}
}
