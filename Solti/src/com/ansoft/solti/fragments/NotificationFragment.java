package com.ansoft.solti.fragments;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ansoft.solti.R;
import com.ansoft.solti.R.layout;
import com.ansoft.solti.adapter.FeedListAdapter;
import com.ansoft.solti.adapter.NotificationFeedListAdapter;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.data.FeedItem;
import com.ansoft.solti.data.NotificationItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class NotificationFragment extends ListFragment {

	protected ProgressBar pb;
	public static final String TAG = "Username";
	protected List<ParseObject> mMessages;
	protected List<NotificationItem> notificationItems;
	private NotificationFeedListAdapter listAdapter;
	protected SwipeRefreshLayout swipe;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_notification_fragment,
				container, false);
		pb=(ProgressBar) rootView.findViewById(R.id.pbar);
		swipe=(SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
		swipe.setOnRefreshListener(mrl);
		swipe.setColorScheme(R.color.swipe1, R.color.swipe2, R.color.swipe3, R.color.swipe4);
		refresh();
		return rootView;
	}

	protected OnRefreshListener mrl=new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			refresh();
			
		}
	};
	@Override
	public void onResume() {
		super.onResume();
		notificationItems = new ArrayList<NotificationItem>();
		
	
	}
	private void refresh() {
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Constants.KEY_NOTIFICATION_CLASS);
		
		query.whereEqualTo(Constants.KEY_NOTIFICATION_RECIPIENTSID, ParseUser.getCurrentUser().getObjectId());
		query.addDescendingOrder(Constants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				if (swipe.isRefreshing()) {
					swipe.setRefreshing(false);
				}
				getActivity().setProgressBarIndeterminateVisibility(false);
				pb.setVisibility(View.GONE);
				if (e == null) {
					// We found messages!
					if (notificationItems!=null){
						notificationItems.clear();}
					mMessages = messages;

					String[] Msg = new String[mMessages.size()];
					Uri[] profUri =new Uri[mMessages.size()];
					
					long[] timestamp=new long[mMessages.size()];
					
					int i = 0;
					for(ParseObject message : mMessages) {
						Msg[i] = message.getString(Constants.KEY_NOTIFICATION_MESSAGE);
						
						
						profUri[i]=Uri.parse(message.getString(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC));
						
						Date date = message.getCreatedAt();
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						timestamp[i]=cal.getTimeInMillis();
						
						
						
						
						NotificationItem item=new NotificationItem();
						item.setNotification(message);
						
						
						item.setTimeStamp(timestamp[i]);
						
						item.setMessage(Msg[i]);
						item.setProfuri(profUri[i].toString());
						notificationItems.add(item);
						i++;
					}
					
						
					
				} 

				listAdapter = new NotificationFeedListAdapter(getActivity(), notificationItems);
				
				
				
				
				setListAdapter(listAdapter);
			
			}
		}); 
	}
	
}