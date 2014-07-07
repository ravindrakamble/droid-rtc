package com.droidrtc.fragments;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.activity.UIUpdator;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.util.Fonts;

public class SettingsFragment extends Fragment implements OnClickListener,UIUpdator {
	private RelativeLayout logout;
	TextView logoutTextView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.settings, container, false);
		logout = (RelativeLayout)rootView.findViewById(R.id.logoutLayoutID);
		logoutTextView = (TextView)rootView.findViewById(R.id.logoutID);
		logoutTextView.setTypeface(Fonts.THROW_HANDS_BOLD);
		logout.setOnClickListener(this);
		return rootView;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutLayoutID:
			showLogoutAlert();
			break;

		default:
			break;
		}		
	}
	public void showLogoutAlert(){
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);
		TextView alertText = (TextView)dialog.findViewById(R.id.alertTextID);
		alertText.setText("Do you want to logout?");
		alertText.setTypeface(Fonts.THROW_HANDS);
		Button okButton = (Button) dialog.findViewById(R.id.okBtnID);
		okButton.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		Button cancelButton = (Button)dialog.findViewById(R.id.cancelBtnID);
		cancelButton.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logout();
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
	private void logout(){
		ConnectionManager.getInstance().logout(this);
	}
	@Override
	public void updateUI(int reqCode, Object response) {
		if(response instanceof Boolean){
			getActivity().finish();
		}

	}
	@Override
	public void updateUI(int reqCode, String sender, String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateUI(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateUI(int reqCode) {
		getActivity().finish();
		
	}

}