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
import android.widget.TextView;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.ViewImageActivity;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.data.FeedItem;
import com.ansoft.solti.data.commentFeedItem;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

@SuppressLint("CutPasteId")
public class CommentFeedListAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<commentFeedItem> feedItems;
	
	public static final String TAG = "Timestamp";
		
	public CommentFeedListAdapter(Activity activity, List<commentFeedItem> feedItems2) {
		this.activity = activity;
		this.feedItems = feedItems2;
	}

	@Override
	public int getCount() {
		return feedItems.size();
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
		convertView = inflater.inflate(R.layout.feed_comment, null);
		
		final commentFeedItem item = feedItems.get(position);
		
		
		
		
		TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
		
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		
		
		ImageView profilePic=(ImageView)convertView.findViewById(R.id.profilePic);
	
		CharSequence timeego = DateUtils.getRelativeTimeSpanString(
	            item.getTimeStamp(),
	            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		
		timestamp.setText(timeego);
		
		name.setText(item.getName());
		if (!TextUtils.isEmpty(item.getMessage())) {
			statusMsg.setText(item.getMessage());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			
			statusMsg.setVisibility(View.GONE);
		}
		Picasso.with(activity).load(item.getProfuri().toString()).fit().placeholder(R.drawable.placeholder).into(profilePic) ;
		
		
		
		
		
		return convertView;} else {
			return convertView;
		}
	
	
	}

}