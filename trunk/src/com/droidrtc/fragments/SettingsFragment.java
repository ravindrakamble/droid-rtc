package com.droidrtc.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
		logoutTextView.setTypeface(Fonts.BOOK_ANTIQUA);
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
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("Do you want to logout?");
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				logout();
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

}