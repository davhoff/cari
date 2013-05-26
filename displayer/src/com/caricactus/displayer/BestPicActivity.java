package com.caricactus.displayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class BestPicActivity extends Activity implements ISpikeDisplayer
{
	Caricature _caricature;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_best_pic);
	
		// Get appropriate caricature
		_caricature = Gallery.Singleton().getBestPic();
				
		if(_caricature != null)
		{
			// Set the image
			ImageButton image = (ImageButton)findViewById(R.id.image);
			_caricature.setImage(image);
			
			// Spike button
			ImageButton spikeButton = (ImageButton)findViewById(R.id.spikeButton);
			spikeButton.setTag(_caricature._id);
			if(_caricature._didSpike)
			{
				spikeButton.setImageResource(R.drawable.spike_big_pic_disabled);
				spikeButton.setEnabled(false);
			}
			else
			{
				spikeButton.setImageResource(R.drawable.spike_big_pic);
				spikeButton.setEnabled(true);
			}
			
			// Spike count
			refreshSpikes();
			
			// Author
			TextView title = (TextView)findViewById(R.id.author);
			title.setText(_caricature._author);
		}
	}
	
	public void backButton(View view)
	{
		finish();
	}
	
	public void spikeButton(View view)
	{
		_caricature.spike((ImageButton)view, this);
	}
	
	public void imageButton(View view)
	{
		Intent intent = new Intent(this, FullscreenActivity.class);
		intent.putExtra("IMAGE_ID", _caricature._id);
		startActivity(intent);
	}
	
	public void refreshSpikes()
	{
		TextView spikeCount = (TextView)findViewById(R.id.spikeCount);
		spikeCount.setText(String.valueOf(_caricature._spikes));
	}
}
