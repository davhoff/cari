package com.caricactus.displayer;

public class Author
{
	public Author(String name, int spikes)
	{
		_name = name;
		_spikes = spikes;
	}
	
	public String getName()
	{
		return _name;
	}
	String _name;
	
	public int getSpikes()
	{
		return _spikes;
	}
	public void addSpikes(int i)
	{
		_spikes += i;
	}
	int _spikes;
}
