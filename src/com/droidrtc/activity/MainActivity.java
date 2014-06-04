package com.droidrtc.activity;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import com.droidrtc.R;
import com.droidrtc.adapters.TabsPagerAdapter;
import com.droidrtc.util.Constants;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Contacts", "Chat", "Channels" };

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		/*SmackAndroid.init(this);
		new ConnectToXmpp().execute();*/
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		//		actionBar.setTitle("Contacts");
		return true;
	}

	/*private class ConnectToXmpp extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("ConnectToXmpp", "Connecting to server " + Constants.SERVER_HOST);
			 // Create a connection
	        ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.SERVER_HOST,(Constants.SERVER_PORT));
	        XMPPConnection connection = new XMPPConnection(connConfig);
	 
	        try {
	            connection.connect();
	            Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
	        } catch (XMPPException ex) {
	            Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
	        }
	        try {
	            connection.login(Constants.USERNAME, Constants.PASSWORD);
	            Log.i("XMPPClient", "Logged in as " + connection.getUser());
	 
	            // Set the status to available
	            Presence presence = new Presence(Presence.Type.available);
	            connection.sendPacket(presence);
	        } catch (XMPPException ex) {
	            Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + Constants.USERNAME);
	        }
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

	}*/
}
