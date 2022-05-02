package com.ansoft.solti.adapter;

import com.parse.ParseUser;

public class Friend {
    public ParseUser puser;
    public String title;
    public Friend(){
        super();
    }
    
    public Friend(ParseUser puser, String title) {
        super();
        this.puser = puser;
        this.title = title;
    }
}
