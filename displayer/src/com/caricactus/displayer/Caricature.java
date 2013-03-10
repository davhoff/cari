package com.caricactus.displayer;


public class Caricature
{
	int _id;
	String _file;
	String _date;
	
	String[] _tags;
	String _author;
	String _title;

	int _spikes;
	boolean _didSpike;
	
	public Caricature(int id, String file, String tagsString, String author, String title, String date, int spikes, int didSpike)
	{
		_id = id;
		_file = file;
		_date = date;
		_tags = tagsString.split("-");
		_author = author;
		_title = title;
		_spikes = spikes;
		_didSpike = didSpike == 1;
	}
	
	// TODO: getBigPic
	// getSmallPic
}
