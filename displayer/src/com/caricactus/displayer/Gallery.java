package com.caricactus.displayer;

import java.util.ArrayList;
import java.util.Random;


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
	
	public void updateSpikes()
	{
		int[][] spikeData = ImageDbHelper.getCurrent().getSpikes();
		for(int i=0; i<spikeData.length; i++)
		{
			Caricature c = getCaricature(spikeData[i][0]);
			c._spikes = spikeData[i][1];
		}
	}

	int _bestPic = 0;
	public void setBestPic(int id)
	{
		_bestPic = id;
	}
	
	public Caricature getBestPic()
	{
		return getCaricature(_bestPic);
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
	
	public Caricature[] getSelectedList()
	{
		int count = 0;
		for(int i=0; i<_isSelected.length; i++)
		{
			if(_isSelected[i])
				count++;
		}
		
		Caricature[] selectedList = new Caricature[count];
		int j = 0;
		for(int i=0; i<_isSelected.length; i++)
		{
			if(_isSelected[i])
			{
				selectedList[j] = _imageList[i];
				j++;
			}
		}
		
		return selectedList;
	}
	
	public Author[] getAuthorList()
	{
		Author[] result = new Author[_authorList.size()];
		return _authorList.toArray(result);
	}
	
	
	public int getRandomId()
	{
		return _imageList[_rand.nextInt(_imageList.length)]._id;
	}
	final Random _rand = new Random();

}
