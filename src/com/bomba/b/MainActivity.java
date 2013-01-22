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

import com.bomba.R;
import com.bomba.database.DbHelper;
import com.easy.facebook.android.apicall.GraphApi;
import com.easy.facebook.android.data.User;
import com.easy.facebook.android.error.EasyFacebookError;
import com.easy.facebook.android.facebook.FBLoginManager;
import com.easy.facebook.android.facebook.Facebook;
import com.easy.facebook.android.facebook.LoginListener;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import android.content.Intent;
import android.content.SharedPreferences;

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

public class MainActivity extends SlidingActivity implements TabListener,
		LoginListener {
	DbHelper pickLists;

	public final String fbAppID = "280604498727857";
	private FBLoginManager fbl;
	SharedPreferences prefs;

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
		prefs = getApplicationContext().getSharedPreferences("meprefs", 0);
		

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

		String[] permissions = { "user_about_me", "user_activities",
				"user_birthday", "user_checkins", "user_education_history",
				"user_events", "user_groups", "user_hometown",
				"user_interests", "user_likes", "user_location", "user_notes",
				"user_online_presence", "user_photo_video_tags", "user_photos",
				"user_relationships", "user_relationship_details",
				"user_religion_politics", "user_status", "user_videos",
				"user_website", "user_work_history", "email",

				"read_friendlists", "read_insights", "read_mailbox",
				"read_requests", "read_stream", "xmpp_login", "ads_management",
				"create_event", "manage_friendlists", "manage_notifications",
				"offline_access", "publish_checkins", "publish_stream",
				"rsvp_event", "sms",
				// "publish_actions",

				"manage_pages"

		};
		fbl = new FBLoginManager(this, R.layout.activity_main, fbAppID,
				permissions);
		if (fbl.existsSavedFacebook()) {
			fbl.loadFacebook();
		} else {
			fbl.login();
		}

		init();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		fbl.loginSuccess(data);
	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Log.v("theback", "theback has been pressed");
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
						"http://41.139.204.179/select.php");
				HttpResponse resp = getSongs.execute(request);
				String jsonPayload = EntityUtils.toString(resp.getEntity());
				JSONObject parentObject = new JSONObject(jsonPayload);
				JSONArray parentArray = parentObject.getJSONArray("songs");
				JSONObject childObject;
				for (int i = 0; i < parentArray.length(); i++) {
					childObject = parentArray.getJSONObject(i).getJSONObject(
							"song");
					pickLists.open();
					pickLists.AddSongsToLocalMaster(childObject.getInt("_id"),
							childObject.getString("track_title"),
							childObject.getString("a_legal_name"),
							childObject.getString("a_stage_name"),
							childObject.getString("featured_a"),
							childObject.getString("album_title"),
							childObject.getString("track_number"),
							childObject.getString("genre"),
							childObject.getString("cut"),
							childObject.getString("producer"),
							childObject.getString("studio"),
							childObject.getString("management"),
							childObject.getString("label"),
							childObject.getString("image_file"),
							childObject.getString("track_file"));

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

	class FbThread extends Thread {
		Facebook fb;
		User user = new User();

		FbThread(Facebook fb) {
			this.fb = fb;
		}

		public void run() {
			try {
				GraphApi graphApi = new GraphApi(fb);
				user = graphApi.getMyAccountInfo();
				// update your status if logged in
				//graphApi.setStatus("Has logged into Bomba on Facebook");
				Log.v("FACEBOOK", user.toString());
			} catch (EasyFacebookError e) {
				Log.d("TAG: ", e.toString());
			}
		}

		public User getUser() {
			return user;
		}
	}

	public void loginSuccess(Facebook facebook) {
		FbThread fbThread = new FbThread(facebook);
		User user = fbThread.getUser();

		fbThread.start();

		fbl.displayToast("Hey, " + user.getName() + "! Login success!");
	}

	@Override
	public void logoutSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginFail() {
		fbl.displayToast("this was an epic fail at logging in");

	}

}
