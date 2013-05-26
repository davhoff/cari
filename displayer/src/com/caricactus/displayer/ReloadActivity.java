package com.caricactus.displayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;

public class ReloadActivity extends Activity
{
	static String STORAGE_DIR;
	
	long _waitingTime = 15000;
	long _initialTime = System.currentTimeMillis();
	long _refreshTime = 50;
	
	ImageDbHelper _dbHelper = new ImageDbHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reload);
		
		STORAGE_DIR = getFilesDir().getAbsolutePath();
		
		// Organize the update of the progress bar
		ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
		final Runnable r = new Runnable()
		{
		    public void run()
		    {
		        incrementLoading();
				ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
				progressBar.postDelayed(this, _refreshTime);
		    }
		};
		progressBar.postDelayed(r, _refreshTime);
		
		GeneralUpdateTask task = new GeneralUpdateTask();
		task.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reload, menu);
		return true;
	}
	
	
	void incrementLoading()
	{
		ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

		if(System.currentTimeMillis() > _initialTime + _waitingTime)
		{
			progressBar.setProgress(100);
			finish();
		}
		else
		{
			progressBar.setProgress((int)(System.currentTimeMillis() - _initialTime) * 100 / (int)_waitingTime);
		}
	}
	
	class GeneralUpdateTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
		    HttpClient client = AndroidHttpClient.newInstance("caricactus");

		    // Update DB list from web service
			try
			{
				long lastId = _dbHelper.getLastId();
			    String url = "http://caricactus.com/feeds/v1/appli/getLastImages.php?lastid="+lastId+"&device=android&version=v1";
		        Log.v("Gallery query",url);
				
		        HttpResponse response = client.execute(new HttpGet(url));
		        
			    StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			    {
			        ByteArrayOutputStream output = new ByteArrayOutputStream();
			        response.getEntity().writeTo(output);
			        output.close();
			        String responseString = output.toString();
			        Log.v("Gallery", "Response: " + responseString);
			        
			        _dbHelper.addImageList(responseString);
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
		    	Log.v("Gallery Ex", e.getMessage());
		    	// We should throw an error status to notify the activity
		    }
			
			// Update Caricature list in Gallery
			Gallery.Singleton().updateImageList();
			
			// Update Spikes here
			try
			{
			    String url = "http://caricactus.com/feeds/v1/appli/getSpikeCount.php";
		        Log.v("Gallery query",url);
				
		        HttpResponse response = client.execute(new HttpGet(url));
		        
			    StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			    {
			        ByteArrayOutputStream output = new ByteArrayOutputStream();
			        response.getEntity().writeTo(output);
			        output.close();
			        String responseString = output.toString();
			        Log.v("Gallery", "Response: " + responseString);
			        
			        _dbHelper.updateSpikes(responseString);
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
		    	Log.v("Gallery Ex", e.getMessage());
		    	// We should throw an error status to notify the activity
		    }
			
			// Update BestPic here
			try
			{
			    String url = "http://caricactus.com/feeds/v1/appli/getBest.php";
				
		        HttpResponse response = client.execute(new HttpGet(url));
		        
			    StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
			    {
			        ByteArrayOutputStream output = new ByteArrayOutputStream();
			        response.getEntity().writeTo(output);
			        output.close();
			        String responseString = output.toString();
			        
			        Gallery.Singleton().setBestPic(Integer.parseInt(responseString.split(",")[0]));
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
		    	Log.v("Gallery Ex", e.getMessage());
		    	// We should throw an error status to notify the activity
		    }
			
			return null;
		}
	}

}
