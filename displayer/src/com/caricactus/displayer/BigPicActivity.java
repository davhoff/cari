package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BigPicActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		RelativeLayout scrollerBody = (RelativeLayout)findViewById(R.id.scrollerBody);
		
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		barbel.setImageResource(R.drawable.all_title_barbel);
		
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.placeholder);
		image.setId(400);
		scrollerBody.addView(image, new RelativeLayout.LayoutParams(300, 300));
		//*/
		for(int i=0; i<15; i++)
		{
			image = new ImageView(this);
			image.setImageResource(R.drawable.placeholder);
			image.setId(401+i);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(300 - 8*i, 300 - 8*i);
			lp.addRule(RelativeLayout.BELOW, 400+i);
			scrollerBody.addView(image, lp);
		}
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
		
	}
	
	public void bigPicButton(View view)
	{
		
	}

	public void smallPicButton(View view)
	{
		Intent intent = new Intent(this, SmallPicActivity.class);
		startActivity(intent);
	}
}
