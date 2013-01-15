package com.bomba.b;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.bomba.R;
import com.bomba.database.DbHelper;
import com.slidingmenu.lib.app.SlidingActivity;

public class AddPlaylist extends Activity implements OnClickListener {
	static String TAG = "AddPL";
	Button Create, Cancel;
	TextView tvPName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_list);
		
		init();

	}

	private void init() {
		Create = (Button) findViewById(R.id.btCreate);
		Cancel = (Button) findViewById(R.id.btCancel);
		Create.setOnClickListener(this);
		Cancel.setOnClickListener(this);
		tvPName = (TextView) findViewById(R.id.etTitle);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btCancel:
			finish();
			break;
		case R.id.btCreate:
			Log.v(TAG, "database insert starting now");
			String Pname = null;
			if (tvPName.getText().toString() != null) {
				boolean didItwork = true;
				try {
					Pname = tvPName.getText().toString();
					// add the new playlist to the local database
					DbHelper playlistEntry = new DbHelper(AddPlaylist.this);
					// open the database
					playlistEntry.open();
					playlistEntry.CreatePlayList(Pname);
					// close the database after the transaction
					playlistEntry.close();
				} catch (Exception e) {
					didItwork = false;
					Toast.makeText(AddPlaylist.this,
							"Error " + e.toString() + " occured",
							Toast.LENGTH_LONG).show();

				} finally {
					if (didItwork) {
						Toast.makeText(AddPlaylist.this,
								Pname + " added to playlists",
								Toast.LENGTH_SHORT).show();
						startActivity(new Intent(AddPlaylist.this,
								MainActivity.class));

					}

				}

			} else {
				Toast.makeText(AddPlaylist.this,
						"Please Enter a title for the Playlist",
						Toast.LENGTH_LONG).show();
			}
			break;
		}

	}
}
