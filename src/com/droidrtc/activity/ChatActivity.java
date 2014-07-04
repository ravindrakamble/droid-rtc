package com.droidrtc.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.droidrtc.R;
import com.droidrtc.adapters.ChatAdapter;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.data.OneComment;
import com.droidrtc.util.Constants;
import com.droidrtc.util.RtcLogs;

public class ChatActivity extends Activity implements UIUpdator{
	private String TAG = "ChatActivity";
	private ChatAdapter adapter;
	private ListView lv;
	private String recipient,message;
	private EditText mSendText;
	private String text;		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);

		Intent intent = getIntent();
		recipient = intent.getStringExtra("Name");

		try {
			setTitle(recipient);
			message = intent.getStringExtra("Message");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		lv = (ListView) findViewById(R.id.chatListId);
		adapter = new ChatAdapter(this, R.layout.listitem_discuss);
		lv.setAdapter(adapter);
		if(message != null){
			adapter.add(new OneComment(true, message));
		}
		mSendText = (EditText) findViewById(R.id.sendTextId);
		mSendText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					adapter.add(new OneComment(false, mSendText.getText().toString()));					
					text = mSendText.getText().toString();
					mSendText.setText("");
					Toast.makeText(ChatActivity.this, recipient, Toast.LENGTH_SHORT).show();
					RtcLogs.i(TAG, "Sending text [" + text + "] to [" + recipient + "]");
					ConnectionManager.getInstance().sendMsg(recipient, text,ChatActivity.this);
					return true;
				}
				return false;
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		Constants.inChatActivity = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("my-msg"));
	}
	@Override
	protected void onPause() {
	  // Unregister since the activity is not visible
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	  super.onPause();
	} 
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Extract data included in the Intent
		    String message = intent.getStringExtra("MESSAGE");
		    String from = intent.getStringExtra("FROM");
		    RtcLogs.d("receiver", "Got message: " + message+";From:"+from);
		    adapter.add(new OneComment(true, message));
			adapter.notifyDataSetChanged();
//			lv.setAdapter(adapter);
		  }
		};
	@Override
	public void updateUI(int reqCode, Object response) {
		// TODO Auto-generated method stub

	}
	@Override
	public void updateUI(int reqCode, String sender, String message) {

	}
	@Override
	public void updateUI(String chatMsg) {
		message = chatMsg;
		RtcLogs.e(TAG, message+" Adapter"+adapter);
		updateChat();

	}
	private void updateChat() {
		runOnUiThread (new Thread(new Runnable() { 
			public void run() {
				adapter.add(new OneComment(true, message));
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
			}
		}));

	}
	public class MyMessageReceiver extends BroadcastReceiver {

		@Override
		  public void onReceive(Context context, Intent intent) {
		    Bundle extras = intent.getExtras();
		    RtcLogs.i(TAG, message);
		  }
		} 

}