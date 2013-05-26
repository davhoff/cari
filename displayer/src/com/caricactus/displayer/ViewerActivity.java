package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class ViewerActivity extends Activity implements ISpikeDisplayer
{
	String _selectionType;
	String _selectionArg;
	
	enum ViewerState
	{
		BigPic,
		SmallPic
	}
	ViewerState _state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewer);
		
		// Screen width
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		
		// Display proper title and barbel, and select the right images in the Gallery
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		_selectionType = getIntent().getStringExtra("SELECTION_TYPE");
		_selectionArg = getIntent().getStringExtra("SELECTION_ARG");
		
		if(_selectionType.equals("TAG"))
		{
			barbel.setImageResource(R.drawable.tag_title_barbel);
			Gallery.Singleton().selectTag(_selectionArg);
		}
		else if(_selectionType.equals("AUTHOR"))
		{
			barbel.setImageResource(R.drawable.author_title_barbel);
			Gallery.Singleton().selectAuthor(_selectionArg);
		}
		else
		{
			barbel.setImageResource(R.drawable.all_title_barbel);
			Gallery.Singleton().selectAll();
		}
		
		// Barbel layout
		LayoutParams barbelLayout = new LayoutParams(screenWidth, screenWidth / 5);
		barbelLayout.addRule(RelativeLayout.BELOW, R.id.included);
		barbel.setLayoutParams(barbelLayout);
		
		// Set up the Scroller
		initScroller();
	}
	
	private void initScroller()
	{
		ListView scroller = (ListView)findViewById(R.id.scrollerBody);
		scroller.setDivider(null);
		
		// Get screen width
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		// Create Footer
		ImageView footer = new ImageView(this);
		footer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, size.x * 10 / 28));
		footer.setImageResource(R.drawable.cactus_bas);
		scroller.addFooterView(footer, null, false);
		
		// Set the adapter for this view
		bigPicButton(null);
	}
	
	public void refreshSpikes()
	{
		ListView scroller = (ListView)findViewById(R.id.scrollerBody);
		for(int i=0; i< scroller.getChildCount(); i++)
		{
			// View and associated Caricature
			View tile = scroller.getChildAt(i);
			Caricature c = Gallery.Singleton().getCaricature(Integer.parseInt(tile.findViewById(R.id.newImage).getTag().toString()));

			// Spike count
			TextView spikeCount = (TextView)tile.findViewById(R.id.newSpikeCount);
			spikeCount.setText(String.valueOf(c._spikes));
		}
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
		if(_state != ViewerState.BigPic)
		{
			_state = ViewerState.BigPic;
			ListView scroller = (ListView)findViewById(R.id.scrollerBody);
			scroller.setAdapter(new BigPicAdapter(this, Gallery.Singleton().getSelectedList()));
		}
	}

	public void smallPicButton(View view)
	{
		if(_state != ViewerState.SmallPic)
		{
			_state = ViewerState.SmallPic;
			ListView scroller = (ListView)findViewById(R.id.scrollerBody);
			scroller.setAdapter(new SmallPicAdapter(this, Gallery.Singleton().getSelectedList()));
		}
	}
	
	public void spikeButton(View view)
	{
		Caricature c = Gallery.Singleton().getCaricature(Integer.valueOf(view.getTag().toString()));
		c.spike((ImageButton)view, this);
	}
	
	public void imageButton(View view)
	{
		Intent intent = new Intent(this, FullscreenActivity.class);
		intent.putExtra("IMAGE_ID", (Integer)view.getTag());
		startActivity(intent);
	}
}
