package com.ansoft.solti.adapter;

import java.util.List;

import com.ansoft.solti.R;
import com.ansoft.solti.data.Message;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter {
	private Context activity;
	
	private List<Message> chatItems;
	public ChatListAdapter(Context context,
			List<Message> chatItems) {
		super();
		this.activity = context;
		this.chatItems = chatItems;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Message message = chatItems.get(position);

		final boolean isMe = message.isSelf();

		final ViewHolder listholder = new ViewHolder();
		if (convertView == null) {
			// Identifying the message owner
			if (isMe) {
				// message belongs to you, so load the right aligned layout
				convertView = LayoutInflater.from(activity).inflate(
						R.layout.list_item_message_right, parent, false);
				
				listholder.name = (TextView) convertView.findViewById(R.id.lblMsgFrom);
				listholder.body = (TextView) convertView.findViewById(R.id.txtMsg);
			} else {
				// message belongs to other person, load the left aligned layout
				convertView = LayoutInflater.from(activity).inflate(
						R.layout.list_item_message_left, parent, false);
				
				listholder.name = (TextView) convertView.findViewById(R.id.lblMsgFrom);
				listholder.body = (TextView) convertView.findViewById(R.id.txtMsg);
			}
		}
		
		listholder.name.setText(message.getSenderName());
		listholder.body.setText(message.getMessage());

		return convertView;
	}

	final class ViewHolder {
		public TextView name;
		public TextView body;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}