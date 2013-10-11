package com.bomba.b;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.bomba.R;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

public class SideSearch extends SlidingFragmentActivity implements OnQueryTextListener {
	
	ActionBar bar; 
	String queryT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.slide);
		LinearLayout l = new LinearLayout(this);
		setContentView(l);
		bar =  getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayHomeAsUpEnabled(true);
		queryT = null;
		
		//init();

		
		
		
		
	}
	
	private void init() {
		ActionBar.Tab tab1 = bar.newTab();
		ActionBar.Tab tab2 = bar.newTab();
		tab1.setText("Tracks");
		tab2.setText("Artists");
		tab1.setTabListener(new MyTabListener());
		tab2.setTabListener(new MyTabListener());
		bar.addTab(tab1);
		bar.addTab(tab2);
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case 1:
			SearchView sv = (SearchView) item.getActionView();
			sv.setOnQueryTextListener(this);
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.widget.SearchView searchView = new com.actionbarsherlock.widget.SearchView(
				getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search for Track");
		menu.add(0, 1, 1, "playlist")
				.setIcon(R.drawable.ic_action_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		return true;
	}

	private class MyTabListener implements ActionBar.TabListener {
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.getPosition() == 0) {
				Bundle b = new Bundle();
				b.putString("value", queryT);
				TracksFragment frag = new TracksFragment();
				frag.setArguments(b);
				ft.replace(android.R.id.content, frag);
				invalidateOptionsMenu();
			} 
			if(tab.getPosition() == 1){
				ArtistGridFragment frag = new ArtistGridFragment();
				ft.replace(android.R.id.content, frag);
				invalidateOptionsMenu();
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		queryT = query;
		init();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
}
