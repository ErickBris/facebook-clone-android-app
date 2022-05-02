package com.ansoft.solti.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.ViewImageActivity;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.data.FeedItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

@SuppressLint("CutPasteId")
public class FeedListAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	public static final String TAG = "Timestamp";
	public static final String TAG2 = "likers";
	ArrayList<String> likers;
	ArrayList<String> dislikers;
	boolean liked=false;
	ImageView likeImg;
	boolean disliked;
	int cases;
	private int numOfLike;
	private int numOfDisLike;
	private int favourite=0;
	Random random;
	ImageView favicon;
	Drawable icon;
	String currentuser=new String();
	String numOfFav=new String();
	private int nFav=0;
	
	ArrayList<String> colorlist=new ArrayList<String>();
	ArrayList<String> liks=new ArrayList<String>();
	ArrayList<String> disliks=new ArrayList<String>();
	
	public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
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

	
		

	             LayoutInflater inflater = (activity).getLayoutInflater();
	             convertView = inflater.inflate(R.layout.feed_item, parent, false);
	             //Make sure the textview exists in this xml
	      

		
		
		
		
		final FeedItem item = feedItems.get(position);
		
		
		
		
		TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
		
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		
		
		TextView name = (TextView) convertView.findViewById(R.id.name);
		
		
		ImageView profilePic=(ImageView)convertView.findViewById(R.id.profilePic);
		
		TextView numLike=(TextView)convertView.findViewById(R.id.numLike);
		
		TextView numComments=(TextView)convertView.findViewById(R.id.numComments);
		
		LinearLayout sharedlin=(LinearLayout)convertView.findViewById(R.id.sharedlinear);
		
		TextView sharedMsg=(TextView)convertView.findViewById(R.id.txtSharedMessage);
		
		TextView sharedBy=(TextView)convertView.findViewById(R.id.txtSharedBy);
		String statustype=item.getMsg().get(Constants.KEY_STATUS_TYPE).toString();
		Log.e(TAG, statustype);
		if (item.getMsg().getString(Constants.KEY_STATUS_TYPE).equals(Constants.KEY_SHARED_STATUS)) {
			
			String sharedTxt=item.getMsg().get(Constants.KEY_SHARER_NAME).toString()+" shared";
			sharedMsg.setVisibility(View.VISIBLE);
			sharedMsg.setText(item.getMsg().get(Constants.KEY_SHARE_MESSAGE).toString());
			sharedBy.setText(sharedTxt);
			sharedlin.setVisibility(View.VISIBLE);
		} 
		
		numLike.setText(item.getMsg().get(Constants.KEY_TOTAL_LIKE).toString());
		numComments.setText(item.getMsg().get(Constants.KEY_TOTAL_COMMENTS).toString());
		/*
		numComments.setText(item.getMsg().get(ParseConstants.KEY_TOTAL_COMMENTS).toString()); */
		
		likeImg=(ImageView)convertView.findViewById(R.id.likeIcon);
		ImageView commentImg=(ImageView)convertView.findViewById(R.id.comIcon);
		ImageView shareImg=(ImageView)convertView.findViewById(R.id.shareIcon);
		
		
		
		checklikes(item);
		if (liked) {
		Log.e(TAG, "has liked");
		}
		
		likeImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checklikes(item);
				if (!liked) {
				likeImg.setImageResource(R.drawable.ic_like_sel);
				item.getMsg().increment(Constants.KEY_TOTAL_LIKE);
				item.getMsg().add(Constants.KEY_LIKER_IDS, ParseUser.getCurrentUser().getObjectId());
				item.getMsg().saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						
						ParseObject notification =new ParseObject(Constants.KEY_NOTIFICATION_CLASS);
						notification.put(Constants.KEY_NOTIFICATION_MESSAGE, globalvariable.getCurrentUserFullName()+" has liked your Post");
						notification.put(Constants.KEY_NOTIFICATION_RECIPIENTSID, item.getMsg().get(Constants.KEY_SENDER_ID));
						notification.put(Constants.KEY_NOTIFICATION_SENDERID, ParseUser.getCurrentUser().getObjectId().toString());
						notification.put(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC, globalvariable.getCurrentUserProfilePic());
						notification.put(Constants.KEY_NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_POST);
						notification.put(Constants.NOTIFICATION_POST_ID, item.getMsg().getObjectId());
						notification.put(Constants.KEY_NOTIFICATION_READ, false);
						notification.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								if (e==null) {
									sendPushNotifications(globalvariable.getCurrentUserFullName()+" has liked your Post", item.getMsg().get(Constants.KEY_SENDER_ID).toString());
								}
							}
						});
						checklikes(item);
						updateLikeIcon(likeImg, liked);
				}});
					
				}
				
			}
		});
		
		commentImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

			    final EditText edittext= new EditText(activity);
			    alert.setMessage("Enter Comments");
			    

			    alert.setView(edittext);

			    alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			    	imm.toggleSoftInput(0, 0);
			        Editable comments=edittext.getText();
			        String com=comments.toString();
			        String fullname=globalvariable.getCurrentUserFullName();
			        String senderProfUri=globalvariable.getCurrentUserProfilePic();
			        long time=System.currentTimeMillis();
			        String fullComments=com+"----"+fullname+"----"+senderProfUri+"----"+time;
			        item.getMsg().add(Constants.KEY_COMMENTS, fullComments);
			        item.getMsg().increment(Constants.KEY_TOTAL_COMMENTS);
			        item.getMsg().saveInBackground(new SaveCallback() {
					
						@Override
						public void done(ParseException e) {
							
							ParseObject notification =new ParseObject(Constants.KEY_NOTIFICATION_CLASS);
							notification.put(Constants.KEY_NOTIFICATION_MESSAGE, globalvariable.getCurrentUserFullName()+" has commented on your post");
							notification.put(Constants.KEY_NOTIFICATION_RECIPIENTSID, item.getMsg().get(Constants.KEY_SENDER_ID));
							notification.put(Constants.KEY_NOTIFICATION_SENDERID, ParseUser.getCurrentUser().getObjectId().toString());
							notification.put(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC, globalvariable.getCurrentUserProfilePic());
							notification.put(Constants.KEY_NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_POST);
							notification.put(Constants.NOTIFICATION_POST_ID, item.getMsg().getObjectId());
							notification.put(Constants.KEY_NOTIFICATION_READ, false);
							notification.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e==null) {
										sendPushNotifications(globalvariable.getCurrentUserFullName()+" has commented on your post", item.getMsg().get(Constants.KEY_SENDER_ID).toString());
									}
								}
							});
								if (e==null) {
									Toast.makeText(activity, "Comment published", Toast.LENGTH_SHORT);
								} else {
									Toast.makeText(activity, "Comment could not be published, please check your network connection", Toast.LENGTH_SHORT);
								}
								
							
						}
					});

			      }
			    });

			    

			    alert.show();
				
			}
		});
		shareImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

			    final EditText edittext= new EditText(activity);
			    alert.setMessage("Say something about this");
			    alert.setTitle("Share ");
			    

			    alert.setView(edittext);

			    alert.setPositiveButton("Share", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			    	imm.toggleSoftInput(0, 0);
			        Editable sharemsg=edittext.getText();
			        ParseObject message = createMessage(item, sharemsg);

			        message.saveInBackground(new SaveCallback() {
						
						
						@Override
						public void done(ParseException e) {
							if (e == null) {
								// success!
								
								Toast.makeText(activity, R.string.success_message, Toast.LENGTH_LONG).show();
								
							}
							else {
								e.printStackTrace();
								
								AlertDialog.Builder builder = new AlertDialog.Builder(activity);
								builder.setMessage(R.string.error_sending_message)
									.setTitle(R.string.error_selecting_file_title)
									.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
								
							}
						}
					});
				
			        
			        
			      }
			    });
				alert.show();
			}
		});


		
		
		
		
		CharSequence timeego = DateUtils.getRelativeTimeSpanString(
	            item.getTimeStamp(),
	            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		
		timestamp.setText(timeego);
		
		name.setText(item.getMsg().getString(Constants.KEY_SENDER_NAME).toString());
		if (!TextUtils.isEmpty(item.getMessage())) {
			statusMsg.setText(addClickablePart(item.getMessage().toString()), BufferType.SPANNABLE);
			
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			
			statusMsg.setVisibility(View.GONE);
		}
		statusMsg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String messageType = item.getMsg().getString(Constants.KEY_FILE_TYPE);
				if (messageType.equals(Constants.TYPE_TEXT)) {
					Intent intent = new Intent(activity, ViewImageActivity.class);
					globalvariable.setMsg(item.getMsg());
					activity.startActivity(intent);
				}
			}
		});
		Picasso.with(activity).load(item.getProfuri().toString()).fit().placeholder(R.drawable.placeholder).into(profilePic) ;
		
		ImageView feedImageView=(ImageView)convertView.findViewById(R.id.feedImage1);
		if (feedImageView == null) {
		    feedImageView = new ImageView(activity);
		  }
		feedImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String messageType = item.getMsg().getString(Constants.KEY_FILE_TYPE);
				ParseFile file = item.getMsg().getParseFile(Constants.KEY_FILE);
				Uri fileUri = Uri.parse(file.getUrl());
				if (messageType.equals(Constants.TYPE_IMAGE)) {
					
					Intent intent = new Intent(activity, ViewImageActivity.class);
					globalvariable.setMsg(item.getMsg());
					startActivity(intent);
				}
				else {
					// view the video
					Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
					intent.setDataAndType(fileUri, "video/*");
					startActivity(intent);
				}
			}

			private void startActivity(Intent intent) {
				
				activity.startActivity(intent);
			}
		}); 
		
		if (item.getImguri() != null) {
		
		Picasso.with(activity).load(item.getImguri().toString()).fit().centerCrop().placeholder(R.drawable.placeholder).into(feedImageView); 
		}
		else {
			feedImageView.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	private void updateLikeIcon(final ImageView likeImg,
			final boolean userHasLiked) {
		if (userHasLiked) {
			likeImg.setImageResource(R.drawable.ic_like_sel);
		}else {
			likeImg.setImageResource(R.drawable.ic_like);
		}
	}
	

	private CharSequence addClickablePart(String str) {
		str=str+" ";
		SpannableStringBuilder ssb = new SpannableStringBuilder(str);

	    int idx1 = str.indexOf("#");
	    int idx2 = 0;
	    while (idx1 != -1) {
	        idx2 = str.indexOf(" ", idx1) + 1;

	        final String clickString = str.substring(idx1, idx2);
	        
	        ssb.setSpan(new ClickableSpan() {

	            @Override
	            public void onClick(View widget) {
	                Toast.makeText(activity, clickString,
	                        Toast.LENGTH_SHORT).show();
	            }
	            
	            
	        }, idx1, idx2, 0);
	        
	        idx1 = str.indexOf("#", idx2);
	    }

	    return ssb;
	}

	public void checklikes(FeedItem item) {
		final ArrayList<String> likers=(ArrayList<String>) item.getMsg().get(Constants.KEY_LIKER_IDS);
		Log.e(TAG2, item.getMessage().toString());
		for(int i=0; i<likers.size(); i++) {
			Log.e(TAG2, likers.get(i).toString());
			if ((likers.get(i).toString()).equalsIgnoreCase((ParseUser.getCurrentUser().getObjectId().toString()) )) {
				liked=true;
				return;
			}
		}
		
			
		}
	protected ParseObject createMessage(FeedItem item, Editable sharemsg) {
		ParseObject message = new ParseObject(Constants.CLASS_MESSAGES);
		liks.add("");
		disliks.add("");
		message.put(Constants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(Constants.KEY_SENDER_NAME, item.getMsg().get(Constants.KEY_SENDER_NAME));
		message.put(Constants.KEY_RECIPIENT_IDS, getRecipientIds());
		message.put(Constants.KEY_TOTAL_LIKE, 0);
		message.put(Constants.KEY_TOTAL_DISLIKE, 0 );
		message.put(Constants.KEY_LIKER_IDS, liks);
		message.put(Constants.KEY_DISLIKER_IDS, disliks);
		message.put(Constants.KEY_STATUS_TYPE, Constants.KEY_SHARED_STATUS);
		message.put(Constants.KEY_COMMENTS, item.getMsg().get(Constants.KEY_COMMENTS));
		message.put(Constants.KEY_TOTAL_COMMENTS, item.getMsg().get(Constants.KEY_TOTAL_COMMENTS));
		message.put(Constants.KEY_FILE_TYPE, item.getMsg().get(Constants.KEY_FILE_TYPE));
		message.put(Constants.KEY_MESSAGE2, item.getMsg().get(Constants.KEY_MESSAGE2));
		message.put(Constants.KEY_MESSAGE, item.getMsg().get(Constants.KEY_MESSAGE));
		message.put(Constants.KEY_SHARE_MESSAGE, sharemsg.toString());
		message.put(Constants.KEY_SHARER_NAME, globalvariable.getCurrentUserFullName());
		
			
			
			ParseFile file = item.getMsg().getParseFile(Constants.KEY_FILE);
			message.put(Constants.KEY_FILE, file);

			return message;
		
	}


	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		recipientIds.add(ParseUser.getCurrentUser().getObjectId());
		
		return recipientIds;
	}

	

	protected void sendPushNotifications(String msg, String userid){
		ParseQuery<ParseInstallation> query=ParseInstallation.getQuery();
		query.whereEqualTo(Constants.KEY_USER_ID, userid);
		
		//send notification
		ParsePush push=new ParsePush();
		push.setQuery(query);
		push.setMessage(msg);
		push.sendInBackground();
		
		
	}	
		

	

	public ArrayList<String> getLikers() {
		return likers;
	}

	public void setLikers(ArrayList<String> likers) {
		this.likers = likers;
	}

	public ArrayList<String> getDislikers() {
		return dislikers;
	}

	public void setDislikers(ArrayList<String> dislikers) {
		this.dislikers = dislikers;
	}

	public int getnFav() {
		return nFav;
	}

	public void setnFav(int nFav) {
		this.nFav = nFav;
	}

}