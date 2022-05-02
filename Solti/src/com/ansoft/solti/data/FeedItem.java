package com.ansoft.solti.data;

import com.parse.ParseObject;

public class FeedItem {
    private String name, Message;
    private String profuri, imguri;
    private ParseObject msg;
    long timestamp;
 
    public FeedItem() {
    }
 
    public FeedItem(String nm, String prof, String img, String Mes, long time, ParseObject message) {
        super();
        this.name=nm;
        this.profuri=prof;
        this.imguri=img;
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

	public String getImguri() {
		if (imguri!=null) {
		return imguri.toString();} else {
			return null;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProfuri(String profuri) {
		this.profuri = profuri;
	}

	public void setImguri(String string) {
		this.imguri = string;
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