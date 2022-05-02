package com.ansoft.solti;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ansoft.solti.adapter.FeedListAdapter;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.data.FeedItem;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class ShowProfile extends Activity {

	public static final String TAG = ShowProfile.class.getSimpleName();
	public static final String TAG2 = "processes";
	
	String cases="";
	ParseUser user;
	ImageView profilePic;
	TextView un, email, fullname, phnumber, noPost;
	TextView totalFollowers, totalFollowings;
	ImageView followngtext;
	private FeedListAdapter listAdapter;
	protected List<ParseObject> mMessages;
	ImageButton addBtn, remBtn;
	protected ProgressBar pb;
	protected List<FeedItem> feedItems;
	ListView list;
	String strun=null, stremail=null, strfn=null, strph=null;
	
	protected static ParseUser mCurrentUser;
	protected static ParseRelation<ParseUser> mFriendsRelation;
	protected static ParseUser mOtherUser;
	protected static ParseRelation<ParseUser> mOtherFriendsRelation;
	String AID=null;
	String BID=null;
	ArrayList<String> Afollower=new ArrayList<String>();
	ArrayList<String> Afollowing=new ArrayList<String>();
	ArrayList<String> Bfollower=new ArrayList<String>();
	ArrayList<String> Bfollowing=new ArrayList<String>();
	boolean AtoB=false;
	boolean BtoA=false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_show_profile);
		totalFollowers=(TextView)findViewById(R.id.totalfollower);
		totalFollowings=(TextView)findViewById(R.id.totalfollowing);
		un=(TextView) findViewById(R.id.username);
		pb=(ProgressBar)findViewById(R.id.pb);
		fullname=(TextView) findViewById(R.id.fullname);
		noPost=(TextView) findViewById(R.id.nopostyet);
		list=(ListView)findViewById(R.id.listPro);
		addBtn=(ImageButton) findViewById(R.id.addbtn);
		remBtn=(ImageButton) findViewById(R.id.remvbtn);
		followngtext=(ImageView) findViewById(R.id.following_text);
		profilePic=(ImageView) findViewById(R.id.profileImage);
		if (user==ParseUser.getCurrentUser()){
			user=ParseUser.getCurrentUser();
		} else {
		try {
			user=globalvariable.getuser();
		} catch(Exception e) {
			
		}}
		feedItems = new ArrayList<FeedItem>();
		mCurrentUser=ParseUser.getCurrentUser();
		mFriendsRelation=mCurrentUser.getRelation(Constants.KEY_FRIENDS_RELATION);
		mOtherUser=user;
		mOtherFriendsRelation=mOtherUser.getRelation(Constants.KEY_FRIENDS_RELATION);
		String name1=ParseUser.getCurrentUser().getObjectId().toString();
		String name2=user.getObjectId().toString();
		final String AfollowingB=name1+name2;
		final String BfollowingA=name2+name1;
		
		
		un.setText("@"+user.getUsername().toString());
		
		fullname.setText(user.getString(Constants.USER_FIRSTNAME).toString()+" "+user.getString(Constants.USER_LASTNAME));
		
		ParseFile photo=user.getParseFile(Constants.USER_PHOTO);
		Picasso.with(ShowProfile.this).load(photo.getUrl().toString()).placeholder(R.drawable.profile_mage_placeholder).into(profilePic);
		if (user!=ParseUser.getCurrentUser()){
		totalFollowers.setText(user.get(Constants.KEY_TOTAL_FOLLOWERS).toString());
		totalFollowings.setText(user.get(Constants.KEY_TOTAL_FOLLOWINGS).toString());
		} else {
			totalFollowers.setText(ParseUser.getCurrentUser().get(Constants.KEY_TOTAL_FOLLOWERS).toString());
			totalFollowings.setText(ParseUser.getCurrentUser().get(Constants.KEY_TOTAL_FOLLOWINGS).toString());
		}
		if (user!=ParseUser.getCurrentUser()){
			getcases();
		
		addBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cases=="CaseB") {
					followngtext.setVisibility(View.VISIBLE);
					remBtn.setVisibility(View.VISIBLE);
					addBtn.setVisibility(View.GONE);
					ParseObject follower = new ParseObject(Constants.KEY_CLASS_FOLLOWERS);
					follower.put(Constants.KEY_FOLLOWERS_BETWEEN, AfollowingB);
					follower.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							
								mFriendsRelation.add(user);
								mCurrentUser.saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException e) {
										
										mOtherFriendsRelation.add(ParseUser.getCurrentUser());
										mOtherUser.saveInBackground(new SaveCallback() {
											
											@Override
											public void done(ParseException arg0) {
												ParseUser.getCurrentUser().increment(Constants.KEY_TOTAL_FOLLOWINGS);
												ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
													
													@Override
													public void done(ParseException arg0) {
														user.increment(Constants.KEY_TOTAL_FOLLOWERS);
														user.saveInBackground(new SaveCallback() {
															
															@Override
															public void done(ParseException arg0) {
																ParseObject notification =new ParseObject(Constants.KEY_NOTIFICATION_CLASS);
																notification.put(Constants.KEY_NOTIFICATION_MESSAGE, globalvariable.getCurrentUserFullName()+" has followed you back. Now you are friend with "+globalvariable.getCurrentUserFullName());
																notification.put(Constants.KEY_NOTIFICATION_RECIPIENTSID, user.getObjectId().toString());
																notification.put(Constants.KEY_NOTIFICATION_SENDERID, ParseUser.getCurrentUser().getObjectId().toString());
																notification.put(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC, globalvariable.getCurrentUserProfilePic());
																notification.put(Constants.KEY_NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_FOLLOW);
																notification.put(Constants.KEY_NOTIFICATION_READ, false);
																notification.saveInBackground(new SaveCallback() {
																	
																	@Override
																	public void done(ParseException e) {
																		if (e==null) {
																			sendPushNotifications(globalvariable.getCurrentUserFullName()+" has followed you back. Now you are friend with "+globalvariable.getCurrentUserFullName());
																		}
																	}
																});
																
																getcases();
															}
														});
														
													}
												});
												
											}
										});
										
										
										
									}
								});
							
							
						}
					});
					
				}else if (cases=="CaseD") {
					followngtext.setVisibility(View.VISIBLE);
					remBtn.setVisibility(View.VISIBLE);
					addBtn.setVisibility(View.GONE);
					ParseObject follower = new ParseObject(Constants.KEY_CLASS_FOLLOWERS);
					follower.put(Constants.KEY_FOLLOWERS_BETWEEN, AfollowingB);
					follower.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							
							ParseUser.getCurrentUser().increment(Constants.KEY_TOTAL_FOLLOWINGS);
							ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException arg0) {
									user.increment(Constants.KEY_TOTAL_FOLLOWERS);
									user.saveInBackground(new SaveCallback() {
										
										@Override
										public void done(ParseException arg0) {
											ParseObject notification =new ParseObject(Constants.KEY_NOTIFICATION_CLASS);
											notification.put(Constants.KEY_NOTIFICATION_MESSAGE, globalvariable.getCurrentUserFullName()+" is following you");
											notification.put(Constants.KEY_NOTIFICATION_RECIPIENTSID, user.getObjectId().toString());
											notification.put(Constants.KEY_NOTIFICATION_SENDERID, ParseUser.getCurrentUser().getObjectId().toString());
											notification.put(Constants.KEY_NOTIFICATION_SENDERPROFILEPIC, globalvariable.getCurrentUserProfilePic());
											notification.put(Constants.KEY_NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_FOLLOW);
											notification.put(Constants.KEY_NOTIFICATION_READ, false);
											notification.saveInBackground(new SaveCallback() {
												
												@Override
												public void done(ParseException e) {
													if (e==null) {
														sendPushNotifications(globalvariable.getCurrentUserFullName()+" is following you");
													}
												}
											});
											
											getcases();
										}
									});
									
								}
							});
							
							
						}
					});
				} else {
					//do nothing
				}
				
				
			}
		});
		
		remBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cases=="CaseC") {
					followngtext.setVisibility(View.GONE);
					remBtn.setVisibility(View.GONE);
					addBtn.setVisibility(View.VISIBLE);
					ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>(Constants.KEY_CLASS_FOLLOWERS);
					query1.whereEqualTo(Constants.KEY_FOLLOWERS_BETWEEN, AfollowingB);
					
					query1.findInBackground(new FindCallback<ParseObject>() {
						@Override
						public void done(List<ParseObject> followers, ParseException e) {
							for(ParseObject follower : followers) {
								follower.remove(Constants.KEY_FOLLOWERS_BETWEEN);
								follower.deleteInBackground(new DeleteCallback() {
									
									@Override
									public void done(ParseException arg0) {
										mFriendsRelation.remove(user);
										mCurrentUser.saveInBackground(new SaveCallback() {
											
											@Override
											public void done(ParseException arg0) {
												mOtherFriendsRelation.remove(ParseUser.getCurrentUser());
												mOtherUser.saveInBackground(new SaveCallback() {
													
													@Override
													public void done(ParseException arg0) {
														// TODO Auto-generated method stub
														ParseUser.getCurrentUser().increment(Constants.KEY_TOTAL_FOLLOWINGS, -1);
														ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
															
															@Override
															public void done(ParseException arg0) {
																user.increment(Constants.KEY_TOTAL_FOLLOWERS, -1);
																user.saveInBackground(new SaveCallback() {
																	
																	@Override
																	public void done(ParseException arg0) {
																		getcases();
																	}
																});
																
															}
														});
														
													}
												});
												
												
											}
										});
									}
								});
							}
						
						
						
						}});
					
					
				} else if (cases=="CaseA") {
					
					followngtext.setVisibility(View.GONE);
					remBtn.setVisibility(View.GONE);
					addBtn.setVisibility(View.VISIBLE);
					ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>(Constants.KEY_CLASS_FOLLOWERS);
					query1.whereEqualTo(Constants.KEY_FOLLOWERS_BETWEEN, AfollowingB);
					
					query1.findInBackground(new FindCallback<ParseObject>() {
						@Override
						public void done(List<ParseObject> followers, ParseException e) {
							for(ParseObject follower : followers) {
								follower.remove(Constants.KEY_FOLLOWERS_BETWEEN);
								follower.deleteInBackground(new DeleteCallback() {
									
									@Override
									public void done(ParseException arg0) {
										ParseUser.getCurrentUser().increment(Constants.KEY_TOTAL_FOLLOWINGS, -1);
										ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
											
											@Override
											public void done(ParseException arg0) {
												user.increment(Constants.KEY_TOTAL_FOLLOWERS, -1);
												user.saveInBackground(new SaveCallback() {
													
													@Override
													public void done(ParseException arg0) {
														getcases();
													}
												});
												
											}
										});
										
									}
								});
							}
						
						
						
						}});
				} else {
					//do nothing
				}
				
			}
		});
		}
		refresh();
	
	}
	protected void sendPushNotifications(String msg){
		ParseQuery<ParseInstallation> query=ParseInstallation.getQuery();
		query.whereEqualTo(Constants.KEY_USER_ID, user.getObjectId());
		
		//send notification
		ParsePush push=new ParsePush();
		push.setQuery(query);
		push.setMessage(msg);
		push.sendInBackground();
		
		
	}
	private void getcases() {
		
		String user1Id=ParseUser.getCurrentUser().getObjectId().toString();
		String user2Id=user.getObjectId().toString();
		
		final String AtoBFollower=user1Id+user2Id;
		final String BtoAFollower=user2Id+user1Id;
		Log.e(TAG2, user1Id);
		Log.e(TAG2, user2Id);
		Log.e(TAG2, AtoBFollower);
		Log.e(TAG2, BtoAFollower);
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>(Constants.KEY_CLASS_FOLLOWERS);
		query1.whereEqualTo(Constants.KEY_FOLLOWERS_BETWEEN, AtoBFollower);
		query1.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> follow, ParseException e) {
				
				
				if (e == null) {
				       if(follow!=null && !follow.isEmpty()){
				          ParseObject review = follow.get(0);
				          if (review == null) {
				        	  Log.e(TAG2, "A is not following B");
								AtoB=false;
				             } else {
				            	 Log.e(TAG2, "A is following B");
			    					AtoB=true;
				             }
				          } else { 
				        	  Log.e(TAG2, "A is not following B");
								AtoB=false;
				                 }
				       } else {
				    	   Log.e(TAG2, "A is not following B");
							AtoB=false;
				     }
				
				
				
				
				
				
				ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>(Constants.KEY_CLASS_FOLLOWERS);
				query2.whereEqualTo(Constants.KEY_FOLLOWERS_BETWEEN, BtoAFollower);
				
				query2.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> follow, ParseException e) {

						if (e == null) {
						       if(follow!=null && !follow.isEmpty()){
						          ParseObject review = follow.get(0);
						          if (review == null) {
						        	  Log.e(TAG2, "B is not following A");
										BtoA=false;
						             } else {
						            	 Log.e(TAG2, "B is following A");
						            	 BtoA=true;
						             }
						          } else { 
						        	  Log.e(TAG2, "B is not following A");
						        	  BtoA=false;
						                 }
						       } else {
						    	   Log.e(TAG2, "B is not following A");
						    	   BtoA=false;
						     }
						
						
						if (AtoB && BtoA) {
							cases="CaseC";
							followngtext.setVisibility(View.VISIBLE);
							remBtn.setVisibility(View.VISIBLE);
							addBtn.setVisibility(View.GONE);
							Log.e(TAG2, "Friends");
						} else if (AtoB && !(BtoA)) {
							cases="CaseA";
							followngtext.setVisibility(View.VISIBLE);
							remBtn.setVisibility(View.VISIBLE);
							addBtn.setVisibility(View.GONE);
							Log.e(TAG2, "A is following B and B is not following A");
						} else if (!(AtoB) && (BtoA)) {
							cases="CaseB";
							followngtext.setVisibility(View.GONE);
							remBtn.setVisibility(View.GONE);
							addBtn.setVisibility(View.VISIBLE);
							Log.e(TAG2, "B is following A and A is not following B");
						} else if (!AtoB && !BtoA) {
							cases="CaseD";
							followngtext.setVisibility(View.GONE);
							remBtn.setVisibility(View.GONE);
							addBtn.setVisibility(View.VISIBLE);
							Log.e(TAG2, "B is not following A & A is not following B");
						} else {
							Log.e(TAG2, "What the hell");
						}
						
					}
				}); 
				
			}
		}); 
		
		
		
	}
	private void refresh() {
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Constants.CLASS_MESSAGES);
		query.whereEqualTo(Constants.KEY_SENDER_ID, user.getObjectId());
		query.addDescendingOrder(Constants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				
				
				pb.setVisibility(View.GONE);
				if (e == null) {
					// We found messages!
					if (messages!=null && !messages.isEmpty()){
						if (feedItems!=null){
						feedItems.clear();}
					mMessages = messages;

					String[] usernames = new String[mMessages.size()];
					Uri[] profUri =new Uri[mMessages.size()];
					Uri[] imgUri = new Uri[mMessages.size()];
					String[] status=new String[mMessages.size()];
					long[] timestamp=new long[mMessages.size()];
					
					int i = 0;
					for(ParseObject message : mMessages) {
						
						usernames[i] = message.getString(Constants.KEY_SENDER_NAME);
						String type=message.getString(Constants.KEY_FILE_TYPE);
						if (!type.equalsIgnoreCase(Constants.TYPE_TEXT)) {
						ParseFile file = message.getParseFile(Constants.KEY_FILE);
						imgUri[i] = Uri.parse(file.getUrl());
						} else {
							imgUri[i]=null;
						}
						status[i]=message.getString(Constants.KEY_MESSAGE2);
						profUri[i]=Uri.parse((String) message.get(Constants.KEY_MESSAGE));
						
						Date date = message.getCreatedAt();
						Format formatter = new SimpleDateFormat("ddMMyyyyHHmm");
						
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						timestamp[i]=cal.getTimeInMillis();
						
						
						
						
						
						FeedItem item=new FeedItem();
						item.setMsg(message);
						item.setName(usernames[i]);
						item.setTimeStamp(timestamp[i]);
						if (!type.equalsIgnoreCase(Constants.TYPE_TEXT)) {
						item.setImguri(imgUri[i].toString());}
						else {
							item.setImguri(null);
						}
						item.setMessage(status[i]);
						item.setProfuri(profUri[i].toString());
						feedItems.add(item);
						i++;
					}
					
						
					
				} 

				listAdapter = new FeedListAdapter(ShowProfile.this, feedItems);
				
				list.setAdapter(listAdapter);
				}else {
					noPost.setVisibility(noPost.VISIBLE);}
			} 
			
		}); 
	}
	
}
