package com.droidrtc.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.util.Fonts;
import com.droidrtc.util.RtcLogs;

public class ContactInfoActivity extends Activity implements OnClickListener{	
	TextView voiceCalltv, videoCalltv, chattv, deletetv, nametv, presencetv;
	String TAG = "ContactInfoActivity";
	LinearLayout audioCallBtn,videoCallBtn,chatBtn,deleteBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_info);
		voiceCalltv = (TextView)findViewById(R.id.voiceCallTextID);
		voiceCalltv.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
		videoCalltv = (TextView)findViewById(R.id.videoCallTextID);
		videoCalltv.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
		chattv = (TextView)findViewById(R.id.chatTextID);
		chattv.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
		deletetv = (TextView)findViewById(R.id.deleteTextID);
		deletetv.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
		Intent intent = getIntent();
		String userName,presence; 
		userName = intent.getStringExtra("Name");
		presence = intent.getStringExtra("Presence");
		RtcLogs.e(TAG, "userName :"+userName+"Presence:"+presence);
		nametv = (TextView)findViewById(R.id.nameID);
		presencetv = (TextView)findViewById(R.id.presenceID);
		nametv.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
		presencetv.setTypeface(Fonts.BOOK_ANTIQUA, Typeface.BOLD);
		nametv.setText(userName);
		presencetv.setText(presence);
		audioCallBtn = (LinearLayout)findViewById(R.id.voiceCallLayoutID);
		videoCallBtn = (LinearLayout)findViewById(R.id.videoCallLayoutID);
		chatBtn = (LinearLayout)findViewById(R.id.chatLayoutID);
		deleteBtn = (LinearLayout)findViewById(R.id.deleteLayoutID);
		audioCallBtn.setOnClickListener(this);
		videoCallBtn.setOnClickListener(this);
		chatBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voiceCallLayoutID:

			break;
		case R.id.videoCallLayoutID:

			break;
		case R.id.chatLayoutID:

			break;
		case R.id.deleteLayoutID:

			break;

		default:
			break;
		}

	}
}
