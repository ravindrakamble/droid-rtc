package com.droidrtc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.activity.UIUpdator;

public class SettingsFragment extends Fragment implements OnClickListener,UIUpdator {
	private TextView logout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.settings, container, false);
		logout = (TextView)rootView.findViewById(R.id.logoutID);
		logout.setOnClickListener(this);
		return rootView;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logoutID:

			break;

		default:
			break;
		}		
	}

	@Override
	public void updateUI(int reqCode, Object response) {
		if(response instanceof Boolean){
			getActivity().finish();
		}

	}

}