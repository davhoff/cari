package com.caricactus.displayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;


public class Caricature
{
	int _id;
	String _file;
	String _miniatureFile;
	String _date;
	
	String[] _tags;
	String _author;
	String _title;

	int _spikes;
	boolean _didSpike;
	
	public Caricature(int id, String fileName, String fileType, String tagsString, String author, String title, String date, int spikes, int didSpike)
	{
		_id = id;
		_file = fileName + "." + fileType;
		_miniatureFile = fileName + "_m." + fileType;
		_date = date;
		_tags = tagsString.split("-");
		_author = author;
		_title = title;
		_spikes = spikes;
		_didSpike = didSpike == 1;
		
		downloadImage();
		downloadMiniature();
	}
	
	// TODO: getBigPic
	// getSmallPic
	
	public void setImage(ImageButton view)
	{
		new LoadBitmapTask(view).execute();
	}
	
	class LoadBitmapTask extends AsyncTask<Void, Void, Bitmap>
	{
	    private final WeakReference<ImageButton> imageViewReference;

	    public LoadBitmapTask(ImageButton imageView)
	    {
	        imageViewReference = new WeakReference<ImageButton>(imageView);
	    }

	    @Override
	    protected Bitmap doInBackground(Void... params)
	    {
	    	try
	    	{
		    	Log.v("LoadBitmap:", _file);
		    	Bitmap bitmap = BitmapFactory.decodeFile(ReloadActivity.STORAGE_DIR + "/" + _file);
		    	Log.v("!! Loaded !!", _file);
		    	return bitmap;
	    	}
	    	catch(Exception ex)
	    	{
	    		Log.v("LoadBitmap:", "ERROR with file: " + _file);
	    		Log.v("LoadBitmap:", "msg:    " + _file);
	    		return null;
	    	}
	    }

	    @Override
	    protected void onPostExecute(Bitmap bitmap)
	    {
	        if (imageViewReference != null && bitmap != null)
	        {
	            final ImageButton imageView = imageViewReference.get();
	            if (imageView != null)
	            {            	
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
	public void spike()
	{
		Log.v("spike","id: "+_id);
	}
	
	void downloadImage()
	{
		Log.v("downloadImage",ReloadActivity.STORAGE_DIR + "/" + _file);
		File file = new File(ReloadActivity.STORAGE_DIR + "/" + _file);
		if(!file.exists())
		{
			DownloadImageTask task = new DownloadImageTask();
			task.execute(_file);
		}
	}

	void downloadMiniature()
	{
		Log.v("downloadImage",ReloadActivity.STORAGE_DIR + _miniatureFile);
		File file = new File(ReloadActivity.STORAGE_DIR + _miniatureFile);
		if(!file.exists())
		{
			DownloadImageTask task = new DownloadImageTask();
			task.execute(_miniatureFile);
		}
	}

	class DownloadImageTask extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params)
		{
			try
			{
			    HttpClient client = AndroidHttpClient.newInstance("caricactus");
			    String fileName = params[0];
			    
			    String url = "http://caricactus.com/cari/" + fileName;
		        Log.v(fileName, url);
				
		        HttpResponse response = client.execute(new HttpGet(url));
		        
			    StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			    {
			        FileOutputStream output = new FileOutputStream(ReloadActivity.STORAGE_DIR + "/" + fileName);
			        response.getEntity().writeTo(output);
			        output.close();
			    }
			    else
			    {
			        response.getEntity().getContent().close();
			        throw new IOException(statusLine.getReasonPhrase());
			    }
			}
		    catch(Exception e)
		    {
		    	Log.v("Gallery Ex", e.getMessage());
		    }
			
			return null;
		}
	}
}
