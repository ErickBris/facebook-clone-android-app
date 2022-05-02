package com.ansoft.solti.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.ShowProfile;
import com.ansoft.solti.ViewImageActivity;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.data.FeedItem;
import com.ansoft.solti.data.NotificationItem;
import com.ansoft.solti.data.commentFeedItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

@SuppressLint("CutPasteId")
public class NotificationFeedListAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<NotificationItem> feedItems;
	
	public static final String TAG = "Timestamp";
		
	public NotificationFeedListAdapter(Activity activity, List<NotificationItem> feedItems2) {
		this.activity = activity;
		this.feedItems = feedItems2;
	}

	@Override
	public int getCount() {
		if (feedItems!=null){
		return feedItems.size();}
		return 0;
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		
		if (convertView == null) {
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.notification_item, null);
		
		final NotificationItem item = feedItems.get(position);
		final LinearLayout feed=(LinearLayout)convertView.findViewById(R.id.notifylin);
		
		
		
		TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
		
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.name);
		
		
		
		ImageView profilePic=(ImageView)convertView.findViewById(R.id.profilePic);
	
		CharSequence timeego = DateUtils.getRelativeTimeSpanString(
	            item.getTimeStamp(),
	            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		
		timestamp.setText(timeego);
		
		
		if (!TextUtils.isEmpty(item.getMessage())) {
			statusMsg.setText(item.getMessage());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			
			statusMsg.setVisibility(View.GONE);
		}
		Picasso.with(activity).load(item.getProfuri().toString()).fit().placeholder(R.drawable.placeholder).into(profilePic) ;
		if (item.getNotification().getBoolean(Constants.KEY_NOTIFICATION_READ)) {
			feed.setBackgroundResource(R.color.feed_bg);
			
			statusMsg.setTextColor(Color.parseColor("#1b1b1b"));
			timestamp.setTextColor(Color.parseColor("#181818"));
		}
		feed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String type=item.getNotification().getString(Constants.KEY_NOTIFICATION_TYPE);
				feed.setBackgroundResource(R.color.feed_bg);
				if (type.equals(Constants.NOTIFICATION_TYPE_POST)) {
					
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Constants.CLASS_MESSAGES);
					
					query.whereEqualTo(Constants.OBJECTID, item.getNotification().getString(Constants.NOTIFICATION_POST_ID));
					
					query.findInBackground(new FindCallback<ParseObject>() {
						ProgressDialog dialog=ProgressDialog.show(activity, "", "");
						@Override
						public void done(List<ParseObject> msg,ParseException e) {
							dialog.dismiss();
							if (e==null) {
								ParseObject message = null;
								for(ParseObject mesg : msg) {
									message=mesg;
								}
								item.getNotification().put(Constants.KEY_NOTIFICATION_READ, true);
								item.getNotification().saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException arg0) {
										// TODO Auto-generated method stub
										
									}
								});
								Intent intent = new Intent(activity, ViewImageActivity.class);
								globalvariable.setMsg(message);
								activity.startActivity(intent);
							}
							
						}
					});
				} else {
					ParseQuery<ParseUser> query = ParseUser.getQuery();
					
					query.whereEqualTo(Constants.OBJECTID, item.getNotification().get(Constants.KEY_SENDER_ID));
					
					query.findInBackground(new FindCallback<ParseUser>() {
						ProgressDialog dialog=ProgressDialog.show(activity, "", "");
						@Override
						public void done(List<ParseUser> users, ParseException e) {
							dialog.dismiss();
							if (e==null) {
								item.getNotification().put(Constants.KEY_NOTIFICATION_READ, true);
								item.getNotification().saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException arg0) {
										// TODO Auto-generated method stub
										
									}
								});
								globalvariable.setuser(users.get(0));
								activity.startActivity(new Intent(activity, ShowProfile.class));
								
							}
							
						}});
					
				}
			}
		});
		
		
		
		
		return convertView;} else {
			return convertView;
		}
	
	
	}

}