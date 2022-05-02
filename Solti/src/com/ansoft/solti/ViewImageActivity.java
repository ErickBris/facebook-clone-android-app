package com.ansoft.solti;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansoft.solti.adapter.CommentFeedListAdapter;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.data.FeedItem;
import com.ansoft.solti.data.commentFeedItem;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends Activity {

	protected List<commentFeedItem> feedItems;
	private CommentFeedListAdapter listadapter;
	ListView list;
	boolean liked=false;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
		
		feedItems = new ArrayList<commentFeedItem>();
		setupActionBar();
		list=(ListView) findViewById(R.id.listViewInOwnProfile);
		LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
		LinearLayout headerLayout = (LinearLayout) inflater.inflate(R.layout.viewimage_header, null);
		
		
		list.addHeaderView(headerLayout);
		ImageView imageView = (ImageView)findViewById(R.id.imageView);
		ImageView profileImg=(ImageView) findViewById(R.id.profilePicInComments);
		TextView fullname=(TextView) findViewById(R.id.nameInComments);
		TextView timestamp=(TextView) findViewById(R.id.timestampInComments);
		TextView statusMsg=(TextView) findViewById(R.id.txtStatusMsgInComments);
		TextView noLike=(TextView) findViewById(R.id.numLikeInComments);
		TextView noComments=(TextView) findViewById(R.id.numCommentsInComments);
		
		final ImageView likeIcon=(ImageView) findViewById(R.id.likeIconInComments);
		ImageView commentIcon=(ImageView) findViewById(R.id.comIconInComments);
		ImageView shareIcon=(ImageView) findViewById(R.id.shareIconInComments);
		
		final ParseObject item2=globalvariable.getMsg();
		
		likeIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checklikes(item2);
				if (!liked) {
				likeIcon.setImageResource(R.drawable.ic_like_sel);
				item2.increment(Constants.KEY_TOTAL_LIKE);
				item2.add(Constants.KEY_LIKER_IDS, ParseUser.getCurrentUser().getObjectId());
				item2.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(ParseException e) {
						
						ParseObject notification =new ParseObject(Constants.KEY_NOTIFICATION_CLASS);
						notification.put(Constants.KEY_NOTIFICATION_MESSAGE, globalvariable.getCurrentUserFullName()+" has liked your Post");
						notification.put(Constants.KEY_NOTIFICATION_RECIPIENTSID, item2.get(Constants.KEY_SENDER_ID));
						notification.put(Constants.KEY_NOTIFICATION_SENDERID, ParseUser.getCurrentUser().getObjectId().toString());
						notification.put(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC, globalvariable.getCurrentUserProfilePic());
						notification.put(Constants.KEY_NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_POST);
						notification.put(Constants.NOTIFICATION_POST_ID, item2.getObjectId());
						notification.put(Constants.KEY_NOTIFICATION_READ, false);
						notification.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								if (e==null) {
									sendPushNotifications(globalvariable.getCurrentUserFullName()+" has liked your Post", item2.get(Constants.KEY_SENDER_ID).toString());
								}
							}
						});
						checklikes(item2);
						
				}});
					
				}
				
			}
		});
		
		
		
		commentIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ViewImageActivity.this);

			    final EditText edittext= new EditText(ViewImageActivity.this);
			    alert.setMessage("Enter Comments");
			    

			    alert.setView(edittext);

			    alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	InputMethodManager imm = (InputMethodManager) ViewImageActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
			    	imm.toggleSoftInput(0, 0);
			        Editable comments=edittext.getText();
			        String com=comments.toString();
			        String fullname=globalvariable.getCurrentUserFullName();
			        String senderProfUri=globalvariable.getCurrentUserProfilePic();
			        long time=System.currentTimeMillis();
			        String fullComments=com+"----"+fullname+"----"+senderProfUri+"----"+time;
			        item2.add(Constants.KEY_COMMENTS, fullComments);
			        item2.increment(Constants.KEY_TOTAL_COMMENTS);
			        item2.saveInBackground(new SaveCallback() {
					
						@Override
						public void done(ParseException e) {
							
							ParseObject notification =new ParseObject(Constants.KEY_NOTIFICATION_CLASS);
							notification.put(Constants.KEY_NOTIFICATION_MESSAGE, globalvariable.getCurrentUserFullName()+" has commented on your post");
							notification.put(Constants.KEY_NOTIFICATION_RECIPIENTSID, item2.get(Constants.KEY_SENDER_ID));
							notification.put(Constants.KEY_NOTIFICATION_SENDERID, ParseUser.getCurrentUser().getObjectId().toString());
							notification.put(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC, globalvariable.getCurrentUserProfilePic());
							notification.put(Constants.KEY_NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_POST);
							notification.put(Constants.NOTIFICATION_POST_ID, item2.getObjectId());
							notification.put(Constants.KEY_NOTIFICATION_READ, false);
							notification.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException e) {
									if (e==null) {
										sendPushNotifications(globalvariable.getCurrentUserFullName()+" has commented on your post", item2.get(Constants.KEY_SENDER_ID).toString());
									}
								}
							});
								if (e==null) {
									Toast.makeText(ViewImageActivity.this, "Comment published", Toast.LENGTH_SHORT);
								} else {
									Toast.makeText(ViewImageActivity.this, "Comment could not be published, please check your network connection", Toast.LENGTH_SHORT);
								}
								
							
						}
					});

			      }
			    });

			    

			    alert.show();
				
			}
		});
		
		
		shareIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ViewImageActivity.this);

			    final EditText edittext= new EditText(ViewImageActivity.this);
			    alert.setMessage("Say something about this");
			    alert.setTitle("Share ");
			    

			    alert.setView(edittext);

			    alert.setPositiveButton("Share", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	InputMethodManager imm = (InputMethodManager) ViewImageActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
			    	imm.toggleSoftInput(0, 0);
			        Editable sharemsg=edittext.getText();
			        ParseObject message = createMessage(item2, sharemsg);

			        message.saveInBackground(new SaveCallback() {
						
						
						@Override
						public void done(ParseException e) {
							if (e == null) {
								// success!
								
								Toast.makeText(ViewImageActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
								
							}
							else {
								e.printStackTrace();
								
								AlertDialog.Builder builder = new AlertDialog.Builder(ViewImageActivity.this);
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
		
		int numberofComment=0;
		numberofComment=globalvariable.getMsg().getInt(Constants.KEY_TOTAL_COMMENTS);
		//set the image
		if (globalvariable.getMsg().get(Constants.KEY_FILE_TYPE).toString().equals(Constants.TYPE_TEXT)) {
			imageView.setVisibility(imageView.INVISIBLE);
		} else {
		ParseFile file = globalvariable.getMsg().getParseFile(Constants.KEY_FILE);
		Uri imageUri = Uri.parse(file.getUrl());
		
		
		Picasso.with(this).load(imageUri.toString()).fit().placeholder(R.drawable.placeholder).into(imageView);
		}
		//set the profile Image
		
		Uri profuri=Uri.parse((String) globalvariable.getMsg().get(Constants.KEY_MESSAGE));
		Picasso.with(this).load(profuri.toString()).placeholder(R.drawable.placeholder).into(profileImg);
		
		//set the fullname
		fullname.setText(globalvariable.getMsg().getString(Constants.KEY_SENDER_NAME));
		
		//set teh timestamp
		Date date = globalvariable.getMsg().getCreatedAt();
		Format formatter = new SimpleDateFormat("ddMMyyyyHHmm");
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long time=cal.getTimeInMillis();
		
		CharSequence timeego = DateUtils.getRelativeTimeSpanString(
	            time,
	            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeego);
		
		//set the status
		statusMsg.setText(globalvariable.getMsg().get(Constants.KEY_MESSAGE2).toString());
		
		//set the number of likes
		noLike.setText(globalvariable.getMsg().get(Constants.KEY_TOTAL_LIKE).toString());
		
		//set the number of comments
		noComments.setText(globalvariable.getMsg().get(Constants.KEY_TOTAL_COMMENTS).toString());
		
		if (numberofComment!=0) {
			
			String[] profileUri=new String[numberofComment];
			String[] FN=new String[numberofComment];
			String[] statusText=new String[numberofComment];
			long[] timeago=new long[numberofComment];
			ParseObject message=globalvariable.getMsg();			
			ArrayList<String> comm=new ArrayList<String>();
			comm=(ArrayList<String>) message.get(Constants.KEY_COMMENTS);
				int i=0;
			for(String str : comm)
			{
			
						String rawComment=comm.get(i);
						String parts[]=rawComment.split("----");
						statusText[i]=parts[0];
						FN[i]=parts[1];
						profileUri[i]=parts[2];
						
						timeago[i]=Long.parseLong(parts[3]);
						
						commentFeedItem item=new commentFeedItem();
						item.setProfuri(profileUri[i]);
						item.setName(FN[i]);
						item.setMessage(statusText[i]);
						item.setTimestamp(timeago[i]);
						feedItems.add(item);
						i++;
					}
				}
				
				listadapter=new CommentFeedListAdapter(this, feedItems);
				list.setAdapter(listadapter);
			
		
		
	}
	@Override
	public void onBackPressed() {
		ViewImageActivity.this.finish();
	}
	
	
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void checklikes(ParseObject item) {
		final ArrayList<String> likers=(ArrayList<String>) item.get(Constants.KEY_LIKER_IDS);
		
		for(int i=0; i<likers.size(); i++) {
			
			if ((likers.get(i).toString()).equalsIgnoreCase((ParseUser.getCurrentUser().getObjectId().toString()) )) {
				liked=true;
				return;
			}
		}
		
			
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
	protected ParseObject createMessage(ParseObject item, Editable sharemsg) {
		ParseObject message = new ParseObject(Constants.CLASS_MESSAGES);
		ArrayList<String> liks=new ArrayList<String>();
		ArrayList<String> disliks=new ArrayList<String>();
		liks.add("");
		disliks.add("");
		message.put(Constants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(Constants.KEY_SENDER_NAME, item.get(Constants.KEY_SENDER_NAME));
		message.put(Constants.KEY_RECIPIENT_IDS, getRecipientIds());
		message.put(Constants.KEY_TOTAL_LIKE, 0);
		message.put(Constants.KEY_TOTAL_DISLIKE, 0 );
		message.put(Constants.KEY_LIKER_IDS, liks);
		message.put(Constants.KEY_DISLIKER_IDS, disliks);
		message.put(Constants.KEY_STATUS_TYPE, Constants.KEY_SHARED_STATUS);
		message.put(Constants.KEY_COMMENTS, item.get(Constants.KEY_COMMENTS));
		message.put(Constants.KEY_TOTAL_COMMENTS, item.get(Constants.KEY_TOTAL_COMMENTS));
		message.put(Constants.KEY_FILE_TYPE, item.get(Constants.KEY_FILE_TYPE));
		message.put(Constants.KEY_MESSAGE2, item.get(Constants.KEY_MESSAGE2));
		message.put(Constants.KEY_MESSAGE, item.get(Constants.KEY_MESSAGE));
		message.put(Constants.KEY_SHARE_MESSAGE, sharemsg.toString());
		message.put(Constants.KEY_SHARER_NAME, globalvariable.getCurrentUserFullName());
		
			
			
			ParseFile file = item.getParseFile(Constants.KEY_FILE);
			message.put(Constants.KEY_FILE, file);

			return message;
		
	}
	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		recipientIds.add(ParseUser.getCurrentUser().getObjectId());
		
		return recipientIds;
	}

}
