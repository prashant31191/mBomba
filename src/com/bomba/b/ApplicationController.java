package com.bomba.b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ApplicationController extends Application {

	File bombaDir;
	boolean Downloading;

	@Override
	public void onCreate() {
		super.onCreate();
		Downloading = false;

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {

			mExternalStorageAvailable = mExternalStorageWriteable = true;
			String root = Environment.getExternalStorageDirectory().toString();
			bombaDir = new File(root + "/bomba/content/.music");
			if (bombaDir.exists()) {
				Log.d("DIRECTORY", "Directory exists");

			} else {
				bombaDir.mkdirs();
				Log.d("DIRECTORY", "Creating the Directory");
				
				File nMedia = new File(bombaDir, ".nomedia");
				if(!nMedia.exists())
				{
				try
				{
				nMedia.createNewFile();
				}
				catch(Exception e)
				{
					Log.d("DIRECTORY", e.toString());
				}
				}
				

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

	}

}
