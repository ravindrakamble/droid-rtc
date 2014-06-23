package com.droidrtc.activity;

import org.jivesoftware.smack.XMPPConnection;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import com.droidrtc.R;
import com.droidrtc.adapters.ChatAdapter;
import com.droidrtc.data.OneComment;
import com.droidrtc.util.Constants;

public class ChatActivity extends Activity {
	private ChatAdapter adapter;
	private ListView lv;
	private EditText editText1;
	public XMPPConnection connection = Constants.xmppConnection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		lv = (ListView) findViewById(R.id.listView1);

		adapter = new ChatAdapter(getApplicationContext(), R.layout.listitem_discuss);

		lv.setAdapter(adapter);

		editText1 = (EditText) findViewById(R.id.editText1);
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
	}
}