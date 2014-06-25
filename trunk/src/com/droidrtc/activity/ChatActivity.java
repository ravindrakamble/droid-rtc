package com.droidrtc.activity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.droidrtc.R;
import com.droidrtc.adapters.ChatAdapter;
import com.droidrtc.data.OneComment;
import com.droidrtc.util.RtcLogs;

public class ChatActivity extends Activity {
	private ChatAdapter adapter;
	private ListView lv;
	private String recipient;
	private EditText mSendText;
	private XMPPConnection connection;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		Intent intent = getIntent();
		recipient = intent.getStringExtra("Name");
		lv = (ListView) findViewById(R.id.chatListId);
		adapter = new ChatAdapter(this, R.layout.listitem_discuss);
		lv.setAdapter(adapter);

		mSendText = (EditText) findViewById(R.id.sendTextId);
		mSendText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					adapter.add(new OneComment(false, mSendText.getText().toString()));
					mSendText.setText("");
					String text = mSendText.getText().toString();
					Toast.makeText(ChatActivity.this, recipient, Toast.LENGTH_SHORT).show();
					RtcLogs.i("XMPPClient", "Sending text [" + text + "] to [" + recipient + "]");
					/*Message msg = new Message(to, Message.Type.chat);
					msg.setBody(text);
					connection.sendPacket(msg);*/
					return true;
				}
				return false;
			}
		});
	}
	private void sendMessage(){
		ChatManager chatmanager = connection.getChatManager();
		Chat newChat = chatmanager.createChat("abc@gmail.com", new MessageListener() {
			// Receiving Messages
			public void processMessage(Chat chat, Message message) {
				Message outMsg = new Message(message.getBody());
				/*try {
		      newChat.sendMessage(outMsg);
		    } catch (XMPPException e) {
		      //Error
		    }*/
			}
		});
		try {
			//Send String as Message
			newChat.sendMessage("How are you?");
		} catch (XMPPException e) {
			//Error
		}
	}
}