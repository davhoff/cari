package com.caricactus.displayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDbHelper extends SQLiteOpenHelper
{
	public static ImageDbHelper getCurrent()
	{
		return _current;
	}
	public static void Close()
	{
		_current = null;
	}
	private static ImageDbHelper _current = null;
	
	
	
	static final int DATABASE_VERSION = 17;
	static final String DATABASE_NAME = "Images.db";
	
	// Contract
	static final String IMAGE_TABLE_NAME = "caricatures";
	static final String COLUMN_NAME_ID = "id";
	static final String COLUMN_NAME_FILE = "file";
	static final String COLUMN_NAME_TYPE = "type";
	static final String COLUMN_NAME_AUTHOR = "author";
	static final String COLUMN_NAME_TAGS = "tags";
	static final String COLUMN_NAME_DATE = "date";
	static final String COLUMN_NAME_TITLE = "title";
	static final String COLUMN_NAME_SPIKES = "spikes";
	static final String COLUMN_NAME_DIDSPIKE = "didspike";
	
	// Query to create the Image table
	static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + IMAGE_TABLE_NAME + " ( " +
			COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
			COLUMN_NAME_FILE + " TEXT" + ", " +
			COLUMN_NAME_TYPE + " TEXT" + ", " +
			COLUMN_NAME_AUTHOR + " TEXT" + ", " +
			COLUMN_NAME_TAGS + " TEXT" + ", " +
			COLUMN_NAME_DATE + " TEXT" + ", " +
			COLUMN_NAME_TITLE + " TEXT" + ", " +
			COLUMN_NAME_SPIKES + " INTEGER" + ", " +
			COLUMN_NAME_DIDSPIKE + " INTEGER" +
			" )";
	// Query to delete the Image table
	static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + IMAGE_TABLE_NAME;
	
	/**
	 * Constructor
	 * @param context The context in which the DbHelper is created
	 */
	ImageDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		_current = this;
	}
	
	/**
	 * Called at the creation of the DB
	 * @param db New DB to configure
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_ENTRIES);
	}
	
	/**
	 * Called when updating the DB
	 * @param db DB to upgrade
	 * @param oldVersion Version from which we upgrade
	 * @param newVersion Version to which we upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	/**
	 * Gets the DB
	 * @return The DB with the application's data
	 */
	private SQLiteDatabase getDb()
	{
		if(_db == null)
			_db = getWritableDatabase();
		
		return _db;
	}
	SQLiteDatabase _db;
	
	/**
	 * Adds images to the DB from a raw input
	 * @param rawData Raw input in csv format
	 */
	void addImageList(String rawData)
	{
		String[] lines = rawData.split(";");
		for(int i=0; i<lines.length; i++)
		{
			addImage(lines[i]);
		}
	}
	
	/**
	 * Adds an image to the DB from a raw input
	 * @param rawData Raw input, items separated by commas
	 */
	private void addImage(String rawData)
	{
		String[] items = rawData.split(",");
		if(items.length < 7)
			return;
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, Integer.parseInt(items[0]));
		values.put(COLUMN_NAME_FILE, items[1]);
		values.put(COLUMN_NAME_TYPE, items[2]);
		values.put(COLUMN_NAME_AUTHOR, items[3]);
		values.put(COLUMN_NAME_TAGS, items[4]);
		values.put(COLUMN_NAME_DATE, items[5]);
		values.put(COLUMN_NAME_TITLE, items[6]);
		values.put(COLUMN_NAME_SPIKES, 0);
		values.put(COLUMN_NAME_DIDSPIKE, 0);
		
		getDb().insert(IMAGE_TABLE_NAME, null, values);
	}
	
	public long getLastId()
	{
		String[] projection = { COLUMN_NAME_ID };
		
		Cursor c = getDb().query(IMAGE_TABLE_NAME, projection, null, null, null, null, COLUMN_NAME_ID + " DESC", "1");
		c.moveToFirst();
		
		if(c.isAfterLast())
			return -1;
		else
			return c.getLong(c.getColumnIndexOrThrow(COLUMN_NAME_ID));
	}
	
	public void updateSpikes(String rawData)
	{
		String[] items = rawData.split(";");
		for(String s : items)
		{
			String[] pair = s.split(",");
			if(pair.length != 2)
				continue;
			
			ContentValues args = new ContentValues();
		    args.put(COLUMN_NAME_SPIKES, Integer.parseInt(pair[1]));
			getDb().update(IMAGE_TABLE_NAME, args, COLUMN_NAME_ID+"="+pair[0], null);  
		}
		
	}
	
	public Caricature[] getImagesFromDb()
	{
		String[] projection =
		{
			COLUMN_NAME_ID,
			COLUMN_NAME_FILE,
			COLUMN_NAME_TYPE,
			COLUMN_NAME_AUTHOR,
			COLUMN_NAME_TAGS,
			COLUMN_NAME_DATE,
			COLUMN_NAME_TITLE,
			COLUMN_NAME_SPIKES,
			COLUMN_NAME_DIDSPIKE
		};
		
		// TODO: order by date
		Cursor c = getDb().query(IMAGE_TABLE_NAME, projection, null, null, null, null, null);
		
		c.moveToFirst();
		int count = 0;
		while(!c.isAfterLast())
		{
			c.moveToNext();
			count++;
		}

		c.moveToFirst();
		Caricature[] imageList = new Caricature[count];
		for(int i=0; i<count; i++)
		{
			int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_ID));
			String file = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_FILE));
			String filetype = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_TYPE));
			String author = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_AUTHOR));
			String tags = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_TAGS));
			String date = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_DATE));
			String title = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
			int spikes = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_SPIKES));
			int didSpike = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_DIDSPIKE));
			imageList[i] = new Caricature(id, file+"."+filetype, tags, author, title, date, spikes, didSpike);
			c.moveToNext();
		}
		
		return imageList;
	}
}
