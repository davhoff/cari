package com.caricactus.displayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ImageDbHelper extends SQLiteOpenHelper
{
	static final int DATABASE_VERSION = 11;
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
	
	// Query to create the Image table
	static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + IMAGE_TABLE_NAME + " (" +
			COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
			COLUMN_NAME_FILE + " TEXT" + "," +
			COLUMN_NAME_TYPE + " TEXT" + "," +
			COLUMN_NAME_AUTHOR + " TEXT" + "," +
			COLUMN_NAME_TAGS + " TEXT" + "," +
			COLUMN_NAME_DATE + " TEXT" + "," +
			COLUMN_NAME_TITLE + " TEXT" + "," +
			COLUMN_NAME_SPIKES + " INTEGER" +
			" )";
	// Query to delete the Image table
	static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + IMAGE_TABLE_NAME;
	
	/**
	 * Constructor
	 * @param context The context in which the DbHelper is created
	 */
	ImageDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }
	
	/**
	 * Called at the creation of the DB
	 * @param db New DB to configure
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.v("DB Creation",SQL_CREATE_ENTRIES);
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
		Log.v("DB Update",SQL_DELETE_ENTRIES);
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
		values.put(COLUMN_NAME_ID, items[0]);
		values.put(COLUMN_NAME_FILE, items[1]);
		values.put(COLUMN_NAME_TYPE, items[2]);
		values.put(COLUMN_NAME_AUTHOR, items[3]);
		values.put(COLUMN_NAME_TAGS, items[4]);
		values.put(COLUMN_NAME_DATE, items[5]);
		values.put(COLUMN_NAME_TITLE, items[6]);
		values.put(COLUMN_NAME_SPIKES, 0);
		Log.v("DB","Inserting image...");
		getDb().insert(IMAGE_TABLE_NAME, null, values);
	}
	
	long[] getImages()
	{
		String[] projection = { COLUMN_NAME_ID };
		
		Cursor c = getDb().query(IMAGE_TABLE_NAME, projection, null, null, null, null, null);
		
		c.moveToFirst();
		int count = 0;
		while(!c.isAfterLast())
		{
			c.moveToNext();
			count++;
		}

		c.moveToFirst();
		long[] result = new long[count];
		for(int i=0; i<count; i++)
		{
			long id = c.getLong(c.getColumnIndexOrThrow(COLUMN_NAME_ID));
			result[i] = id;
		}
		
		return result;
	}
}
