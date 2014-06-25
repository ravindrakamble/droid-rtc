package com.droidrtc.fragments;

import java.util.ArrayList;
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
import com.droidrtc.activity.UIUpdator;
import com.droidrtc.adapters.ContactAdapter;
import com.droidrtc.connection.ConnectionManager;
import com.droidrtc.data.ContactData;

public class ContactsFragment extends Fragment implements OnItemClickListener,UIUpdator{
	private ListView contactListView;
	private ArrayAdapter<ContactData> contactAdapter;
	private ArrayList<ContactData> contactList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contacts, container, false);
		contactListView = (ListView)rootView.findViewById(R.id.list);

		ConnectionManager.getInstance().getContacts(this);
		return rootView;
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
}
