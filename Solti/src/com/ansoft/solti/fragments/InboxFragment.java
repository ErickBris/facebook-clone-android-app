package com.ansoft.solti.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.RecipientsActivity;
import com.ansoft.solti.R.array;
import com.ansoft.solti.R.color;
import com.ansoft.solti.R.id;
import com.ansoft.solti.R.layout;
import com.ansoft.solti.R.string;
import com.ansoft.solti.adapter.FeedListAdapter;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.data.FeedItem;
import com.ansoft.solti.ui.FileHelper;

import com.google.android.gms.appindexing.AppIndexApi.ActionResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class InboxFragment extends ListFragment  {

	protected ProgressBar pb;
	public static final String TAG = "Username";
	protected List<ParseObject> mMessages;
	protected List<FeedItem> feedItems;
	private FeedListAdapter listAdapter;
	EditText editStatus;
	protected Button writeBtn;
	protected SwipeRefreshLayout swipe;
	protected int numOfLike=0;
	protected int numofDislike=0;
	protected String mProfilePc=null;
	protected String mMessage;
	protected String mFileType;
	protected Button photoBtn;
	ListView list;
	LinearLayout ln;
	protected Uri mMediaUri;
	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int TAKE_VIDEO_REQUEST = 1;
	public static final int PICK_PHOTO_REQUEST = 2;
	public static final int PICK_VIDEO_REQUEST = 3;
	public static String own_number="";
	public static final int MEDIA_TYPE_IMAGE = 4;
	public static final int MEDIA_TYPE_VIDEO = 5;
	public static boolean CAMERA_BUTTON_CLICKED=false;
	public static final int FILE_SIZE_LIMIT = 1024*1024*10; 
	
	
	
	protected DialogInterface.OnClickListener mDialogListener = 
			new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
				case 0: 
					Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					if (mMediaUri == null) {
						
						Toast.makeText(getActivity(), R.string.error_external_storage,
								Toast.LENGTH_LONG).show();
					}
					else {
						takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
						startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
					}
					break;
				case 1: 
					Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
					if (mMediaUri == null) {
								Toast.makeText(getActivity(), R.string.error_external_storage,
								Toast.LENGTH_LONG).show();
					}
					else {
						videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
						videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
						videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 = lowest res
						startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
					}
					break;
				case 2: // Choose picture
					Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
					choosePhotoIntent.setType("image/*");
					startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
					break;
				case 3: // Choose video
					Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
					chooseVideoIntent.setType("video/*");
					Toast.makeText(getActivity(), R.string.video_file_size_warning, Toast.LENGTH_LONG).show();
					startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
					break;
			}
		}

		private Uri getOutputMediaFileUri(int mediaType) {
			
			if (isExternalStorageAvailable()) {
				
				String appName = getActivity().getString(R.string.app_name);
				File mediaStorageDir = new File(
						Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						appName);
				
				
				if (! mediaStorageDir.exists()) {
					if (! mediaStorageDir.mkdirs()) {
						Log.e(TAG, "Failed to create directory.");
						return null;
					}
				}
				
				File mediaFile;
				Date now = new Date();
				String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
				
				String path = mediaStorageDir.getPath() + File.separator;
				if (mediaType == MEDIA_TYPE_IMAGE) {
					mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
				}
				else if (mediaType == MEDIA_TYPE_VIDEO) {
					mediaFile = new File(path + "VID_" + timestamp + ".mp4");
				}
				else {
					return null;
				}
				
				Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
				
				// 5. Return the file's URI				
				return Uri.fromFile(mediaFile);
			}
			else {
				return null;
			}
		}
		
		private boolean isExternalStorageAvailable() {
			String state = Environment.getExternalStorageState();
			
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			}
			else {
				return false;
			}
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		if (resultCode == getActivity().RESULT_OK) {			
			if (requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST) {
				if (data == null) {
					Toast.makeText(getActivity(), getString(R.string.general_error), Toast.LENGTH_LONG).show();
				}
				else {
					mMediaUri = data.getData();
				}
				
				Log.i(TAG, "Media URI: " + mMediaUri);
				if (requestCode == PICK_VIDEO_REQUEST) {
					// make sure the file is less than 10 MB
					int fileSize = 0;
					InputStream inputStream = null;
					
					try {
						inputStream = getActivity().getContentResolver().openInputStream(mMediaUri);
						fileSize = inputStream.available();
					}
					catch (FileNotFoundException e) {
						Toast.makeText(getActivity(), R.string.error_opening_file, Toast.LENGTH_LONG).show();
						return;
					}
					catch (IOException e) {
						Toast.makeText(getActivity(), R.string.error_opening_file, Toast.LENGTH_LONG).show();
						return;
					}
					finally {
						try {
							inputStream.close();
						} catch (IOException e) { /* Intentionally blank */ }
					}
					
					if (fileSize >= FILE_SIZE_LIMIT) {
						Toast.makeText(getActivity(), R.string.error_file_size_too_large, Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
			else {
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				mediaScanIntent.setData(mMediaUri);
				getActivity().sendBroadcast(mediaScanIntent);
			}
			
			Intent recipientsIntent = new Intent(getActivity(), RecipientsActivity.class);
			recipientsIntent.setData(mMediaUri);
			
			String fileType;
			if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST) {
				fileType = Constants.TYPE_IMAGE;
			}
			else {
				fileType = Constants.TYPE_VIDEO;
			}
			
			recipientsIntent.putExtra(Constants.KEY_FILE_TYPE, fileType);
			startActivity(recipientsIntent);
		}
		else if (resultCode != getActivity().RESULT_CANCELED) {
			Toast.makeText(getActivity(), R.string.general_error, Toast.LENGTH_LONG).show();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		globalvariable.initialize();
		View rootView = inflater.inflate(R.layout.fragment_inbox,
				container, false);
		pb=(ProgressBar) rootView.findViewById(R.id.pb);
		
		writeBtn=(Button)rootView.findViewById(R.id.WriteBtn);
		list=(ListView) rootView.findViewById(android.R.id.list);
		photoBtn=(Button) rootView.findViewById(R.id.PhotoBtn);
		
		swipe=(SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
		swipe.setOnRefreshListener(mrl);
		swipe.setColorScheme(R.color.swipe1, R.color.swipe2, R.color.swipe3, R.color.swipe4);
		
		pb.setVisibility(View.VISIBLE);
		photoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setItems(R.array.camera_choices, mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		writeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				AlertDialog alert=builder.create();
				alert.setTitle("Write Something");
				final EditText status=new EditText(getActivity());
				status.setHint("What's On Your Mind");
				alert.setView(status);
				alert.setButton("Publish", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						List<String> hashtags=new ArrayList<String>();
						ArrayList<String> mComments=new ArrayList<String>();
						mMessage=status.getText().toString();
						if (mMessage.toLowerCase().contains("#")) {
							String str=mMessage;
							Pattern MY_PATTERN = Pattern.compile("#(\\w+|\\W+)");
							Matcher mat = MY_PATTERN.matcher(str);
							
							while (mat.find()) {
							  //System.out.println(mat.group(1));
							  hashtags.add(mat.group(1));
							}
							
						}
						mProfilePc=globalvariable.getCurrentUserProfilePic();
						mFileType=Constants.TYPE_TEXT;
						hashtags.add("");
						mComments.add("");
						ParseObject message = createMessage(hashtags, mComments);
						if (message == null) {
							
						}
						else {
							
							ProgressDialog di=ProgressDialog.show(getActivity(), "", "Posting... Please Wait");
							send(message, di);
						}
					}
				});
				alert.show();
			}
		});
		
		refresh();
		
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		feedItems = new ArrayList<FeedItem>();
		
	
	}
	
	protected ParseObject createMessage(List<String> hashtags, ArrayList<String> mComments) {
		ParseObject message = new ParseObject(Constants.CLASS_MESSAGES);
		message.put(Constants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(Constants.KEY_SENDER_NAME, ParseUser.getCurrentUser().get("firstname").toString()+" "+ParseUser.getCurrentUser().get("lastname").toString());
		message.put(Constants.KEY_RECIPIENT_IDS, getRecipientIds());
		message.put(Constants.KEY_TOTAL_LIKE, numOfLike);
		message.put(Constants.KEY_TOTAL_DISLIKE, numofDislike);
		message.put(Constants.KEY_LIKER_IDS, getLikerIds());
		message.put(Constants.KEY_DISLIKER_IDS, getDislikerIds());
		message.put(Constants.KEY_STATUS_TYPE, Constants.KEY_ORIGINAL_STATUS);
		message.put(Constants.KEY_COMMENTS, mComments);
		message.put(Constants.KEY_TOTAL_COMMENTS, 0);
		message.put(Constants.KEY_FILE_TYPE, mFileType);
		message.put(Constants.KEY_MESSAGE2, mMessage);
		message.put(Constants.KEY_MESSAGE, mProfilePc);
		message.put(Constants.KEY_SHARE_MESSAGE, "");
		message.put(Constants.KEY_TAGS, hashtags);
		message.put(Constants.KEY_SHARER_NAME, "");
		

			return message;
		
			
			
	}
	
	
	private void refresh() {
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(Constants.CLASS_MESSAGES);
		/*
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());*/
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

				listAdapter = new FeedListAdapter(getActivity(), feedItems);
				
				
				
				
				setListAdapter(listAdapter);
			
			}
		}); 
	}
	
	/*
	@Override
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject message = mMessages.get(position);
		String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
		ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
		Uri fileUri = Uri.parse(file.getUrl());
		
		if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
			// view the image
			Intent intent = new Intent(getActivity(), ViewImageActivity.class);
			intent.setData(fileUri);
			startActivity(intent);
		}
		else {
			// view the video
			Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
			intent.setDataAndType(fileUri, "video/*");
			startActivity(intent);
		}
		
		// Delete it!
		List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
		
		if (ids.size() == 1) {
			// last recipient - delete the whole thing!
			message.deleteInBackground();
		}
		else {
			// remove the recipient and save
			ids.remove(ParseUser.getCurrentUser().getObjectId());
			
			ArrayList<String> idsToRemove = new ArrayList<String>();
			idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
			
			message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
			message.saveInBackground();
		}
	}*/
	
	

	protected ArrayList<String> getLikerIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		recipientIds.add("");
		
		return recipientIds;
	}
	
	protected ArrayList<String> getDislikerIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		recipientIds.add("");
		
		return recipientIds;
	}
	
	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		recipientIds.add(ParseUser.getCurrentUser().getObjectId());
		
		return recipientIds;
	}
	protected OnRefreshListener mrl=new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			refresh();
			
		}
	};
	protected void send(ParseObject message, final ProgressDialog pd) {
		
		message.saveInBackground(new SaveCallback() {
			
			
			@Override
			public void done(ParseException e) {
				pd.dismiss();
				if (e == null) {
					// success!
					
					getActivity().setProgressBarIndeterminateVisibility(false);
					Toast.makeText(getActivity(), "Posted", Toast.LENGTH_LONG).show();
					
				}
				else {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(R.string.error_sending_message)
						.setTitle(R.string.error_selecting_file_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
					
				}
			}
		});
	}
}








