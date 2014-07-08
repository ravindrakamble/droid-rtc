package com.droidrtc.activity;

import org.jivesoftware.smack.SmackAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.connection.ConnectionDetector;
import com.droidrtc.connection.ConnectionManager;

import com.droidrtc.database.DatabaseHelper;
import com.droidrtc.util.Constants;

import com.droidrtc.util.Fonts;
import com.droidrtc.util.ProgressWheel;

public class LoginActivity extends Activity implements OnClickListener,UIUpdator {
	private EditText userEditText,pwdEditText;
	private Button login;
	private String userName,password;
	private ProgressDialog dialog;
	private boolean loginSuccess = false;
	private int LOGIN_REQUEST_CODE = 123;
	private ConnectionDetector connectionDetector;
	private ProgressWheel pw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		pw = (ProgressWheel) findViewById(R.id.progressWheel);
		pw.setVisibility(View.GONE);
		userEditText = (EditText) findViewById(R.id.userEditID);
		pwdEditText = (EditText) findViewById(R.id.pwdEditID);
		login = (Button) findViewById(R.id.loginBtnID);
		userEditText.setTypeface(Fonts.THROW_HANDS);
		pwdEditText.setTypeface(Fonts.THROW_HANDS);
		login.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		login.setOnClickListener(this);
		connectionDetector = new ConnectionDetector(getApplicationContext());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtnID:
			userName = userEditText.getText().toString();
			password = pwdEditText.getText().toString();

			if(connectionDetector.isConnectedToInternet()){
				/*dialog = new ProgressDialog(LoginActivity.this);
				dialog.setTitle("Signing in...");
				dialog.setMessage("Please wait...");
				dialog.setIndeterminate(false);
				dialog.setCancelable(true);
				dialog.show();*/
				if(!pw.isShown()){
					pw.setVisibility(View.VISIBLE);
				}
				SmackAndroid.init(this);
				userEditText.setAlpha(.2f);
				pwdEditText.setAlpha(.2f);
				login.setAlpha(.2f);
				pw.spin();
				pw.setText("Logging in...");
				ConnectionManager.getInstance().connect(userName, password, this);
			}else{
				showAlert("Connection Error!!! Please try again");
			}

			break;

		default:
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			finish();
		}

	}
	private void showAlert(final String msg){
		loginSuccess = false;

		AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
		alertDialog.setTitle("Alert!!!");
		alertDialog.setMessage(msg);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if( pw != null){
			pw.stopSpinning();
			pw.setVisibility(View.GONE);
		}

		userEditText.setAlpha(1f);
		pwdEditText.setAlpha(1f);
		login.setAlpha(1f);
	}

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.LOGIN_REQ_SUCCESS:
				openMainActivity();
				break;
			case Constants.LOGIN_REQ_FAILURE:
				showCustomAlert("Wrong Username or Password");
				break;
			case Constants.CONN_REQ_FAILURE:
				showCustomAlert("Login failed");
				break;
			default:
				break;
			}
		}
	};
	private void openMainActivity(){

		DatabaseHelper chatDB = new DatabaseHelper(getApplicationContext());
		chatDB.cacheChatHistory();

		Intent intent = new Intent(LoginActivity.this,MainActivity.class);
		startActivityForResult(intent, LOGIN_REQUEST_CODE);
	}

	@Override
	public void updateUI(int reqCode, Object response) {

	}

	@Override
	public boolean isFinishing() {
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		return super.isFinishing();
	}
	@Override
	public void updateUI(int reqCode, String sender, String message) {
		// TODO Auto-generated method stub

	}
	@Override
	public void updateUI(String message) {
		// TODO Auto-generated method stub

	}
	@Override
	public void updateUI(int reqCode) {
		Message msg = Message.obtain();
		msg.what = reqCode;
		handler.sendMessage(msg);


	}
	private void showCustomAlert(String message){
		final Dialog dialog = new Dialog(this);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);
		TextView alertText = (TextView)dialog.findViewById(R.id.alertTextID);
		alertText.setText(message);
		alertText.setTypeface(Fonts.THROW_HANDS);
		Button okButton = (Button) dialog.findViewById(R.id.okBtnID);
		okButton.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		Button cancelButton = (Button)dialog.findViewById(R.id.cancelBtnID);
		cancelButton.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();

}
}