package com.droidrtc.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;

import com.droidrtc.activity.UIUpdator;
import com.droidrtc.data.ContactData;
import com.droidrtc.util.Constants;

public class ConnectionManager {

	XMPPConnection connection;
	private static ConnectionManager connectionManager = new ConnectionManager();
	String userName;
	String password;
	UIUpdator uiUpdator;
	private final ExecutorService pool;
	private ArrayList<ContactData> contactList;

	private ConnectionManager(){
		pool  = Executors.newFixedThreadPool(1);
	}
	public static ConnectionManager getInstance(){
		if (connectionManager == null)
		{
			connectionManager = new ConnectionManager();
		}
		return connectionManager;

	}

	public void connect(String userName,String password, UIUpdator uiUpdator){
		this.userName = userName;
		this.password = password;
		this.uiUpdator = uiUpdator;
		ConnectionTask connectionTask = new ConnectionTask();
		pool.execute(connectionTask);
	}

	public void getContacts(UIUpdator uiUpdator){
		this.uiUpdator = uiUpdator;
		ContactsTask contactsTask = new ContactsTask();
		pool.execute(contactsTask);
	}

	private class ConnectionTask implements Runnable{
		@Override
		public void run() {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.SERVER_HOST,(Constants.SERVER_PORT));
			connection = new XMPPConnection(connConfig);
			try {
				connection.connect();
				Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
			} catch (XMPPException ex) {
				uiUpdator.updateUI(1, false);
				Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
			}
			try {
				Log.e("XMPPClient", "userName:" + userName+"Password:"+password);
				connection.login(userName, password);
				Log.i("XMPPClient", "Logged in as " + connection.getUser());
				Constants.xmppConnection = connection;
				// Set the status to available
				Presence presence = new Presence(Presence.Type.available);
				connection.sendPacket(presence);
				uiUpdator.updateUI(1, true);
			} catch (XMPPException ex) {
				Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + userName);
				uiUpdator.updateUI(2, false);
			}
		}
	}

	private class ContactsTask implements Runnable{
		@Override
		public void run() {
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
			uiUpdator.updateUI(3, contactList);
		}

	}
}
