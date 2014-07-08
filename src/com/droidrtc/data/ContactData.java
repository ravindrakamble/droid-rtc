package com.droidrtc.data;

import java.util.List;

import org.jivesoftware.smack.packet.Presence.Type;

public class ContactData {
	private String name;
	private String fullName;
	private Type presence;
	private String status;
	private String type;
	private List<ChatData> chatHistory;
	private String lastMessage;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Type getPresence() {
		return presence;
	}
	public void setPresence(Type type2) {
		this.presence = type2;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ChatData> getChatHistory() {
		return chatHistory;
	}
	public void setChatHistory(List<ChatData> chatHistory) {
		this.chatHistory = chatHistory;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

}
