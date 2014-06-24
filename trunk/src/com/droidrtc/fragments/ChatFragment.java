package com.droidrtc.fragments;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.droidrtc.R;
import com.droidrtc.adapters.ChatAdapter;
import com.droidrtc.data.OneComment;
import com.droidrtc.util.Constants;

public class ChatFragment extends Fragment {
	private ChatAdapter adapter;
	private ListView lv;
	private EditText editText1;
	public XMPPConnection connection = Constants.xmppConnection;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.chat, container, false);
		lv = (ListView) rootView.findViewById(R.id.listView1);

		adapter = new ChatAdapter(getActivity(), R.layout.listitem_discuss);
		lv.setAdapter(adapter);

		editText1 = (EditText) rootView.findViewById(R.id.editText1);
		editText1.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					adapter.add(new OneComment(false, editText1.getText().toString()));
					editText1.setText("");
					return true;
				}
				return false;
			}
		});

		//addItems();

		return rootView;
	}
	private void addItems() {
			String msg = "Hello chat";
			adapter.add(new OneComment(true, msg));
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
