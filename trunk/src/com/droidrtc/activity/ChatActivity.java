package com.droidrtc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.droidrtc.R;
import com.droidrtc.adapters.ChatAdapter;
import com.droidrtc.adapters.ContactAdapter;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.data.OneComment;
import com.droidrtc.fragments.ChatFragment;
import com.droidrtc.util.Constants;
import com.droidrtc.util.RtcLogs;

public class ChatActivity extends Activity implements UIUpdator{
	private String TAG = "ChatActivity";
	private ChatAdapter adapter;
	private ListView lv;
	private String recipient,message;
	private EditText mSendText;
	private String text;
	private static ChatActivity chatActivity = new ChatActivity();
	public static ChatActivity getInstance(){
		if (chatActivity == null)
		{
			chatActivity = new ChatActivity();
		}
		return chatActivity;

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
					mSendText.setText("");
					text = mSendText.getText().toString();
					Toast.makeText(ChatActivity.this, recipient, Toast.LENGTH_SHORT).show();
					RtcLogs.i("XMPPClient", "Sending text [" + text + "] to [" + recipient + "]");
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
	}
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
	
}