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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BigPicAdapter extends ArrayAdapter<Caricature>
{
	private Context _context;
	private Caricature[] _values;
	
	private int _screenWidth;
	private int _bigPicHeight;
	private int _middleCactusHeight;
	private int _topCactusHeight;
	
	private int _imageY;
	private int _imageH;
	private int _spikeX;
	private int _spikeY;
	private int _spikeW;
	private int _spikeH;
	private int _countX;
	private int _countW;
	private int _authorX;
	private int _authorW;
	
	static class ViewHolder
	{
		int position;
		ImageButton image;
		TextView author;
		ImageButton spikeButton;
		TextView spikeCount;
	}
	
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
		
		_imageY = _bigPicHeight * 52 / 400;
		_imageH = _bigPicHeight * 249 / 400;
		_spikeX = _screenWidth * 6 / 64;
		_spikeY = _bigPicHeight * 328 / 400;
		_spikeW = _screenWidth * 15 / 64;
		_spikeH = _bigPicHeight * 66 / 400;
		_countX = _screenWidth * 22 / 64;
		_countW = _screenWidth * 10 / 64;
		_authorX = _screenWidth * 35 / 64;
		_authorW = _screenWidth * 22 / 64;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Boolean isRecycling = false;
		View bigPic = convertView;
		if(position == 0)
		{
			bigPic = LayoutInflater.from(_context).inflate(R.layout.bigpic_first, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _bigPicHeight + _topCactusHeight);
			bigPic.setLayoutParams(layout);
			
			bigPic.findViewById(R.id.cactus).getLayoutParams().height = _topCactusHeight;
		}
		else if(bigPic == null || ((ViewHolder)bigPic.getTag()).position == 0)
		{
			bigPic = LayoutInflater.from(_context).inflate(R.layout.bigpic_middle, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _bigPicHeight + _middleCactusHeight);
			bigPic.setLayoutParams(layout);
			
			bigPic.findViewById(R.id.cactus).getLayoutParams().height = _middleCactusHeight;
		}
		else
		{
			isRecycling = true;
		}
		
		// ViewHolder pattern
		ViewHolder holder;
		if(isRecycling)
		{
			holder = (ViewHolder)bigPic.getTag();
		}
		else
		{
			holder = new ViewHolder();
			holder.image = (ImageButton)bigPic.findViewById(R.id.newImage);
			holder.author = (TextView)bigPic.findViewById(R.id.newAuthor);
			holder.spikeButton = (ImageButton)bigPic.findViewById(R.id.newSpikeButton);
			holder.spikeCount = (TextView)bigPic.findViewById(R.id.newSpikeCount);
			bigPic.setTag(holder);
		}
		holder.position = position;
		
		// Caricature to display
		Caricature c = _values[position];
		
		// Image
		holder.image.setImageBitmap(null);
		c.setImage(holder.image);
		holder.image.setTag(c._id);
		
		// Author
		holder.author.setText(c._author);
		
		// Spike button
		holder.spikeButton.setTag(c._id);
		if(c._didSpike)
		{
			holder.spikeButton.setImageResource(R.drawable.spike_big_pic_disabled);
			holder.spikeButton.setEnabled(false);
		}
		else
		{
			holder.spikeButton.setImageResource(R.drawable.spike_big_pic);
			holder.spikeButton.setEnabled(true);
		}
		
		// Spike count
		holder.spikeCount.setText(String.valueOf(c._spikes));
		
		// Layout
		if(!isRecycling)
		{
			RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, _imageH);
			imageParams.topMargin = _imageY;
			holder.image.setLayoutParams(imageParams);
			
			RelativeLayout.LayoutParams spikeParams = new RelativeLayout.LayoutParams(_spikeW, _spikeH);
			spikeParams.leftMargin = _spikeX;
			spikeParams.topMargin = _spikeY;
			holder.spikeButton.setLayoutParams(spikeParams);
			
			RelativeLayout.LayoutParams countParams = new RelativeLayout.LayoutParams(_countW, _spikeH);
			countParams.leftMargin = _countX;
			countParams.topMargin = _spikeY;
			holder.spikeCount.setLayoutParams(countParams);

			RelativeLayout.LayoutParams authorParams = new RelativeLayout.LayoutParams(_authorW, _spikeH);
			authorParams.leftMargin = _authorX;
			authorParams.topMargin = _spikeY;
			holder.author.setLayoutParams(authorParams);
		}
		
	    return bigPic;
	}	
}