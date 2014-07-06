package com.droidrtc.activity;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.droidrtc.R;
import com.droidrtc.adapters.ChatAdapter;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.data.ChatData;
import com.droidrtc.data.OneComment;
import com.droidrtc.database.DatabaseHelper;
import com.droidrtc.util.RtcLogs;

public class ChatActivity extends Activity implements UIUpdator{
	private String TAG = "ChatActivity";
	private ChatAdapter adapter;
	private ListView lv;
	public ArrayList<ChatData> chatlist;
	private String recipient,message;
	private EditText mSendText;
	private String text;		
	public DatabaseHelper chatDB;
	public Date date;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);

		Intent intent = getIntent();
		recipient = intent.getStringExtra("Name");
		chatDB = new DatabaseHelper(this);
		date = new Date();
		adapter = new ChatAdapter(this, R.layout.listitem_discuss);
		
		lv = (ListView) findViewById(R.id.chatListId);
		mSendText = (EditText) findViewById(R.id.sendTextId);
		mSendText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					adapter.add(new OneComment(false, null, mSendText.getText().toString()));	
					
					text = mSendText.getText().toString();
					mSendText.setText("");
					Toast.makeText(ChatActivity.this, recipient, Toast.LENGTH_SHORT).show();
					RtcLogs.i(TAG, "Sending text [" + text + "] to [" + recipient + "]");
					ChatData tmpChat = new ChatData();
					tmpChat.setDirection(0);
					String tmpTo = recipient.split("\\@")[0];
					tmpChat.setContactName(tmpTo);
					tmpChat.setMessage(text);
					tmpChat.setDate(date.getTime());
					chatDB.insertChat(tmpChat);
					ConnectionManager.getInstance().sendMsg(recipient, text,ChatActivity.this);
					RtcLogs.e(TAG, "************************");
					RtcLogs.e(TAG, "sent [" + text + "] to [" + recipient + "]");
					RtcLogs.e(TAG, "************************");
					return true;
				}
				return false;
			}
		});
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			chatDB.exportDB();
			String tmpTo = recipient.split("\\@")[0];			
			chatlist = chatDB.getChatHistory(tmpTo);
			if(chatlist != null){
				for(int index = 0; index < chatlist.size(); index++ ){
					ChatData tmpchat = chatlist.get(index);
					message = tmpchat.getMessage();
					RtcLogs.e(TAG, "************************");
					RtcLogs.e(TAG, "read [" + message + "] with [" + recipient + "] direction [" + tmpchat.getDirection() + "]");
					RtcLogs.e(TAG, "************************");
					if(message != null) {
						if(tmpchat.getDirection() > 0){
							adapter.add(new OneComment(true, message, null));	
						}else{
							adapter.add(new OneComment(false, null, message));	
						}
					}
				}
			}
			lv.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		    String message = intent.getStringExtra("MESSAGE");
		    String from = intent.getStringExtra("FROM");
		    RtcLogs.d("receiver", "Got message: " + message+";From:"+from);
		    adapter.add(new OneComment(true, message, null));
			adapter.notifyDataSetChanged();
			ChatData tmpChat = new ChatData();
			tmpChat.setDirection(1);
			String tmpTo = from.split("\\@")[0];
			tmpChat.setContactName(tmpTo);			
			tmpChat.setMessage(message);
			tmpChat.setDate(date.getTime());
			chatDB.insertChat(tmpChat);
			RtcLogs.e(TAG, "************************");
			RtcLogs.e(TAG, "recvd [" + message + "] from [" + from + "]");
			RtcLogs.e(TAG, "************************");
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

	}
	public class MyMessageReceiver extends BroadcastReceiver {

		@Override
		  public void onReceive(Context context, Intent intent) {
		    @SuppressWarnings("unused")
			Bundle extras = intent.getExtras();
		    RtcLogs.i(TAG, message);
		  }
		} 

}