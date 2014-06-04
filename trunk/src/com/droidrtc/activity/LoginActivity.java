package com.droidrtc.activity;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import com.droidrtc.R;
import com.droidrtc.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText userEditText,pwdEditText;
	private Button login;
	private String userName,password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
         userEditText = (EditText) findViewById(R.id.userEditID);
         pwdEditText = (EditText) findViewById(R.id.pwdEditID);
         login = (Button) findViewById(R.id.loginBtnID);
         login.setOnClickListener(this);
         
         SmackAndroid.init(this);
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtnID:
			userName = userEditText.getText().toString();
			password = pwdEditText.getText().toString();
			new ConnectToXmpp().execute();
			break;

		default:
			break;
		}
		
	}
	private class ConnectToXmpp extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("ConnectToXmpp", "Connecting to server " + Constants.SERVER_HOST);
			Log.e("XMPPClient", "userName:" + userName+"Password:"+password);
			 // Create a connection
	        ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.SERVER_HOST,(Constants.SERVER_PORT));
	        XMPPConnection connection = new XMPPConnection(connConfig);
	 
	        try {
	            connection.connect();
	            Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
	        } catch (XMPPException ex) {
	            Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
	        }
	        try {
	        	Log.e("XMPPClient", "userName:" + userName+"Password:"+password);
	            connection.login(userName, password);
	            Log.i("XMPPClient", "Logged in as " + connection.getUser());
	 
	            // Set the status to available
	            Presence presence = new Presence(Presence.Type.available);
	            connection.sendPacket(presence);
	        } catch (XMPPException ex) {
	            Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + userName);
	        }
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
		}

	}
}