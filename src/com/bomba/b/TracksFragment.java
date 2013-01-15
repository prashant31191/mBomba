package com.bomba.b;

import java.util.ArrayList;
import java.util.HashMap;

import com.bomba.R;
import com.bomba.database.DbHelper;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TracksFragment extends ListFragment {
	ListView tracks;
	DbHelper datapull;
	String q;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		q = getArguments().getString("value");
		
		return inflater.inflate(R.layout.trackslistfragment, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		tracks = getListView();
		datapull = new DbHelper(getActivity());
		datapull.open();
		populateSongs p = new populateSongs();
		p.execute(q);
		
		Log.v("TRACKSFRAGMENT", q);
		
		super.onActivityCreated(savedInstanceState);

	}
	
	public class loadList extends AsyncTask<String, Void, Void> {
		ArrayList<HashMap<String, String>> what;

		@Override
		protected Void doInBackground(String... st) {
			datapull = new DbHelper(getActivity());
			datapull.open();
			what = datapull.getTracks(st[0]);

			datapull.close();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (what != null) {
				SimpleAdapter adp = new SimpleAdapter(getActivity(), what,
						android.R.layout.simple_list_item_2, new String[] {
								"name", "per" }, new int[] {
								android.R.id.text1, android.R.id.text2 });
				tracks.setAdapter(adp);

			} else {
				Toast.makeText(getActivity(), "Search tracks to add to list",
						Toast.LENGTH_LONG).show();
				Log.v("async", "the arraylist is empty");
			}
		}

	}

	public class populateSongs extends AsyncTask<String, Void, Void> {
		Cursor mCursor;

		@Override
		protected void onPostExecute(Void result) {
			if (mCursor.getCount() <= 0) {
				Toast.makeText(
						getActivity(),
						"We don't have any tracks with that name but we will look around",
						Toast.LENGTH_LONG).show();
			} else {
				ListAdapter adp = new SimpleCursorAdapter(getActivity(),
						R.layout.searchrow, mCursor, new String[] {
								datapull.SONGS_ARTIST_ID,
								datapull.SONGS_NAME }, new int[] {
								R.id.tvs_a_id, R.id.tv_songs_name });
				tracks.setAdapter(adp);

			}

			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {
			
			//datapull.open();
			mCursor = datapull.getSearched(params[0]);
			Log.v("SEARCHY", mCursor.getCount() + "");
			//startManagingCursor(mCursor);
			
			return null;
		}
	}

}
