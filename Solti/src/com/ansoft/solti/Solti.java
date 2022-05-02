package com.ansoft.solti;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.ansoft.solti.constants.Constants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.squareup.picasso.Picasso;

public class Solti extends Application {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() { 
		super.onCreate();
		
		
		 
		
		Parse.initialize(this, "hjhvb1LEbvxcS1tRuSUhaq0faPPAXThAEGzLxBNk", "20eDmqCe8iac2WmvGx1mVxHOXwgu1pxfYLBKzZjA");

		  ParseInstallation.getCurrentInstallation().saveInBackground();
		  PushService.setDefaultPushCallback(this, MainActivity.class, R.drawable.pushicon);
		
	}
	protected static void UpdateParseInstallation(ParseUser user) {
		ParseInstallation installation=ParseInstallation.getCurrentInstallation();
		installation.put(Constants.KEY_USER_ID, user.getObjectId());
		installation.saveInBackground();
	}
	
	public static void LoadImg(ImageView img, Context con, String path){
		Picasso.with(con).load(path).into(img);
	}
	
}
