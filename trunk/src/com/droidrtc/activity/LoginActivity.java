package com.droidrtc.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.droidrtc.R;
import com.droidrtc.connection.ConnectionDetector;
import com.droidrtc.connection.ConnectionManager;
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
		userEditText = (EditText) findViewById(R.id.userEditID);
		pwdEditText = (EditText) findViewById(R.id.pwdEditID);
		login = (Button) findViewById(R.id.loginBtnID);
		userEditText.setTypeface(Fonts.BOOK_ANTIQUA);
		pwdEditText.setTypeface(Fonts.BOOK_ANTIQUA);
		login.setTypeface(Fonts.BOOK_ANTIQUA,Typeface.BOLD);
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


	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				if(loginSuccess){
					openMainActivity();
				}else{
					showAlert("Login failed");
				}
				break;
			case 2:
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				if(loginSuccess){
					openMainActivity();
				}else{
					showAlert("Wrong Username or Password");
				}
				break;
			}

		}
	};
	private void openMainActivity(){
		if(pw.isShown()){
			pw.setVisibility(View.GONE);
			userEditText.setAlpha(1f);
			pwdEditText.setAlpha(1f);
			login.setAlpha(1f);
		}
		pw.stopSpinning();
		pw.setVisibility(View.GONE);
		Intent intent = new Intent(LoginActivity.this,MainActivity.class);
		startActivityForResult(intent, LOGIN_REQUEST_CODE);
	}
	@Override
	public void updateUI(int reqCode, Object response) {
		if(response instanceof Boolean){
			loginSuccess = (Boolean)response;
		}
		Message msg = Message.obtain();
		msg.what = reqCode;
		handler.sendMessage(msg);

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

}