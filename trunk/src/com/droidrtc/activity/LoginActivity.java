package com.droidrtc.activity;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.droidrtc.R;
import com.droidrtc.util.Constants;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText userEditText,pwdEditText;
	private Button login;
	private String userName,password;
	private ProgressDialog dialog;
	private boolean loginSuccess = false;
	private int LOGIN_REQUEST_CODE = 123;
//	public XMPPConnection connection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		userEditText = (EditText) findViewById(R.id.userEditID);
		pwdEditText = (EditText) findViewById(R.id.pwdEditID);
		login = (Button) findViewById(R.id.loginBtnID);
		login.setOnClickListener(this);

//		SmackAndroid.init(this);
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
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				dialog = new ProgressDialog(LoginActivity.this);
				dialog.setTitle("Signing in...");
				dialog.setMessage("Please wait...");
				dialog.setIndeterminate(false);
				dialog.setCancelable(true);
				dialog.show();
				//							dialog = ProgressDialog.show(LoginActivity.this,"Signing in...", "Please wait...", true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {

			Log.i("ConnectToXmpp", "Connecting to server " + Constants.SERVER_HOST);
			Log.e("XMPPClient", "userName:" + userName+"Password:"+password);
			// Create a connection
			XMPPConnection connection;
			ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.SERVER_HOST,(Constants.SERVER_PORT));
			connection = new XMPPConnection(connConfig);
			

			try {
				connection.connect();
				Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
			} catch (XMPPException ex) {

				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}

				showAlert("Failed to connect");
				Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
			}
			try {
				Log.e("XMPPClient", "userName:" + userName+"Password:"+password);
				connection.login(userName, password);
				Log.i("XMPPClient", "Logged in as " + connection.getUser());
				loginSuccess = true;
				Constants.xmppConnection = connection;
				// Set the status to available
				Presence presence = new Presence(Presence.Type.available);
				connection.sendPacket(presence);
			} catch (XMPPException ex) {
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				showAlert("Login failed");
				Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + userName);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
			if(loginSuccess){
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivityForResult(intent, LOGIN_REQUEST_CODE);
			}
			
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			finish();
		}
		
	}
	private void showAlert(final String msg){
		loginSuccess = false;
		runOnUiThread(new Runnable(){
			@SuppressWarnings("deprecation")
			public void run() {
				AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
				alertDialog.setTitle("Alert!!!");
				alertDialog.setMessage(msg);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alertDialog.show();
			}
		});

	}
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			showAlert(msg.toString());
		}
	};  
}