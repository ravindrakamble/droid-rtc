package com.droidrtc.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.droidrtc.R;
import com.droidrtc.activity.ChatActivity;
import com.droidrtc.activity.UIUpdator;
import com.droidrtc.adapters.ContactAdapter;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.data.ContactData;
import com.droidrtc.util.RtcLogs;

public class ChatFragment extends Fragment implements OnItemClickListener,UIUpdator {
	private String TAG = "ChatFragment";
	private ListView contactListView;
	private ArrayAdapter<ContactData> contactAdapter;
	private ArrayList<ContactData> contactList;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contacts_for_chat, container, false);
		contactListView = (ListView)rootView.findViewById(R.id.list);
		contactListView.setOnItemClickListener(this);
		ConnectionManager.getInstance().getContacts(this);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		ContactData contactData = (ContactData) contactListView.getItemAtPosition(position);
		String name = contactData.getName();		
		String presence = contactData.getPresence().toString();
		Intent intent = new Intent(getActivity(),ChatActivity.class);
		intent.putExtra("Name", name);
		intent.putExtra("Presence", presence);
		startActivity(intent);

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
			contactAdapter = new ContactAdapter(getActivity(), R.layout.contact_row_for_chat, contactList);
			contactListView.setAdapter(contactAdapter);
		}
	};
	@Override
	public void updateUI(int reqCode, String sender, String message) {
		RtcLogs.e(TAG, "Sender:"+sender+" Message:"+message);
		Intent intent = new Intent(getActivity(),ChatActivity.class);
		intent.putExtra("Name", sender);
		intent.putExtra("Message", message);
		startActivity(intent);
		
	}

	@Override
	public void updateUI(String message) {
		// TODO Auto-generated method stub
		
	}
}