package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AuthorActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author);
		
		// Screen width
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		
		// Barbel layout
		ImageView barbel = (ImageView)findViewById(R.id.barbel);
		barbel.setImageResource(R.drawable.author_title_barbel);
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
		
		// Set the AuthorAdapter for this view
		scroller.setAdapter(new AuthorAdapter(this, Gallery.Singleton().getAuthorList()));
	}
	
	public void backButton(View view)
	{
		finish();
	}
	
	public void imageButton(View view)
	{
		Intent intent = new Intent(this, ViewerActivity.class);
		intent.putExtra("SELECTION_TYPE", "AUTHOR");
		intent.putExtra("SELECTION_ARG", view.getTag().toString());
		startActivity(intent);
	}
}
