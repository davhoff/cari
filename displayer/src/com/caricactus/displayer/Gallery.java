package com.caricactus.displayer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Gallery
{
	/**
	 * Private constructor to prevent instantiation.
	 */
	private Gallery() { }
	
	/**
	 * Singleton implementation
	 * @return Gallery unique instance
	 */
	public final static Gallery Singleton()
	{
		if (_instance == null)
		{
			synchronized(Gallery.class)
			{
				if (_instance == null)
			   		_instance = new Gallery();
			}
		}
		return _instance;
	}
	private static volatile Gallery _instance = null;
	
	private int _screenWidth;
	
	private Caricature[] _imageList;
	private boolean[] _isSelected;
	
	private ArrayList<Author> _authorList;
	
	/**
	 * Gets a specific Caricature in the list
	 * @param id ID of the Caricature to retrieve
	 * @return The Caricature (null if not found)
	 */
	public Caricature getCaricature(int id)
	{
		for(Caricature c : _imageList)
		{
			if(c._id == id)
			{
				return c;
			}
		}
		return null;
	}
	
	public void updateImageList()
	{
		_imageList = ImageDbHelper.getCurrent().getImagesFromDb();
		_isSelected = new boolean[_imageList.length];
		selectAll();
		
		updateAuthorList();
	}
	
	public void selectAll()
	{
		for(int i=0; i<_imageList.length; i++)
			_isSelected[i] = true;
	}
	
	public void selectTag(String tag)
	{
		for(int i=0; i<_imageList.length; i++)
		{
			_isSelected[i] = false;
			Caricature c = _imageList[i];
			for(int j=0; j<c._tags.length; j++)
				if(tag.equals(c._tags[j]))
					_isSelected[i] = true;
		}
	}

	public void selectAuthor(String author)
	{
		for(int i=0; i<_imageList.length; i++)
		{
			_isSelected[i] = author.equals(_imageList[i]._author);
		}
	}
	
	public void updateAuthorList()
	{
		_authorList = new ArrayList<Author>();
		for(Caricature c : _imageList)
		{
			boolean knownAuthor = false;
			for(Author a : _authorList)
			{
				if(a.getName().equals(c._author))
				{
					knownAuthor = true;
					a.addSpikes(c._spikes);
					break;
				}
			}
			if(!knownAuthor)
				_authorList.add(new Author(c._author, c._spikes));
		}
	}
	
	
	public void buildScroller(RelativeLayout scrollerBody, Context context, String type)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		_screenWidth = size.x;
		
		View pic = null;
		View cactus = null;
		boolean isFirst = true;
		for(int i=0; i<_imageList.length; i++)
		{
			if(_isSelected[i])
			{
				if(isFirst)
				{
					isFirst = false;
					if(type.equals("BIG"))
						pic = createBigPic(_imageList[i], context, 0);
					else
						pic = createSmallPic(_imageList[i], context, 0);
					scrollerBody.addView(pic);
				}
				else
				{
					cactus = createMiddleCactus(context, pic.getId());
					scrollerBody.addView(cactus);
					
					if(type.equals("BIG"))
						pic = createBigPic(_imageList[i], context, cactus.getId());
					else
						pic = createSmallPic(_imageList[i], context, pic.getId());
					scrollerBody.addView(pic);
				}
			}
		}
	}

	private View createBigPic(Caricature c, Context context, int previousId)
	{
		// Layout for the new cactus piece
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, _screenWidth * 10 / 16);
		layout.addRule(RelativeLayout.BELOW, previousId);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout bigPic = (LinearLayout) inflater.inflate(R.layout.bigpic, null, false);
		bigPic.setLayoutParams(layout);
		bigPic.setId(previousId+10);
		
		// Image
		ImageButton image = (ImageButton)bigPic.findViewById(R.id.newImage);
		image.setId(previousId+1);
		image.setImageResource(R.drawable.reload_button);
		image.setTag(c._id);
		
		// Author
		TextView author = (TextView)bigPic.findViewById(R.id.newAuthor);
		author.setId(previousId+2);
		author.setText(c._author);
		
		// Spike button
		ImageButton spikeButton = (ImageButton)bigPic.findViewById(R.id.newSpikeButton);
		spikeButton.setId(previousId+3);
		spikeButton.setTag(c._id);
		
		// Spike count
		TextView spikeCount = (TextView)bigPic.findViewById(R.id.newSpikeCount);
		spikeCount.setId(previousId+4);
		spikeCount.setText(String.valueOf(c._spikes));
		
		return bigPic;
	}
	
	private View createSmallPic(Caricature c, Context context, int previousId)
	{
		// Layout for the new cactus piece
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, _screenWidth * 10 / 35);
		layout.addRule(RelativeLayout.BELOW, previousId);
		
		// Build the container
		ImageView bigPic = new ImageView(context);
		bigPic.setImageResource(R.drawable.small_pic);
		bigPic.setLayoutParams(layout);
		bigPic.setId(previousId+1);

		return bigPic;
	}
	
	private View createMiddleCactus(Context context, int previousId)
	{
		// Layout for the new cactus piece
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, _screenWidth * 10 / 85);
		layout.addRule(RelativeLayout.BELOW, previousId);

		// Build the cactus ImageView
		ImageView cactus = new ImageView(context);
		cactus.setImageResource(R.drawable.cactus_tronc);
		cactus.setLayoutParams(layout);
		cactus.setId(previousId+1);
		
		return cactus;
	}
}
