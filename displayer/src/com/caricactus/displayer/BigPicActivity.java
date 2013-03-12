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

public class BigPicActivity extends Activity
{
	String _selectionType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		
		//*/ TODO: place this in a task!
		// Display proper title and barbel, and select the right images in the Gallery
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		_selectionType = getIntent().getStringExtra("SELECTION_TYPE");
		String selectionArg = getIntent().getStringExtra("SELECTION_ARG");
		if(_selectionType.equals("TAG"))
		{
			barbel.setImageResource(R.drawable.tag_title_barbel);
			Gallery.Singleton().selectTag(selectionArg);
		}
		else if(_selectionType.equals("AUTHOR"))
		{
			barbel.setImageResource(R.drawable.author_title_barbel);
			Gallery.Singleton().selectAuthor(selectionArg);
		}
		else
		{
			barbel.setImageResource(R.drawable.all_title_barbel);
			Gallery.Singleton().selectAll();
		}
		
		// Resize top and bottom cactus
		adjustCactus();
		
		// Build the Scroller
		RelativeLayout scrollerBody = (RelativeLayout)findViewById(R.id.scrollerBody);
		Gallery.Singleton().buildScroller(scrollerBody, this, "BIG");
		//*/
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
		
	}

	public void smallPicButton(View view)
	{
		Intent intent = new Intent(this, SmallPicActivity.class);
		intent.putExtra("SELECTION_TYPE", _selectionType);
		startActivity(intent);
	}
	
	private void adjustCactus()
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		findViewById(R.id.topCactus).getLayoutParams().height = size.x * 10 / 19;
		findViewById(R.id.bottomCactus).getLayoutParams().height = size.x * 10 / 28;
	}
	
	public void spikeButton(View view)
	{
		Caricature c = Gallery.Singleton().getCaricature(Integer.valueOf(view.getTag().toString()));
		c.spike();
	}
	
	public void imageButton(View view)
	{
		
	}
}
