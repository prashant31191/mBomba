package com.bomba.b;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.bomba.database.DbHelper;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingActivity implements TabListener {
	DbHelper pickLists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.slide);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0.35f);
		sm.setMenu(R.layout.slide);

		ListView lv = (ListView) findViewById(R.id.slideList);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				switch (pos) {
				case 0:
					Intent n = new Intent(MainActivity.this, SideSearch.class);
					n.putExtra("p", "Search:");
					startActivity(n);
					break;
				case 1:
					toggle();
					break;
				case 2:
					break;

				}
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// ActionBar.Tab tabP = getSupportActionBar().newTab();
		// tabP.setText("Playlists");
		// tabP.setTabListener(this);
		// getSupportActionBar().addTab(tabP);
		//
		// ActionBar.Tab tabart = getSupportActionBar().newTab();
		// tabart.setText("Artists");
		// tabart.setTabListener(this);
		// getSupportActionBar().addTab(tabart);
		//
		// ActionBar.Tab tabalbs = getSupportActionBar().newTab();
		// tabalbs.setText("Albums");
		// tabalbs.setTabListener(this);
		// getSupportActionBar().addTab(tabalbs);
		//
		// ActionBar.Tab tabtracks = getSupportActionBar().newTab();
		// tabtracks.setText("Tracks");
		// tabtracks.setTabListener(this);
		// getSupportActionBar().addTab(tabtracks);
		//
		// getSupportActionBar().setStackedBackgroundDrawable(
		// getResources().getDrawable(R.drawable.tab_bar_background));
		init();

	}

	private void init() {
		ListView playlists = (ListView) findViewById(R.id.listPlayLists);

		pickLists = new DbHelper(MainActivity.this);
		// opendb

		pickLists.open();
		// get all the play lists

		if (pickLists.getPlayLists().length == 0) {
			Toast.makeText(MainActivity.this, "Create a playlist to continue",
					Toast.LENGTH_LONG).show();
			populatesongsFromOnline poppy = new populatesongsFromOnline();
			poppy.execute();

		} else {

			ArrayAdapter<String> adp = new ArrayAdapter<String>(
					MainActivity.this, R.layout.playlist_row, R.id.tvPLName,
					pickLists.getPlayLists());
			// close the database
			pickLists.close();
			playlists.setAdapter(adp);

			playlists.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long arg3) {

					String pname = ((TextView) view).getText().toString();
					// Toast.makeText(MainActivity.this, pname,
					// Toast.LENGTH_LONG).show();
					Intent specList = new Intent(MainActivity.this,
							Searchy.class);
					specList.putExtra("p", pname);
					startActivity(specList);

				}
			});
		}

	}

	public class populatesongsFromOnline extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				HttpClient getSongs = new DefaultHttpClient();
				HttpGet request = new HttpGet(
						"http://semasoftltd.com/songs_syncer.php");
				HttpResponse resp = getSongs.execute(request);
				String jsonPayload = EntityUtils.toString(resp.getEntity());
				JSONObject parentObject = new JSONObject(jsonPayload);
				JSONArray parentArray = parentObject.getJSONArray("songs");
				JSONObject childObject;
				for (int i = 0; i < parentArray.length(); i++) {
					childObject = parentArray.getJSONObject(i).getJSONObject(
							"song");
					pickLists.AddSongsToLocal(childObject.getInt("song_id"),
							childObject.getString("song_name"),
							childObject.getString("song_link"));

				}
				Log.v("MAINACTIVITY", "songs added to the database");

			} catch (Exception e) {
				Log.v("MAINACTIVITY", e.toString());

			} finally {

			}
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("Playlists")
				.setTitle("Playlists")
				.setIcon(R.drawable.action_bar_add_playlist)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// if (item.getTitle().equals("Playlists")) {
		// startActivity(new Intent(MainActivity.this, AddPlaylist.class));
		// }
		switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(MainActivity.this, AddPlaylist.class));
			break;
		case 16908332:
			toggle();
			break;
		}
		Log.v("MAINACTIVITY", item.getItemId() + "");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		pickLists.close();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		pickLists.close();
	}

}
