package com.caricactus.displayer;

import java.io.ByteArrayOutputStream;
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
	
	public void spike(ImageButton source, ISpikeDisplayer viewer)
	{
		if(!_didSpike)
		{
			_didSpike = true;
			// Update the spike button
			source.setImageResource(R.drawable.spike_big_pic_disabled);
			source.setEnabled(false);
			
			if(viewer != null)
				new SpikeTask(viewer).execute();
		}
		else
			Log.v("spike","You already spiked the id: "+_id);
	}
	
	
	class SpikeTask extends AsyncTask<Void, Void, Void>
	{
		private final WeakReference<ISpikeDisplayer> _viewerReference;
		
	    public SpikeTask(ISpikeDisplayer viewer)
	    {
	    	_viewerReference = new WeakReference<ISpikeDisplayer>(viewer);
	    }

	    @Override
	    protected Void doInBackground(Void... params)
	    {
	    	try
	    	{
		    	// Notify the DB with the new spike
	    		ImageDbHelper.getCurrent().spikeImage(_id);
	    		
	    		// Spike web request
			    HttpClient client = AndroidHttpClient.newInstance("caricactus");
	    		try
				{
				    String url = "http://caricactus.com/feeds/v1/appli/spike.php?id="+_id+"&device=android&version=v1";
					client.execute(new HttpGet(url));
				}
			    catch(Exception e)
			    {
			    	Log.v("Caricature Ex", e.getMessage());
			    }
	    		
	    		// Update all spikes in DB
	    		try
				{
				    String url = "http://caricactus.com/feeds/v1/appli/getSpikeCount.php";
					
			        HttpResponse response = client.execute(new HttpGet(url));
			        
				    StatusLine statusLine = response.getStatusLine();
				    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
				    {
				        ByteArrayOutputStream output = new ByteArrayOutputStream();
				        response.getEntity().writeTo(output);
				        output.close();
				        String responseString = output.toString();
				        Log.v("Gallery", "Response: " + responseString);
				        
				        ImageDbHelper.getCurrent().updateSpikes(responseString);
				    }
				    else
				    {
				        //Closes the connection.
				        response.getEntity().getContent().close();
				        throw new IOException(statusLine.getReasonPhrase());
				    }
				}
			    catch(Exception e)
			    {
			    	Log.v("Caricature Ex", e.getMessage());
			    }
		    	
		    	// Update spikes in Gallery
	    		Gallery.Singleton().updateSpikes();
	    	}
	    	catch(Exception ex)
	    	{
	    		
	    	}
	    	return null;
	    }

	    @Override
	    protected void onPostExecute(Void d)
	    {
	    	// Trigger an update for all visible spikes
	    	if (_viewerReference != null)
	        {
	            final ISpikeDisplayer viewer = _viewerReference.get();
	            if (viewer != null)
	            {            	
	            	viewer.refreshSpikes();
	            }
	        }
	    }
	}
	
	
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
	    		BitmapFactory.Options options = new BitmapFactory.Options();
	    		options.inPurgeable = true;
	    		options.inInputShareable = true;
		    	Bitmap bitmap = BitmapFactory.decodeFile(ReloadActivity.STORAGE_DIR + "/" + _file, options);
		    	return bitmap;
	    	}
	    	catch(Exception ex)
	    	{
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
		
	void downloadImage()
	{
		File file = new File(ReloadActivity.STORAGE_DIR + "/" + _file);
		if(!file.exists())
		{
			DownloadImageTask task = new DownloadImageTask();
			task.execute(_file);
		}
	}

	void downloadMiniature()
	{
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
		    	Log.v("Failed to download image: ", e.getMessage());
		    }
			
			return null;
		}
	}
}
