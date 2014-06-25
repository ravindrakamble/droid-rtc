package com.droidrtc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.fragments.ChannelsFragment;
import com.droidrtc.fragments.ChatFragment;
import com.droidrtc.fragments.ContactsFragment;
import com.droidrtc.fragments.SettingsFragment;
import com.droidrtc.util.Constants;

public class MainActivity extends FragmentActivity implements UIUpdator {
	 private static final String CONTATCS_TAB_TAG = "Contacts";
	 private static final String CHAT_TAB_TAG 	  = "Chat";
	 private static final String CHANNELS_TAB_TAG = "Channels";
	 private static final String SETTINGS_TAB_TAG = "Settings";
	 
	 private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitView();
	}
	
	 private void InitView() {
		  mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


		        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(CONTATCS_TAB_TAG),
		                R.drawable.tab_indicator_gen,"Contacts",R.drawable.contacts),ContactsFragment.class,null);
		        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(CHAT_TAB_TAG),
		                  R.drawable.tab_indicator_gen,"Chat",R.drawable.chat),ChatFragment.class,null);
		        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(CHANNELS_TAB_TAG),
		                  R.drawable.tab_indicator_gen,"Channels",R.drawable.channels),ChannelsFragment.class,null);
		        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(SETTINGS_TAB_TAG),
		                  R.drawable.tab_indicator_gen,"Settings",R.drawable.settings),SettingsFragment.class,null);
		      
		 }

	 private TabSpec setIndicator(Context ctx, TabSpec spec,
			   int resid, String string, int genresIcon) {
			      View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
			      v.setBackgroundResource(resid);
			      TextView tv = (TextView)v.findViewById(R.id.txt_tabtxt);
			      ImageView img = (ImageView)v.findViewById(R.id.img_tabtxt);
			     
			      tv.setText(string);
			      img.setBackgroundResource(genresIcon);
			      return spec.setIndicator(v);
			 }
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Do you want to exit?");
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				ConnectionManager.getInstance().logout(MainActivity.this);
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Constants.inChatActivity = false;
	}
	@Override
	 protected void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	 
	 }
	@Override
	public void updateUI(int reqCode, Object response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUI(int reqCode, String sender, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUI(String message) {
		// TODO Auto-generated method stub
		
	}
}
