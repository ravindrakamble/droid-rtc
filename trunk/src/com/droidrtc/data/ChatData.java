package com.droidrtc.data;

public class ChatData {
	private String contactName;
	private String message;
	private long date;
	private int direction;
	
	public ChatData(String contactName, String message, long date, int direction)
	{
		this.contactName = contactName;
		this.message = message;
		this.date = date;
		this.direction = direction;
	}
	public ChatData(){
		
	}
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	
}
