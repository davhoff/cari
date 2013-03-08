package com.caricactus.displayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class BigPicActivity extends Activity
{
	String _selectionType;
	int _lastIdInScroller;
	ImageDbHelper _dbHelper = new ImageDbHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		
		/*/
		ImageView cactusTop = (ImageView)findViewById(R.id.cactusTop);
		LayoutParams layout = cactusTop.getLayoutParams();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		float ratio = (float)cactusTop.getDrawable().getBounds().width() / display.getWidth();
		//layout.width *= ratio;
		int height = (int) (((float)(layout.height)) * ratio);
		final Rect rect = cactusTop.getDrawable().getBounds();
		Log.v("bounds width", String.valueOf(rect.width()));
		Log.v("display width", String.valueOf(display.getWidth()));
		Log.v("ratio", String.valueOf(ratio));
		Log.v("height", String.valueOf(height));
		//layout.height = height;
		//*/
		
		// Display proper title and barbel
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		_selectionType = getIntent().getStringExtra("SELECTION_TYPE");
		if(_selectionType.equals("TAG"))
			barbel.setImageResource(R.drawable.tag_title_barbel);
		else if(_selectionType.equals("AUTHOR"))
			barbel.setImageResource(R.drawable.author_title_barbel);
		else
			barbel.setImageResource(R.drawable.all_title_barbel);

		buildScroller();
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
		Log.v("msg", _selectionType);
		intent.putExtra("SELECTION_TYPE", _selectionType);
		startActivity(intent);
	}
	
	void buildScroller()
	{
		long[] caricatures = _dbHelper.getImages();
		
		_lastIdInScroller = 42;
		addBigPic();
		
		for(int i=1; i<caricatures.length; i++)
		{
			addMiddleCactus();
			
			addBigPic();
		}
	}
	
	void addBigPic()
	{
		// Layout for the new cactus piece
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.BELOW, _lastIdInScroller);

		// Build the container
		ImageView bigPic = new ImageView(this);
		bigPic.setImageResource(R.drawable.big_pic);
		bigPic.setScaleType(ScaleType.FIT_CENTER);
		bigPic.setLayoutParams(layout);
		_lastIdInScroller++;
		bigPic.setId(_lastIdInScroller);
		
		// Add it to the scroller
		RelativeLayout scrollerBody = (RelativeLayout)findViewById(R.id.scrollerBody);
		scrollerBody.addView(bigPic);
	}
	
	void addMiddleCactus()
	{
		// Layout for the new cactus piece
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.BELOW, _lastIdInScroller);

		// Build the cactus ImageView
		ImageView cactus = new ImageView(this);
		cactus.setImageResource(R.drawable.cactus_tronc);
		_lastIdInScroller++;
		cactus.setId(_lastIdInScroller);
		
		// Add it to the scroller
		RelativeLayout scrollerBody = (RelativeLayout)findViewById(R.id.scrollerBody);
		scrollerBody.addView(cactus, layout);
	}
}
