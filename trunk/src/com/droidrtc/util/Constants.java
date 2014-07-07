package com.droidrtc.util;

import org.jivesoftware.smack.XMPPConnection;

public class Constants {

	public static String SERVER_HOST = "ec2-54-187-247-71.us-west-2.compute.amazonaws.com";
	public static int SERVER_PORT = 5222;
	public static  XMPPConnection xmppConnection = null;
	public static final Boolean PRINT_LOGS = true;
	public static boolean inChatActivity = false;
	public static String LOGGED_IN_USER;
	
	
	public static final int CONN_REQ_SUCCESS = 1;
	public static final int CONN_REQ_FAILURE = 2;
	public static final int LOGIN_REQ_SUCCESS = 3;
	public static final int LOGIN_REQ_FAILURE = 4;
	public static final int LOGIN_REQ_TIMEOUT = 5;
	public static final int LIST_CONTACTS_REQ_SUCCESS = 6;
	public static final int LIST_CONTACTS_REQ_FAILURE = 7;
	public static final int ADD_CONTACT_SUCCESS = 8;
	public static final int ADD_CONTACT_FAILURE = 9;
	public static final int CONTACT_EXISTS = 10;
	public static final int SEND_CHAT_SUCCESS = 11;
	public static final int SEND_CHAT_FAILURE = 12;
	public static final int RECV_CHAT = 13;
	public static final int LOGOUT_SUCCESS = 14;
	public static final int LOGOUT_FAILURE = 15;
	
	
}
