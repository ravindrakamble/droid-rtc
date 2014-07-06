package com.droidrtc.fragments;

import com.droidrtc.R;
import com.droidrtc.util.Fonts;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.text.TextUtilsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class ChannelsFragment extends Fragment {
	private TextView channelView = null;  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.channels, container, false);
		channelView = (TextView)rootView.findViewById(R.id.channelViewID);
		channelView.setTypeface(Fonts.THROW_HANDS, Typeface.BOLD);
		return rootView;
	}

}
