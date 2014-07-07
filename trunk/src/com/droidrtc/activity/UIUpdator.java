package com.droidrtc.activity;

public interface UIUpdator {
	
	void updateUI(int reqCode);
	void updateUI(int reqCode, Object response);
	void updateUI(int reqCode,String sender,String message);
	void updateUI(String message);
}
