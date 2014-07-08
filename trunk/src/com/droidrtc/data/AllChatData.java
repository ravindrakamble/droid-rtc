package com.droidrtc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AllChatData {
	INSTANCE;

	private Map<String, List<ChatData>> chatHistory = new HashMap<String, List<ChatData>>();

	private AllChatData(){

	}

	public void addChatData(String username, ChatData chatDataToAdd){
		if(chatHistory.containsKey(username)){
			List<ChatData> chatList = chatHistory.get(username);
			if(chatList == null){
				chatList = new ArrayList<ChatData>();
			}
			chatList.add(chatDataToAdd);
			chatHistory.put(username, chatList);
		}else{
			List<ChatData> chatList = new ArrayList<ChatData>();
			chatList.add(chatDataToAdd);
			chatHistory.put(username, chatList);
		}
	}

	public List<ChatData> getChatHistory(String username){
		List<ChatData> dataToReturn = null;
		if(chatHistory.containsKey(username)){
			dataToReturn = chatHistory.get(username);
		}

		if(dataToReturn == null){
			dataToReturn = new ArrayList<ChatData>();
		}

		return dataToReturn;
	}
}
