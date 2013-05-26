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

public class AuthorAdapter extends ArrayAdapter<Author>
{
	private Context _context;
	private Author[] _values;
	
	private int _screenWidth;
	private int _authorHeight;
	private int _topCactusHeight;
	private int _topOverlap;
	private int _middleOverlap;
	private int _middleCactusHeight;
	
	public AuthorAdapter(Context context, Author[] values)
	{
		super(context, R.layout.author, values);
		_context = context;
		_values = values;

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		_screenWidth = size.x;

		_authorHeight = _screenWidth * 10 / 35;
		_topCactusHeight = _screenWidth * 10 / 19;
		_topOverlap = _topCactusHeight / 5;
		_middleCactusHeight = _screenWidth * 10 / 85;
		_middleOverlap = _middleCactusHeight * 2 / 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View authorTile = convertView;
		if(position == 0)
		{
			authorTile = LayoutInflater.from(_context).inflate(R.layout.author_first, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _authorHeight + _topCactusHeight - _topOverlap);
			authorTile.setLayoutParams(layout);
			
			authorTile.findViewById(R.id.top_cactus).getLayoutParams().height = _topCactusHeight;
			authorTile.findViewById(R.id.author).getLayoutParams().height = _authorHeight;			
		}
		else if(authorTile == null || authorTile.getTag().toString().equals("0"))
		{
			authorTile = LayoutInflater.from(_context).inflate(R.layout.author_middle, parent, false);
			AbsListView.LayoutParams layout = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, _authorHeight + _middleCactusHeight - _middleOverlap);
			authorTile.setLayoutParams(layout);
			
			authorTile.findViewById(R.id.middle_cactus).getLayoutParams().height = _middleCactusHeight;
			authorTile.findViewById(R.id.author).getLayoutParams().height = _authorHeight;
		}
		authorTile.setTag(position);
		
		// Author to display
		Author a = _values[position];
		
		// Avatar
		ImageButton avatar = (ImageButton)authorTile.findViewById(R.id.newImage);
		avatar.setImageBitmap(null);
		a.setAvatar(avatar);
		avatar.setTag(a._name);

		// Author Name
		TextView name = (TextView)authorTile.findViewById(R.id.newName);
		name.setText(a._name);
		
		// Spike count
		TextView spikeCount = (TextView)authorTile.findViewById(R.id.newSpikeCount);
		spikeCount.setText(String.valueOf(a._spikes));
		
		return authorTile;
	}	
}