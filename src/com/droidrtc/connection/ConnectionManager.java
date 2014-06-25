package com.droidrtc.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.widget.TabHost;

import com.droidrtc.activity.ChatActivity;
import com.droidrtc.activity.UIUpdator;
import com.droidrtc.data.ContactData;
import com.droidrtc.fragments.ChatFragment;
import com.droidrtc.util.Constants;
import com.droidrtc.util.RtcLogs;

public class ConnectionManager {
	String TAG = "ConnectionManager";
	XMPPConnection connection;
	private static ConnectionManager connectionManager = new ConnectionManager();
	String userName;
	String password;
	String msg;
	String recipient;
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

	public void logout(UIUpdator uiUpdator){
		this.uiUpdator = uiUpdator;
		LogoutTask logoutTask = new LogoutTask();
		pool.execute(logoutTask);
	}
	public void sendMsg(String recipient,String msg, UIUpdator uiUpdator){
		this.recipient = recipient;
		this.msg = msg;
		this.uiUpdator = uiUpdator;
		SendMsgTask sendMsgTask = new SendMsgTask();
		pool.execute(sendMsgTask);
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
				recieveMessages(connection);
				uiUpdator.updateUI(1, true);
			} catch (XMPPException ex) {
				Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + userName);
				uiUpdator.updateUI(2, false);
			}
		}
	} 
	 public void recieveMessages(XMPPConnection connection) {
		 if (connection != null) {
		      // Add a packet listener to get messages sent to us
		      PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		      connection.addPacketListener(new PacketListener() {
		        @Override
		        public void processPacket(Packet packet) {
		          Message message = (Message) packet;
		          if (message.getBody() != null) {
		            String sender = StringUtils.parseBareAddress(message.getFrom());
		            RtcLogs.i("XMPPChatDemoActivity ", " Text Recieved " + message.getBody() + " from " +  sender);
		            RtcLogs.i(TAG,sender + ":");
		            RtcLogs.i(TAG,message.getBody());
		            if(Constants.inChatActivity){
		            	uiUpdator = ChatActivity.getInstance();
		            	uiUpdator.updateUI(message.getBody());
		            }else{
		            	uiUpdator.updateUI(6, sender,message.getBody());	
		            }
		            
		          }
		        }
		      }, filter);
		    }
		 
	 }
	private class ContactsTask implements Runnable{
		@Override
		public void run() {
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			ContactData contacts = null;
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
				contacts = new ContactData();
				contacts.setName(entry.getUser());
				contacts.setPresence(entryPresence.getType());
				contactList.add(contacts);
			}
			uiUpdator.updateUI(3, contactList);
		}

	}
	private class LogoutTask implements Runnable{

		@Override
		public void run() {
			if(connection != null && connection.isConnected()){
				connection.disconnect();
			}
			uiUpdator.updateUI(4, true);
		}
		
	}
	private class SendMsgTask implements Runnable{

		@Override
		
		public void run() {
			Message message = new Message(recipient, Message.Type.chat);
			message.setBody(msg);
			message.setFrom(userName+"@"+Constants.SERVER_HOST);
			connection.sendPacket(message);
			uiUpdator.updateUI(5, true);
		}
		
	}
	
	
}
