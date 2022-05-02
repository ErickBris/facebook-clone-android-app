package com.ansoft.solti.data;

import com.parse.ParseObject;

public class NotificationItem {
    private String message;
    private String profuri;
    private ParseObject Notification;
    public ParseObject getNotification() {
		return Notification;
	}

	public void setNotification(ParseObject notification) {
		Notification = notification;
	}

	long timestamp;
 
    public NotificationItem() {
    }
 
    public NotificationItem(String nm, String proflong, long time, ParseObject no) {
        super();
        this.message=nm;
        this.profuri=proflong;
        this.Notification=no;
        this.timestamp=time;
        
    }

	public String getMessage() {
		return message;
	}

	public String getProfuri() {
		return profuri.toString();
	}

	

	public void setMessage(String name) {
		this.message = name;
	}

	public void setProfuri(String profuri) {
		this.profuri = profuri;
	}

	

	public long getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(long timestamp2) {
		this.timestamp = timestamp2;
	}

	

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
 
	
}