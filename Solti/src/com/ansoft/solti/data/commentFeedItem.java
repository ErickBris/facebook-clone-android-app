package com.ansoft.solti.data;

import com.parse.ParseObject;

public class commentFeedItem {
    private String name, Message;
    private String profuri;
    private ParseObject msg;
    long timestamp;
 
    public commentFeedItem() {
    }
 
    public commentFeedItem(String nm, String prof, String Mes, long time, ParseObject message) {
        super();
        this.name=nm;
        this.profuri=prof;
        
        this.Message=Mes;
        this.timestamp=time;
        this.msg=message;
    }

	public String getName() {
		return name;
	}

	public String getProfuri() {
		return profuri.toString();
	}

	

	public void setName(String name) {
		this.name = name;
	}

	public void setProfuri(String profuri) {
		this.profuri = profuri;
	}

	

	public void setMessage(String message) {
		this.Message = message;
	}

	public String getMessage() {
		return Message;
	}

	public long getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(long timestamp2) {
		this.timestamp = timestamp2;
	}

	public ParseObject getMsg() {
		return msg;
	}

	public void setMsg(ParseObject msg) {
		this.msg = msg;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
 
	
}