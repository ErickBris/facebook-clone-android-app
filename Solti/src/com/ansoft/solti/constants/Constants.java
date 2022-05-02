package com.ansoft.solti.constants;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public final class Constants {
	// Class name
	public static final String CLASS_MESSAGES = "Messages";
	public static final String OBJECTID = "objectId";
	
	// Field names
	public static final String KEY_NOTIFICATION_CLASS="Notification";
	public static final String KEY_USER_ID="userId";
	public static final String KEY_NOTIFICATION_SENDERID="senderId";
	public static final String KEY_NOTIFICATION_SENDERNAME="senderName";
	public static final String KEY_NOTIFICATION_TYPE="NotificationType";
	public static final String KEY_NOTIFICATION_SENDERPROFILEPIC="senderProfilePic";
	public static final String KEY_NOTIFICATION_MESSAGE="Message";
	public static final String KEY_NOTIFICATION_RECIPIENTSID="recipientsId";
	public static final String KEY_NOTIFICATION_READ="read";
	public static final String NOTIFICATION_TYPE_FOLLOW="typefollow";
	public static final String NOTIFICATION_TYPE_POST="typepost";
	public static final String NOTIFICATION_POST_ID="postId";
	
	public static final String KEY_CLASS_FOLLOWERS="Followers";
	public static final String KEY_FOLLOWERS_BETWEEN="between";
	public static final String KEY_TOTAL_FOLLOWERS="totalFollowers";
	public static final String KEY_TOTAL_FOLLOWINGS="totalFollowings";
	
	public static final String KEY_CHAT_MESSAGE_CLASS="chatmsg";
	public static final String KEY_CHAT_MESSAGE_CREATEDAT="chat_createdAt";
	public static final String KEY_CHAT_MESSAGE_RECEIVERID="chat_receiverId";
	public static final String KEY_CHAT_MESSAGE_SENDERID="chat_senderId";
	public static final String KEY_CHAT_MESSAGE_SENDERNAME="chat_senderName";
	public static final String KEY_CHAT_MESSAGE="ChatMessage";
	public static final String KEY_CHAT_FROMSENDER="IsFromSender";
	public static final String KEY_CHAT_BETWEEN="chatbetween";
	
	public static final String KEY_TAGS="tags";
	public static final String KEY_SHARE_MESSAGE="shareMessage";
	public static final String KEY_SHARER_NAME="sharer";
	public static final String KEY_SEX="sex";
	public static final String KEY_SEX_MALE="Male";
	public static final String KEY_SEX_FEMALE="Female";
	public static final String KEY_SHARED_STATUS="shared";
	public static final String KEY_ORIGINAL_STATUS="original";
	public static final String KEY_STATUS_TYPE="statustype";
	public static final String KEY_COMMENTS="comments";
	public static final String KEY_TOTAL_COMMENTS="numcomments";
	public static final String KEY_FIRST_NAME="firstname";
	public static final String KEY_LAST_NAME="lastname";
	public static final String KEY_PHONE_NUMBER="phnumber";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_FRIENDS_RELATION = "friendsRelation";
	public static final String KEY_RECIPIENT_IDS = "recipientIds";
	public static final String KEY_SENDER_ID = "senderId";
	public static final String KEY_SENDER_NAME = "senderName";
	public static final String KEY_FILE = "file";
	public static final String KEY_FILE_TYPE = "fileType";
	public static final String KEY_CREATED_AT = "createdAt";
	public static final String KEY_MESSAGE="message";
	public static final String KEY_MESSAGE2="mMessage";
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_VIDEO = "video";
	public static final String TYPE_TEXT="text";
	public static final String KEY_RECIPIENTS_PROFILE_PIC="DisplayPhoto";
	
	public static final String KEY_TOTAL_LIKE="likes";
	public static final String KEY_TOTAL_DISLIKE="dislikes";
	public static final String KEY_LIKER_IDS="likers";
	public static final String KEY_DISLIKER_IDS="dislikers";
	
	public static final String KEY_FOLLOWERS="followers";
	public static final String KEY_FOLLOWING="following";
	public static final String KEY_LATITUDE="latitude";
	public static final String KEY_LONGITUDE="longitude";
	public static final String KEY_USER_CREDIT="credit";
	
	
	public static final String USER_FIRSTNAME="firstname";
	public static final String USER_LASTNAME="lastname";
	public static final String USER_PHONE_NUMBER="phnumber";
	public static final String USER_PHOTO="DisplayPhoto";
	public static final String USER_PHOTO_FILENAME="photo.jpg";
	
	public static String getDP() {
		ParseObject photo=ParseUser.getCurrentUser().getParseObject("Photo");
		
		ParseFile fil=photo.getParseFile("dp");
		String photouri=fil.getUrl();
		return photouri;
	}
	
}
