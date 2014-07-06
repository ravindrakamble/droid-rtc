package com.droidrtc.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.fragments.ChannelsFragment;
import com.droidrtc.fragments.ChatFragment;
import com.droidrtc.fragments.ContactsFragment;
import com.droidrtc.fragments.SettingsFragment;
import com.droidrtc.util.Fonts;

public class MainActivity extends FragmentActivity implements UIUpdator,OnTabChangeListener {
	private static final String CONTATCS_TAB_TAG = "Contacts";
	private static final String CHAT_TAB_TAG 	  = "Chat";
	private static final String CHANNELS_TAB_TAG = "Channels";
	private static final String SETTINGS_TAB_TAG = "Settings";
	private TextView tabText;
	private FragmentTabHost mTabHost;	
	@SuppressWarnings("unused")
	private String TAG = "MainActivity";
	final Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		InitView();
	}

	private void InitView() {
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.setOnTabChangedListener(this);

		mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(CONTATCS_TAB_TAG),
				R.drawable.tab_indicator_gen,"Contacts",R.drawable.contacts_tab_selector),ContactsFragment.class,null);
		mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(CHAT_TAB_TAG),
				R.drawable.tab_indicator_gen,"Chat",R.drawable.chat_tab_selector),ChatFragment.class,null);
		mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(CHANNELS_TAB_TAG),
				R.drawable.tab_indicator_gen,"Channels",R.drawable.channels_tab_selector),ChannelsFragment.class,null);
		mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec(SETTINGS_TAB_TAG),
				R.drawable.tab_indicator_gen,"Settings",R.drawable.settings_tab_selector),SettingsFragment.class,null);

	}

	private TabSpec setIndicator(Context ctx, TabSpec spec,
			int resid, String string, int icon) {
		View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
		v.setBackgroundResource(resid);
		tabText = (TextView)v.findViewById(R.id.txt_tabtxt);
		tabText.setTypeface(Fonts.BOOK_ANTIQUA);
		ImageView img = (ImageView)v.findViewById(R.id.img_tabtxt);
		tabText.setText(string);
		tabText.setTypeface(Fonts.BOOK_ANTIQUA);				
		img.setBackgroundResource(icon);		
		return spec.setIndicator(v);
	}
	@Override
	public void onBackPressed() {
		showCustomAlert();
	}

	@Override
	protected void onResume() {
		super.onResume();	
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
	private void showCustomAlert(){
					final Dialog dialog = new Dialog(this);
					
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.custom_alert);
					TextView alertText = (TextView)dialog.findViewById(R.id.alertTextID);
					alertText.setText("Do you want to exit?");
					alertText.setTypeface(Fonts.BOOK_ANTIQUA);
					Button okButton = (Button) dialog.findViewById(R.id.okBtnID);
					okButton.setTypeface(Fonts.BOOK_ANTIQUA,Typeface.BOLD);
					Button cancelButton = (Button)dialog.findViewById(R.id.cancelBtnID);
					cancelButton.setTypeface(Fonts.BOOK_ANTIQUA,Typeface.BOLD);
					okButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent returnIntent = new Intent();
							setResult(RESULT_OK,returnIntent);
							finish();
						}
					});
					cancelButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.cancel();
						}
					});
		 
					dialog.show();
			
	}
	@Override
	public void onTabChanged(String tabId) {
		int index = mTabHost.getCurrentTab();
		for(int tabIndex  = 0; tabIndex  < mTabHost.getTabWidget().getTabCount(); tabIndex ++) {
			if(index == tabIndex ){
				TextView tv = (TextView) mTabHost.getTabWidget().getChildTabViewAt(tabIndex ).findViewById(R.id.txt_tabtxt);				
		        tv.setTextColor(-16777216);
			}
			else {
				TextView tv = (TextView) mTabHost.getTabWidget().getChildTabViewAt(tabIndex ).findViewById(R.id.txt_tabtxt);			
		        tv.setTextColor(-1);
			}
		}
	}
	@Override
	public boolean isFinishing() {
		super.isFinishing();
			if(dialog != null){
				dialog.dismiss();
			}
			return false;
	}
}
