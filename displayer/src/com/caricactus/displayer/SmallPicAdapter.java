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

public class SmallPicAdapter extends ArrayAdapter<Caricature>
{
	private Context _context;
	private Caricature[] _values;
	
	private int _screenWidth;
	private int _smallPicHeight;
	private int _topCactusHeight;
	private int _topOverlap;
	private int _middleOverlap;
	private int _middleCactusHeight;
	
	public SmallPicAdapter(Context context, Caricature[] values)
	{
		super(context, R.layout.smallpic, values);
		_context = context;
		_values = values;

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		_screenWidth = size.x;

		_smallPicHeight = _screenWidth * 10 / 35;
		_topCactusHeight = _screenWidth * 10 / 19;
		_topOverlap = _topCactusHeight / 5;
		_middleCactusHeight = _screenWidth * 10 / 85;
		_middleOverlap = _middleCactusHeight * 2 / 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View smallPic = convertView;
		if(position == 0)
		{
			smallPic = LayoutInflater.from(_context).inflate(R.layout.smallpic_first, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _smallPicHeight + _topCactusHeight - _topOverlap);
			smallPic.setLayoutParams(layout);
			
			smallPic.findViewById(R.id.top_cactus).getLayoutParams().height = _topCactusHeight;
			smallPic.findViewById(R.id.smallPic).getLayoutParams().height = _smallPicHeight;			
		}
		else if(smallPic == null || smallPic.getTag().toString().equals("0"))
		{
			smallPic = LayoutInflater.from(_context).inflate(R.layout.smallpic_middle, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _smallPicHeight + _middleCactusHeight - _middleOverlap);
			smallPic.setLayoutParams(layout);
			
			smallPic.findViewById(R.id.middle_cactus).getLayoutParams().height = _middleCactusHeight;
			smallPic.findViewById(R.id.smallPic).getLayoutParams().height = _smallPicHeight;			
		}
		smallPic.setTag(position);
		
		// Caricature to display
		Caricature c = _values[position];
		
		// Image
		ImageButton image = (ImageButton)smallPic.findViewById(R.id.newImage);
		image.setImageBitmap(null);
		c.setImage(image);
		image.setTag(c._id);
		
		// Author
		TextView author = (TextView)smallPic.findViewById(R.id.newAuthor);
		author.setText(c._author);

		// Title
		TextView title = (TextView)smallPic.findViewById(R.id.newTitle);
		title.setText(c._title);
		
		// Spike count
		TextView spikeCount = (TextView)smallPic.findViewById(R.id.newSpikeCount);
		spikeCount.setText(String.valueOf(c._spikes));
		
		return smallPic;
	}	
}