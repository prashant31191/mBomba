package com.bomba.services;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class bombaDownloader extends Service {

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

		Toast.makeText(getApplicationContext(), "checking external Storage",
				Toast.LENGTH_LONG).show();
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {

			
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			String root = Environment.getExternalStorageDirectory().toString();
			File bombaDir = new File(root+"/bomba/content/.music");
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

}
