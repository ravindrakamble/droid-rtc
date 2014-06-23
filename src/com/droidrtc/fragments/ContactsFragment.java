package com.droidrtc.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.droidrtc.R;
import com.droidrtc.adapters.ContactAdapter;
import com.droidrtc.data.ContactData;
import com.droidrtc.util.Constants;

public class ContactsFragment extends Fragment implements OnItemClickListener{
	private ListView contactListView;
	public XMPPConnection connection = Constants.xmppConnection;
	private ArrayAdapter<ContactData> contactAdapter;
	private ArrayList<ContactData> contactList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contacts, container, false);
		contactListView = (ListView)rootView.findViewById(R.id.list);
		
		getContacts();
		return rootView;
	}


	public void getContacts(){
		Roster roster = connection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		ContactData contacts = new ContactData();
		contactList = new ArrayList<ContactData>();
		for (RosterEntry entry : entries) {

			Log.d("XMPPChatDemoActivity",  "--------------------------------------");
			Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
			Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
			Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
			Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
			Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
			Presence entryPresence = roster.getPresence(entry.getUser());

			Log.d("XMPPChatDemoActivity", "Presence Status: "+ entryPresence.getStatus());
			Log.d("XMPPChatDemoActivity", "Presence Type: " + entryPresence.getType());
			contacts.setName(entry.getName());
			contacts.setPresence(entryPresence.getType());
			contactList.add(contacts);
			
		}
		contactAdapter = new ContactAdapter(getActivity(), R.layout.contact_row, contactList);
		contactListView.setAdapter(contactAdapter);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
