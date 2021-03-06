package com.bomba.b;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.MailTo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingActivity implements TabListener,
		LoginListener {
	DbHelper pickLists;

	public final String fbAppID = "280604498727857";
	private FBLoginManager fbl;
	SharedPreferences prefs;
	ProgressDialog pd;
	String name = null;
	LinearLayout mainl;
	
	int imageIDs[]  = {
			R.drawable.background_avril,
			R.drawable.background_daddy_owen,
			R.drawable.background_eko_dydda,
			R.drawable.background_it,
			R.drawable.background_joh_makini,
			R.drawable.background_kambua,
			R.drawable.background_kimya,
			R.drawable.background_octopizzo,
			R.drawable.background_mercy_masika
	};
	Random rand;

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
		rand = new Random();
		
		
		
		

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
			//fbl.loadFacebook();
		} else {
			//fbl.login();
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
	private Handler uiCallback = new Handler () {
	    public void handleMessage (Message msg) {
	        // do stuff with UI
	    	 
	    	int im = rand.nextInt(8);
	    	
	    	if (mainl != null)
	    		mainl.setBackgroundResource(imageIDs[im]);
	    }
	};
	
	private void init() {
		
		mainl = (LinearLayout) findViewById(R.id.mainll);
		Thread timer = new Thread() {
		    public void run () {
		        for (;;) {
		            // do stuff in a separate thread
		            uiCallback.sendEmptyMessage(0);
		            try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    // sleep for 3 seconds
		        }
		    }
		};
		
		timer.start();
	
		
		
		//startService(new Intent(MainActivity.this, bombaDownloader.class));
		ListView playlists = (ListView) findViewById(R.id.listPlayLists);

		pickLists = new DbHelper(MainActivity.this);
		// opendb

		pickLists.open();
		// get all the play lists

			if (pickLists.getPlayLists().length == 0) {
				Toast.makeText(MainActivity.this, "Create a playlist to continue",
						Toast.LENGTH_LONG).show();
				pd = ProgressDialog.show(MainActivity.this,
						"Synchronizing the database",
						"The world of music is coming right to you");
				pd.setCancelable(false);
				populatesongsFromOnline poppy = new populatesongsFromOnline();
				poppy.execute();
	
			} else {
				//add the playlist details to the hashmap
				final ArrayList<HashMap<String,String>> list = 

						new ArrayList<HashMap<String,String>>();
				for(int i=0; i<pickLists.getPlayLists().length; i++)
				{
					String p_name = pickLists.getPlayLists()[i];
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("name", pickLists.getPlayLists()[i]);
					item.put("amount", pickLists.getTracksInList(p_name).size()+"");
					list.add(item);
				}
			
				//use a simple adapter to map the items to the layout
				SimpleAdapter mSimpleAdapter = new SimpleAdapter(
						MainActivity.this,
						list,	
						R.layout.playlist_row,
						new String[] {"name","amount"},
						new int[]{R.id.tvPLName,R.id.tvSongs});
	
//				ArrayAdapter<String> adp = new ArrayAdapter<String>(
//						MainActivity.this, R.layout.playlist_row, R.id.tvPLName,
//						pickLists.getPlayLists());
				// close the database
				pickLists.close();
				playlists.setAdapter(mSimpleAdapter);
					
	
				playlists.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int pos, long arg3) {
						TextView v = (TextView) (findViewById(R.id.tvPLName));
						String pname = v.getText().toString();
						//String pname = ((TextView) view).getText().toString();
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
		protected void onPostExecute(Void result) {
			pd.dismiss();
			
			super.onPostExecute(result);
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				HttpClient getSongs = new DefaultHttpClient();
				HttpGet request = new HttpGet("http://109.74.201.47/select.php");
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
				Log.v("MAINACTIVITY LOAD ERROR", e.getMessage() + e.getCause().toString());

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
				name = user.getName();
				// update your status if logged in
				// graphApi.setStatus("Has logged into Bomba on Facebook");
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

			fbl.displayToast("Thank  you" +
					" for logging into Bomba");
		
		
	}

	@Override
	protected void onResume() {

		super.onResume();
	}
	
	public void changeBackground()
	{
		
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
