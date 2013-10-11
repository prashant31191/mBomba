 package com.bomba.b;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.bomba.R;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Player_View extends SlidingActivity {
	
	ImageView ivArtist;
	TextView tvSong, tvArtist;
	String TAG = "PLAYER_VIEW";
	ImageButton ibAdd, ibBAck, ibPlay, ibNext,ibVolume;
	SeekBar sSongs;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_view);
		setBehindContentView(R.layout.slide);
		Bundle e = getIntent().getExtras();
		
		loadUI();
		
		
		
		imageLoader iL = new imageLoader();
		iL.execute(e.getString("artistImage"));
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Toast.makeText(Player_View.this, "and you are back", Toast.LENGTH_LONG).show();
	}
	
	
	private void loadUI() {
		
		ivArtist = (ImageView)findViewById(R.id.artistPic);
		ibAdd = (ImageButton)findViewById(R.id.iBAdd);
		ibBAck = (ImageButton)findViewById(R.id.iBprevious);
		ibPlay = (ImageButton)findViewById(R.id.iBplay);
		ibNext = (ImageButton)findViewById(R.id.iBnext);
		ibVolume = (ImageButton)findViewById(R.id.iBvol);
		sSongs = (SeekBar)findViewById(R.id.nowPlayingBar);
		
		
	}


	public class imageLoader extends AsyncTask<String, Void, Void>
	{
		BitmapFactory.Options Boptions = new BitmapFactory.Options();
		
	
	    //options.inSampleSize = 8;
	    Bitmap bmp = null;

		@Override
		protected Void doInBackground(String... params) {
			
			try
			{
				bmp = getRemoteImage(new URL(params[0]));
				
				
			}catch(Exception e)
			{
				
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(bmp != null)
			{
				ivArtist.setImageBitmap(bmp);
			}
			else
			{
				Log.v(TAG, "the bit is empty ");
			}
		}
		
	}
	public Bitmap getRemoteImage(final URL aURL) {
	    try {
	        final URLConnection conn = aURL.openConnection();
	        conn.connect();
	        final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
	        final Bitmap bm = BitmapFactory.decodeStream(bis);
	        bis.close();
	        return bm;
	    } catch (Exception e)
	    {
	    	Log.v(TAG, e.toString());
	    }
	    return null;
	}

}
