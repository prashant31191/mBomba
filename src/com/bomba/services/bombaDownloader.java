package com.bomba.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class bombaDownloader extends Service {
	
	File bombaDir;
	boolean Downloading;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Downloading = false;

		Toast.makeText(getApplicationContext(), "checking external Storage",
				Toast.LENGTH_LONG).show();
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {

			
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			String root = Environment.getExternalStorageDirectory().toString();
			bombaDir = new File(root+"/bomba/content/.music");
			if(bombaDir.exists())
			{
				Toast.makeText(getApplicationContext(),
						"folders existent", Toast.LENGTH_LONG)
						.show();
			}else
			{
			bombaDir.mkdirs();
			Toast.makeText(getApplicationContext(),
					bombaDir.getPath(), Toast.LENGTH_LONG)
					.show();
			File nMedia = new File(bombaDir, ".nomedia");
			
			}
			
			
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			Toast.makeText(getApplicationContext(),
					" // We can only read the media", Toast.LENGTH_LONG).show();

			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			Toast.makeText(
					getApplicationContext(),
					" // Something else is wrong. It may be one of many other states, but all we need",
					Toast.LENGTH_LONG).show();

			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		return super.onStartCommand(intent, flags, startId);
	}
	
	public class contentGetter extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) {
			
			try {
				URL linkToSong = new URL(params[0]);
				HttpURLConnection songConnection = (HttpURLConnection) linkToSong.openConnection();
				songConnection.setRequestMethod("GET");
				songConnection.setDoOutput(true);
				songConnection.connect();
				File song = new File(bombaDir, params[0]);
				FileOutputStream fos = new FileOutputStream(song);
				InputStream miInputStream = songConnection.getInputStream();
				int fileSize = songConnection.getContentLength();
				int downloadedSize = 0;
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				while((bufferLength = miInputStream.read(buffer))>0)
				{
					Downloading = true;
						fos.write(buffer,0,bufferLength);
						downloadedSize += bufferLength;
				}
				Downloading = false;
				fos.close();
			} catch (Exception e) {
				Log.v("bombaDownloader", e.toString());
				e.printStackTrace();
			}
			
			return null;
		}
		
	}

}
