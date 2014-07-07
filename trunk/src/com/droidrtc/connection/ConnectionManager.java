package com.droidrtc.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
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
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.droidrtc.activity.UIUpdator;
import com.droidrtc.data.ContactData;
import com.droidrtc.util.Constants;
import com.droidrtc.util.MyApplication;
import com.droidrtc.util.RtcLogs;

public class ConnectionManager {
	String TAG = "ConnectionManager";
	public XMPPConnection connection;
	private ChatManager chatManager;
	private MessageListener messageListener;

	private static ConnectionManager connectionManager = new ConnectionManager();
	String userName;
	String password;
	String msg;
	String recipient;
	UIUpdator uiUpdator;
	private final ExecutorService pool;
	private ArrayList<ContactData> contactList;

	private ConnectionManager(){
		pool  = Executors.newFixedThreadPool(2);
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
	
	public boolean IsContactAdded(String userName) {
		for(int index = 0; index < contactList.size(); index++){
			ContactData contact = contactList.get(index);
			if(contact.getName().equalsIgnoreCase(userName)) {
				return true;
			}
		}
		return false;
	}	
	
	public void checkUserPresent(String userName){
		this.userName = userName;		
		searchUserTask UserPresent = new searchUserTask();  
		pool.execute(UserPresent);
	}

	public void addUser(String userName) {
		this.userName = userName;		
		AddUserTask addUserTask = new AddUserTask();
		pool.execute(addUserTask);
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
				uiUpdator.updateUI(Constants.CONN_REQ_FAILURE);
				Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
			}
			try {				
				recievePackets(connection);
				
				Log.e("XMPPClient", "userName:" + userName+"Password:"+password);
				connection.login(userName, password);
				Constants.LOGGED_IN_USER = userName;
				Log.i("XMPPClient", "Logged in as " + connection.getUser());
				Constants.xmppConnection = connection;
				
				Presence presence = new Presence(Presence.Type.unavailable);
				connection.sendPacket(presence);
				
				presence = new Presence(Presence.Type.available);
				connection.sendPacket(presence);
				
				uiUpdator.updateUI(Constants.LOGIN_REQ_SUCCESS);
				chatManager = connection.getChatManager();
				messageListener = new ChatMessageListner();
			} catch (XMPPException ex) {
				Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + userName);
				uiUpdator.updateUI(Constants.LOGIN_REQ_FAILURE);
			}
		}
	}
	
	public void recievePackets(XMPPConnection connection) {
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					if(packet instanceof Message){
						Message message = (Message) packet;
						if (message.getBody() != null) {
							String sender = StringUtils.parseBareAddress(message.getFrom());
							RtcLogs.i(TAG, " Text Recieved " + message.getBody() + " from " +  sender);
							RtcLogs.i(TAG,sender + ":");
							RtcLogs.i(TAG,message.getBody());
							sendBroadcastMessage(sender,message.getBody());
						}
					}
					else if(packet instanceof Presence) {
						Presence presence = (Presence) packet;
						String sender = StringUtils.parseBareAddress(presence.getFrom());
						RtcLogs.i(TAG, " presence type : " + presence.getType()+ " presence mode :" +  presence.getMode());
						RtcLogs.i(TAG, " presence To : " + presence.getTo()+ " presence from :" +  presence.getFrom());
						if (presence.getType() == Type.subscribe) {
							
		                } else {
		                	
		                }
						sendBroadcastMessage(sender,presence);
					}
				}
			}, filter);
		}
	}

	private class ContactsTask implements Runnable{
		@Override
		public void run() {
			Roster roster = null;
			Collection<RosterEntry> entries;
			try {
				roster = connection.getRoster();				
			} catch (Exception e) {
				e.printStackTrace();
			}
			entries = roster.getEntries();
			ContactData contacts = null;
			contactList = new ArrayList<ContactData>();
			for (RosterEntry entry : entries) {
				Presence entryPresence = roster.getPresence(entry.getUser());
				contacts = new ContactData();
				contacts.setName(entry.getUser());
				contacts.setPresence(entryPresence.getType());
				contactList.add(contacts);
			}
			uiUpdator.updateUI(Constants.LIST_CONTACTS_REQ_SUCCESS, contactList);
		}

	}
	private class LogoutTask implements Runnable{

		@Override
		public void run() {
			try {
				if (connection != null && connection.isConnected()) {
					connection.disconnect();
					uiUpdator.updateUI(Constants.LOGOUT_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				uiUpdator.updateUI(Constants.LOGOUT_FAILURE);
			}
			
		}
	}

	private class SendMsgTask implements Runnable{
		@Override
		public void run() {
			Chat chat = chatManager.createChat(recipient, messageListener);
			try {
				chat.sendMessage(msg);
				uiUpdator.updateUI(Constants.SEND_CHAT_SUCCESS);
			} catch (XMPPException e) {
				e.printStackTrace();
				uiUpdator.updateUI(Constants.SEND_CHAT_FAILURE);
			}
			
		}

	}
	
	private void sendBroadcastMessage(String from,String msg) {
		Intent intent = new Intent("my-msg");		
		intent.putExtra("FROM", from);
		intent.putExtra("MESSAGE", msg);
		LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
	} 

	private void sendBroadcastMessage(String from, Presence presence) {
		Intent intent = new Intent("my-presence");
		Type presenceType = presence.getType();
		Mode presenceMode = presence.getMode();
		intent.putExtra("FROM", from);
		intent.putExtra("PresenceType", presenceType);
		intent.putExtra("presenceMode", presenceMode);
		LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
	}
	
	private class AddUserTask implements Runnable {
		@Override
		public void run() {
			Presence subscribe = new Presence(Presence.Type.subscribe);
			String address = userName + Constants.SERVER_HOST;
            subscribe.setTo(address);
            connection.sendPacket(subscribe);
            
            try {
            	Roster roster = connection.getRoster();
                roster.createEntry(address, userName, null);
                uiUpdator.updateUI(Constants.ADD_CONTACT_SUCCESS);
            } catch (XMPPException e) {
                e.printStackTrace();
                uiUpdator.updateUI(Constants.ADD_CONTACT_FAILURE);
            }
		}
	}
	
	private class searchUserTask implements Runnable{

		@Override
		public void run() {
			if(IsContactAdded(userName)) {
				uiUpdator.updateUI(Constants.CONTACT_EXISTS);
				return;
			}

			UserSearchManager search = new UserSearchManager(connection);
			try {
				Form searchForm = search.getSearchForm("search." + connection.getServiceName());
				Form answerForm = searchForm.createAnswerForm();

				answerForm.setAnswer("Username", true);
				answerForm.setAnswer("search", "*");

				System.out.println("search form");
				ReportedData data = search.getSearchResults(answerForm, "search."+connection.getHost());

				if(data.getRows() != null)
				{
					System.out.println("not null");
					Iterator<Row> it = data.getRows();
					while(it.hasNext())
					{
						Row row = it.next();
						@SuppressWarnings("rawtypes")
						Iterator iterator = row.getValues("jid");
						if(iterator.hasNext())
						{
							String value = iterator.next().toString();
							if(value.equals(userName)){
								addUser(userName);
							}
						}
					}
				}else{
					uiUpdator.updateUI(Constants.ADD_CONTACT_FAILURE);
				}
			} catch (XMPPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				uiUpdator.updateUI(Constants.ADD_CONTACT_FAILURE);
			}
		}
	}
}
