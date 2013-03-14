package com.caricactus.displayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FullscreenActivity extends Activity
{
	int _id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		
		_id = getIntent().getIntExtra("IMAGE_ID",0);
		ImageButton image = (ImageButton)findViewById(R.id.image);
		Gallery.Singleton().getCaricature(_id).setImage(image);
	}
	
	public void backButton(View view)
	{
		finish();
	}
}
