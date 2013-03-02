package com.caricactus.displayer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SmallPicActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		RelativeLayout scrollerBody = (RelativeLayout)findViewById(R.id.scrollerBody);
		
		// Set appropriate barbel
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		barbel.setImageResource(R.drawable.all_title_barbel);
		
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.placeholder);
		image.setId(400);
		scrollerBody.addView(image, new RelativeLayout.LayoutParams(300, 300));
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
		
	}
	
	public void bigPicButton(View view)
	{
		finish();
	}

	public void smallPicButton(View view)
	{
		
	}
}
