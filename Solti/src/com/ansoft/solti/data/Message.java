package com.ansoft.solti.data;

import com.ansoft.solti.constants.Constants;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(Constants.KEY_CHAT_MESSAGE_CLASS)
public class Message {
	public String senderName, Message;
	public boolean isSelf;
	public Message() {}
	
	public Message(String senderName, String message, boolean isSelf) {
		super();
		this.senderName = senderName;
		this.Message = message;
		this.isSelf = isSelf;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
}
