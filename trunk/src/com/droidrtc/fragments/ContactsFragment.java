package com.droidrtc.fragments;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.droidrtc.R;
import com.droidrtc.activity.UIUpdator;
import com.droidrtc.adapters.ContactAdapter;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.data.ContactData;
import com.droidrtc.util.Constants;
import com.droidrtc.util.Fonts;
import com.droidrtc.util.ProgressWheel;

public class ContactsFragment extends Fragment implements OnItemClickListener,UIUpdator, OnClickListener{
	private ListView contactListView;
	XMPPConnection connection;
	private ArrayAdapter<ContactData> contactAdapter;
	private ArrayList<ContactData> contactList;
	private ImageView addContact;
	private ProgressWheel pw;
	private TextView loggedInUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contacts, container, false);
		contactListView = (ListView)rootView.findViewById(R.id.list);
		addContact = (ImageView)rootView.findViewById(R.id.addContactID);
		loggedInUser =(TextView)rootView.findViewById(R.id.nameID);
		loggedInUser.setText(Constants.LOGGED_IN_USER);
		loggedInUser.setTypeface(Fonts.THROW_HANDS_BOLD);
		pw = (ProgressWheel) rootView.findViewById(R.id.progressWheel);
		pw.setVisibility(View.GONE);
		addContact.setOnClickListener(this);
		getContacts();
		
		return rootView;
	}
	private void getContacts(){
		ConnectionManager.getInstance().getContacts(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateUI(int reqCode, Object response) {
		if(response instanceof Object){
			contactList = (ArrayList<ContactData>)response;
			handler.sendEmptyMessage(0);
		}
	}
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			contactAdapter = new ContactAdapter(getActivity(), R.layout.contact_row, contactList);
			contactListView.setAdapter(contactAdapter);
		}
	};
	protected Handler contactHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.ADD_CONTACT_SUCCESS:
				stopSpinningPW();
				getContacts();
				
				break;
			case Constants.ADD_CONTACT_FAILURE:
				showCustomAlert("Adding contact failed");
				stopSpinningPW();
				break;
			case Constants.CONTACT_EXISTS:
				showCustomAlert("Contact already exists");
				stopSpinningPW();
				break;

			default:
				break;
			}
			
		}
	};
	private void stopSpinningPW(){
		if(pw != null && pw.isSpinning()){
			pw.setVisibility(View.GONE);
			pw.stopSpinning();
			contactListView.setAlpha(1f);
		}
	}
	@Override
	public void updateUI(int reqCode, String sender, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUI(String message) {
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addContactID:
			
			showAddContactDialog();
			break;

		default:
			break;
		}

	}	
	private void showAddContactDialog() {
		final Dialog dialog = new Dialog(getActivity());

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.add_contact);
		TextView alertText = (TextView)dialog.findViewById(R.id.alertTextID);
		alertText.setText("Enter the username");
		alertText.setTypeface(Fonts.THROW_HANDS);
		final EditText editText = (EditText)dialog.findViewById(R.id.contactEditTextID);
		editText.setTypeface(Fonts.THROW_HANDS);
		Button okButton = (Button) dialog.findViewById(R.id.okBtnID);
		okButton.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		Button cancelButton = (Button)dialog.findViewById(R.id.cancelBtnID);
		cancelButton.setTypeface(Fonts.THROW_HANDS,Typeface.BOLD);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				if(!pw.isShown()){
					pw.setVisibility(View.VISIBLE);
				}
				contactListView.setAlpha(.2f);
				pw.spin();
				pw.setText("Adding Contact...");
				ConnectionManager.getInstance().checkUserPresent(editText.getText().toString());
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

	@Override
	public void updateUI(int reqCode) {
		Message msg = Message.obtain();
		msg.what = reqCode;
		contactHandler.sendMessage(msg);
		
	}
	
	private void showCustomAlert(String message){
		final Dialog dialog = new Dialog(getActivity());
		
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
				dialog.cancel();
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
