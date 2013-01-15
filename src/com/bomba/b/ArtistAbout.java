package com.bomba.b;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

import com.actionbarsherlock.view.MenuItem;
import com.bomba.R;
import com.slidingmenu.lib.app.SlidingActivity;

public class ArtistAbout extends SlidingActivity implements TabListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist_about);
		setBehindContentView(R.layout.slide);

		// set up the tabs for this activity
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab tabP = getSupportActionBar().newTab();
		tabP.setText("About");
		tabP.setTabListener(this);
		getSupportActionBar().addTab(tabP);

		ActionBar.Tab tabart = getSupportActionBar().newTab();
		tabart.setText("Discography");
		tabart.setTabListener(this);
		getSupportActionBar().addTab(tabart);

		ActionBar.Tab tabalbs = getSupportActionBar().newTab();
		tabalbs.setText("Related artists");
		tabalbs.setTabListener(this);
		getSupportActionBar().addTab(tabalbs);

		

		getSupportActionBar().setStackedBackgroundDrawable(
				getResources().getDrawable(R.drawable.tab_bar_background));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
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

}
