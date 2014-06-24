package com.droidrtc.data;

import org.jivesoftware.smack.packet.Presence.Type;

public class ContactData {
	private String name;
	private String fullName;
	private Type presence;
	private String status;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
}
