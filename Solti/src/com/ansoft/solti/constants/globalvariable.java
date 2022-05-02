package com.ansoft.solti.constants;
import java.util.ArrayList;
import android.graphics.Bitmap;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
public final class globalvariable {
	public static String currentUserName;
	public static String currentUserId;
	public static String currentUserFullName;
	public static String currentUserPhNumber;
	public static String currentUserEmail;
	public static int currentUserNoOfFollowers;
	public static int currentUserNoOfFollowings;
	public static ArrayList<String> currentUserFollowers;
	public static ArrayList<String> currentUserFollowing;
	public static ArrayList<String> otherUserFollowers;
	public static ArrayList<String> otherUserFollowings;
	public static double currentUserLatitude;
	public static double currentUserLongitude;
	public static String currentUserProfilePic;
	public static ParseUser cuser;
	public static Bitmap bmp;
	public static String[] contactlist=null;
	public static ParseUser user;
	public static String phn;
	public static ParseObject msg;
	public static String chatUserId;
	public static String getChatUserId() {
		return chatUserId;
	}
	public static void setChatUserId(String chatUserId) {
		globalvariable.chatUserId = chatUserId;
	}
	public static ParseObject getMsg() {
		return msg;
	}
	public static void setMsg(ParseObject msg) {
		globalvariable.msg = msg;
	}
	public static int getCurrentUserNoOfFollowers() {
		return currentUserNoOfFollowers;
	}
	public static void setCurrentUserNoOfFollowers(int currentUserNoOfFollowers) {
		globalvariable.currentUserNoOfFollowers = currentUserNoOfFollowers;
	}
	public static int getCurrentUserNoOfFollowings() {
		return currentUserNoOfFollowings;
	}
	public static void setCurrentUserNoOfFollowings(int currentUserNoOfFollowings) {
		globalvariable.currentUserNoOfFollowings = currentUserNoOfFollowings;
	}
		@SuppressWarnings("unchecked")
	public static void initialize() {
		cuser=ParseUser.getCurrentUser();
		globalvariable.currentUserName=cuser.getUsername().toString();
		globalvariable.currentUserId=cuser.getObjectId().toString();
		globalvariable.currentUserFullName=cuser.getString(Constants.USER_FIRSTNAME)+" "+cuser.getString(Constants.USER_LASTNAME);
		globalvariable.currentUserPhNumber=cuser.getString(Constants.USER_PHONE_NUMBER).toString();
		globalvariable.currentUserEmail=cuser.getEmail().toString();
		globalvariable.currentUserFollowers=(ArrayList<String>) cuser.get(Constants.KEY_FOLLOWERS);
		globalvariable.currentUserFollowing=(ArrayList<String>) cuser.get(Constants.KEY_FOLLOWING);
		globalvariable.currentUserLatitude=cuser.getDouble(Constants.KEY_LATITUDE);
		globalvariable.currentUserLongitude=cuser.getDouble(Constants.KEY_LONGITUDE);
		ParseFile photo=cuser.getParseFile(Constants.USER_PHOTO);
		globalvariable.currentUserProfilePic=photo.getUrl().toString();
		
	}
	@SuppressWarnings("unchecked")
	public static void refresharrays() {
		globalvariable.currentUserFollowers=(ArrayList<String>) cuser.get(Constants.KEY_FOLLOWERS);
		globalvariable.currentUserFollowing=(ArrayList<String>) cuser.get(Constants.KEY_FOLLOWING);
		globalvariable.otherUserFollowers=(ArrayList<String>) user.get(Constants.KEY_FOLLOWERS);
		globalvariable.otherUserFollowings=(ArrayList<String>) user.get(Constants.KEY_FOLLOWING);
	}
	public static String getprof() {
		return currentUserProfilePic;
	}
	public static void setuser(ParseUser ps) {
		user=ps;
	}
	public static ParseUser getuser() {
		return user;
	}
	public static void setph(String ph) {
		phn=ph;
	}
	public static void setContactlist(String[] contactlist) {
		globalvariable.contactlist = contactlist;
	}
	public static void setContactlist(String phcon, int i) {
		contactlist[i]=phcon;
		
	}
	public static void erasenull() {
		for (int i=0; i<contactlist.length; i++) {
			contactlist[i]=contactlist[i].replace("+977 ", "");
			contactlist[i]=contactlist[i].replace("+977", "");
			contactlist[i]=contactlist[i].replace("-", "");
			contactlist[i]=contactlist[i].replace("+", "");
			contactlist[i]=contactlist[i].replace("*", "");
		}
	}
	public static void setBmp(Bitmap ump) {
		bmp = ump;
	}
	public static Bitmap getBmp() {
		return bmp;
	}
	public static String getCurrentUserName() {
		return currentUserName;
	}
	public static String getCurrentUserId() {
		return currentUserId;
	}
	public static String getCurrentUserFullName() {
		return currentUserFullName;
	}
	public static String getCurrentUserPhNumber() {
		return currentUserPhNumber;
	}
	public static String getCurrentUserEmail() {
		return currentUserEmail;
	}
	public static ArrayList<String> getCurrentUserFollowers() {
		return currentUserFollowers;
	}
	public static ArrayList<String> getCurrentUserFollowing() {
		return currentUserFollowing;
	}
	public static double getCurrentUserLatitude() {
		return currentUserLatitude;
	}
	public static double getCurrentUserLongitude() {
		return currentUserLongitude;
	}
	public static String getCurrentUserProfilePic() {
		return currentUserProfilePic;
	}
}
