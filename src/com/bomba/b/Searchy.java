package com.bomba.b;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import android.widget.ListView;
//import android.widget.SearchView;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import com.bomba.R;
import com.bomba.database.DbHelper;
import com.bomba.services.Mplayer;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class Searchy extends SlidingActivity implements OnQueryTextListener,
		OnClickListener {
	ListView tracks;
	String pls;
	Bundle m;
	DbHelper pickTracks;
	Button bPrev, bStop, bNext;
	ApplicationController BC;
	int imageIDs[] = { R.drawable.background_avril,
			R.drawable.background_daddy_owen, R.drawable.background_eko_dydda,
			R.drawable.background_it, R.drawable.background_joh_makini,
			R.drawable.background_kambua, R.drawable.background_kimya,
			R.drawable.background_octopizzo, R.drawable.background_mercy_masika };
	Random rand;
	LinearLayout mainl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pls = getIntent().getExtras().getString("p");
		getSupportActionBar().setTitle(pls);
		BC = (ApplicationController) getApplicationContext();
		rand = new Random();

		setContentView(R.layout.s_view);
		setBehindContentView(R.layout.slide);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0.35f);
		sm.setMenu(R.layout.slide);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		init();

		// set up the side list
		ListView lv = (ListView) findViewById(R.id.slideList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				switch (pos) {
				case 0:
					Intent n = new Intent(Searchy.this, SideSearch.class);
					n.putExtra("p", "Search:");
					startActivity(n);
					break;
				case 1:
					startActivity(new Intent(Searchy.this, MainActivity.class));
					break;
				case 2:
					break;

				}
			}
		});

	}

	private Handler uiCallback = new Handler() {
		public void handleMessage(Message msg) {
			// do stuff with UI

			int im = rand.nextInt(8);

			if (mainl != null)
				mainl.setBackgroundResource(imageIDs[im]);
		}
	};

	private void init() {

		mainl = (LinearLayout) findViewById(R.id.sview);
		Thread timer = new Thread() {
			public void run() {
				for (;;) {
					// do stuff in a separate thread
					uiCallback.sendEmptyMessage(0);
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} // sleep for 3 seconds
				}
			}
		};

		timer.start();
		bPrev = (Button) findViewById(R.id.Previous);
		bStop = (Button) findViewById(R.id.Stop);
		bNext = (Button) findViewById(R.id.Next);
		bPrev.setOnClickListener(this);
		bStop.setOnClickListener(this);
		bPrev.setOnClickListener(this);
		tracks = (ListView) findViewById(R.id.listofTracks);
		pickTracks = new DbHelper(Searchy.this);
		pickTracks.open();
		initpl();
		//click listener for the initial playlist being the items on the list before any action is taken on the activity 
		tracks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> vi, View v, int pos,
					long arg3) {
				RelativeLayout relly = (RelativeLayout) v;
				TextView tv = (TextView) relly.findViewById(R.id.tv_songs_link);
				String me = tv.getText().toString();

				String aI = "http://109.74.201.47/content/" + me;
				String sName = me;
				Log.w("THIS IS WHAT YOU HAVE CLICKED ", pos + " " + me);
				String UR = "/mnt/sdcard/bomba/content/.music/" + me + ".mp3";
				Intent view = new Intent(Searchy.this, Player_View.class);
				view.putExtra("artistImage", aI);
				Intent sing = new Intent(Searchy.this, Mplayer.class);
				Log.d("DATASOURCE", UR);
				sing.putExtra("songName", sName);
				sing.putExtra("url", UR);

				startActivity(view);
				startService(sing);

			}
		});

	}

	/*this loads the playlist from the database
	 * 
	 */
	private void initpl() {
		
		// if (pickTracks.DoesPlaylistExist(pls)) {
		loadList l = new loadList();
		l.execute(pls);
		// } else {
		Toast.makeText(Searchy.this,
				"Please search for a track to add to this playlist",
				Toast.LENGTH_LONG).show();
		// }
	}
	//background thread to load the list from the database to the UI 
	public class loadList extends AsyncTask<String, Void, Void> {
		ArrayList<HashMap<String, String>> what;

		@Override
		protected Void doInBackground(String... st) {
			pickTracks = new DbHelper(Searchy.this);
			pickTracks.open();
			what = pickTracks.getTracksInList(st[0]);
			Log.d("PNAME", st[0]);
			pickTracks.close();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (what != null) {
				Log.v("SEARCHYLOADER", what.size() + "");
				SimpleAdapter adp = new SimpleAdapter(
						Searchy.this,
						what,
						R.layout.searchrow,
						new String[] { pickTracks.TRACK_TITLE,
								pickTracks.A_STAGE_NAME, pickTracks.TRACK_file },
						new int[] { R.id.tv_songs_name, R.id.tvs_a_id,
								R.id.tv_songs_link });
				tracks.setAdapter(adp);

			} else {
				Toast.makeText(Searchy.this, "Search tracks to add to list",
						Toast.LENGTH_LONG).show();
				Log.v("async", "the arraylist is empty");
			}
		}

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		pickTracks.close();
	}

	@Override
	protected void onPause() {

		super.onPause();
		pickTracks.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.widget.SearchView searchView = new com.actionbarsherlock.widget.SearchView(
				getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search for Track");
		menu.add(0, 1, 1, "playlist")
				.setIcon(R.drawable.action_bar_add_playlist)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:

			SearchView sv = (SearchView) item.getActionView();
			sv.setOnQueryTextListener(this);

			// onSearchRequested();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {

		// pull data from the cursor
		Log.v("SEARCHY", "loading songs from db");
		populateSongs p = new populateSongs();
		p.execute(query);

		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {

		return false;
	}

	/*loads the list after a search has happened 
	 * this is where the magic or the fails happens
	 *   
	 */
	
	public class populateSongs extends AsyncTask<String, Void, Void> {
		Cursor mCursor;

		@Override
		protected void onPostExecute(Void result) {
			if (mCursor.getCount() <= 0) {
				Toast.makeText(
						Searchy.this,
						"We don't have any tracks with that name but we will look around",
						Toast.LENGTH_LONG).show();
			} else {
				TextView header = new TextView(Searchy.this);
				header.setText("long press to add to playlist");
				tracks.invalidate();
				// tracks.addHeaderView(header);
				// tracks.addFooterView(header);
				@SuppressWarnings("deprecation")
				ListAdapter adp = new SimpleCursorAdapter(
						Searchy.this,
						R.layout.searchrow,
						mCursor,
						new String[] { pickTracks.TRACK_TITLE,
								pickTracks.A_STAGE_NAME, pickTracks.TRACK_file },
						new int[] { R.id.tv_songs_name, R.id.tvs_a_id,
								R.id.tv_songs_link });
				tracks.setAdapter(adp);

			}

			super.onPostExecute(result);
		}	

		@Override
		protected Void doInBackground(String... params) {
			pickTracks.open();
			mCursor = pickTracks.getSearched(params[0]);
			Log.v("SEARCHY", mCursor.getCount() + "");
			startManagingCursor(mCursor);

			// tracks.setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View v, int pos,
			// long arg3) {
			//
			// RelativeLayout relly = (RelativeLayout) v;
			// TextView tv = ((TextView) findViewById(R.id.tv_songs_link));
			// String me = tv.getText().toString();
			//
			// String aI = "http://109.74.201.47/content/" + me;
			// String sName = "your favorite Song";
			// String UR = "http://109.74.201.47/content/" + me+".mp3";
			// Intent view = new Intent(Searchy.this, Player_View.class);
			// view.putExtra("artistImage", aI);
			// Intent sing = new Intent(Searchy.this, Mplayer.class);
			// sing.putExtra("songName", sName);
			// sing.putExtra("url", UR);
			//
			// // startActivity(view);
			// startService(sing);
			//
			// }
			// });

			tracks.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> v, View arg1,
						int arg2, long arg3) {
					// RelativeLayout relly = (RelativeLayout) v;
					TextView tv = ((TextView) findViewById(R.id.tv_songs_link));
					String me = tv.getText().toString();
					pickTracks.open();
					int pl_id = pickTracks.getPlaylistId(pls);
					int tr_id = pickTracks.getTrackId(me);
					try {
						pickTracks.AddSongToPlaylist(tr_id, pl_id, pls);
						pickTracks.close();
					} catch (Exception e) {
						Log.d("SQLEXCEPTION", e.toString() + " "
								+ e.getCause().toString());
					}

					Toast.makeText(Searchy.this,
							me + " has been added to" + pls, Toast.LENGTH_LONG)
							.show();

					initpl();
					contentGetter cG = new contentGetter();
					cG.execute(me + ".mp3");

					return false;
				}
			});
			return null;
		}
	}

	public class contentGetter extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {

			try {
				URL linkToSong = new URL("http://109.74.201.47/content/"
						+ params[0]);
				Log.d("GETTERURL", linkToSong.toString());
				HttpURLConnection songConnection = (HttpURLConnection) linkToSong
						.openConnection();
				songConnection.setRequestMethod("GET");
				songConnection.setDoOutput(true);
				songConnection.connect();

				File song = new File(BC.bombaDir, params[0]);
				if (song.exists()) {
					Toast.makeText(Searchy.this,
							"song already sync~ed to another play list",
							Toast.LENGTH_LONG).show();
				} else {
					FileOutputStream fos = new FileOutputStream(song);
					InputStream miInputStream = songConnection.getInputStream();
					int fileSize = songConnection.getContentLength();
					int downloadedSize = 0;
					byte[] buffer = new byte[1024];
					int bufferLength = 0;
					while ((bufferLength = miInputStream.read(buffer)) > 0) {
						Log.d("DOWNLOADER", "the download has begun");
						BC.Downloading = true;
						fos.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
					}
					Log.d("DOWNLOADER", downloadedSize + "");
					BC.Downloading = false;
					fos.close();
				}
			} catch (Exception e) {
				Log.d("bombaDownloader", e.toString());
				e.printStackTrace();
			}

			return null;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Stop:
			stopService(new Intent(Searchy.this, Mplayer.class));
			break;
		}

	}
}
