package com.caricactus.displayer;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class BigPicAdapter extends ArrayAdapter<Caricature>
{
	private Context _context;
	private Caricature[] _values;
	
	private int _screenWidth;
	private int _bigPicHeight;
	private int _middleCactusHeight;
	private int _topCactusHeight;
	
	public BigPicAdapter(Context context, Caricature[] values)
	{
		super(context, R.layout.bigpic, values);
		_context = context;
		_values = values;

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		_screenWidth = size.x;
		
		_bigPicHeight = _screenWidth * 10 / 16;
		_middleCactusHeight = _screenWidth * 10 / 85;
		_topCactusHeight = _screenWidth * 10 / 19;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View bigPic = convertView;
		if(position == 0)
		{
			bigPic = LayoutInflater.from(_context).inflate(R.layout.bigpic_first, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _bigPicHeight + _topCactusHeight);
			bigPic.setLayoutParams(layout);
			
			bigPic.findViewById(R.id.top_cactus).getLayoutParams().height = _topCactusHeight;
		}
		else if(bigPic == null || bigPic.getTag().toString().equals("0"))
		{
			bigPic = LayoutInflater.from(_context).inflate(R.layout.bigpic_middle, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _bigPicHeight + _middleCactusHeight);
			bigPic.setLayoutParams(layout);
			
			bigPic.findViewById(R.id.middle_cactus).getLayoutParams().height = _middleCactusHeight;
		}
		bigPic.setTag(position);

		// Caricature to display
		Caricature c = _values[position];
		
		// Image
		ImageButton image = (ImageButton)bigPic.findViewById(R.id.newImage);
		image.setImageBitmap(null);
		c.setImage(image);
		image.setTag(c._id);
		
		// Author
		TextView author = (TextView)bigPic.findViewById(R.id.newAuthor);
		author.setText(c._author);
		
		// Spike button
		ImageButton spikeButton = (ImageButton)bigPic.findViewById(R.id.newSpikeButton);
		spikeButton.setTag(c._id);
		if(c._didSpike)
		{
			spikeButton.setImageResource(R.drawable.spike_big_pic_disabled);
			spikeButton.setEnabled(false);
		}
		else
		{
			spikeButton.setImageResource(R.drawable.spike_big_pic);
			spikeButton.setEnabled(true);
		}
		
		// Spike count
		TextView spikeCount = (TextView)bigPic.findViewById(R.id.newSpikeCount);
		spikeCount.setText(String.valueOf(c._spikes));
		
		// Add the middle cactus
		
	    return bigPic;
	}	
}