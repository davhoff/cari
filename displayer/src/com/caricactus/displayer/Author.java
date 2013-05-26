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

public class Author
{
	String _name;
	String _file;
	
	int _spikes;
	public int getSpikes() { return _spikes; }
	public void addSpikes(int i) { _spikes += i; }
	
	public Author(String name, int spikes)
	{
		_name = name;
		_file = _name + "_a.png";
		_spikes = spikes;
		
		downloadAvatar();
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setAvatar(ImageButton view)
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
		    	Bitmap bitmap = BitmapFactory.decodeFile(ReloadActivity.STORAGE_DIR + "/" + _file);
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
	
	void downloadAvatar()
	{
		File file = new File(ReloadActivity.STORAGE_DIR + "/" + _file);
		if(!file.exists())
		{
			DownloadImageTask task = new DownloadImageTask();
			task.execute(_file);
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
			    
			    String url = "http://caricactus.com/dessinateurs/" + fileName;
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
		    	Log.v("Failed to download avatar: ", e.getMessage());
		    }
			
			return null;
		}
	}
}
