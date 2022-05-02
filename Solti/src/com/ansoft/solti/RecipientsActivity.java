package com.ansoft.solti;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.ui.FileHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

public class RecipientsActivity extends Activity {

	public static final String TAG = RecipientsActivity.class.getSimpleName();

	
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;	
	protected List<ParseUser> mFriends;	
	protected MenuItem mSendMenuItem;
	protected Uri mMediaUri;
	protected String profile;
	protected String mFileType;
	protected ArrayList<String> mComments=new ArrayList<String>();
	protected String mMessage;
	protected EditText Message;
	protected Button sendBtn;
	protected int progess=0;
	protected int numOfLike=0;
	protected int numofDislike=0;
	protected int numOfComments=0;
	protected String mProfilePc=null;
	protected List<String> hashtags=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recipients);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Message=(EditText) findViewById(R.id.editMessage);
		sendBtn=(Button) findViewById(R.id.sendBtn);
		
		
		
		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString(Constants.KEY_FILE_TYPE);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mProfilePc=globalvariable.getCurrentUserProfilePic();
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(Constants.KEY_FRIENDS_RELATION);
		
		setProgressBarIndeterminateVisibility(true);
		sendBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mMessage=Message.getText().toString();
				if (mMessage.toLowerCase().contains("#")) {
					String str=mMessage;
					Pattern MY_PATTERN = Pattern.compile("#(\\w+|\\W+)");
					Matcher mat = MY_PATTERN.matcher(str);
					
					while (mat.find()) {
					  //System.out.println(mat.group(1));
					  hashtags.add(mat.group(1));
					}
					
				}
				
				ParseObject message = createMessage();
				if (message == null) {
					
				}
				else {
					send(message);
					ProgressDialog di=ProgressDialog.show(RecipientsActivity.this, "", "Posting... Please Wait");
				}
				
			}
		});
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
		mSendMenuItem = menu.getItem(0);
		return true;
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
		case R.id.action_send:
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	protected ParseObject createMessage() {
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
		message.put(Constants.KEY_TOTAL_COMMENTS, numOfComments);
		message.put(Constants.KEY_FILE_TYPE, mFileType);
		message.put(Constants.KEY_MESSAGE2, mMessage);
		message.put(Constants.KEY_MESSAGE, mProfilePc);
		message.put(Constants.KEY_SHARE_MESSAGE, "");
		message.put(Constants.KEY_TAGS, hashtags);
		message.put(Constants.KEY_SHARER_NAME, "");
		byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
		if (fileBytes == null ) {
			return null;
		}
		else {
			if (mFileType.equals(Constants.TYPE_IMAGE)) {
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);
			}
			
			String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
			ParseFile file = new ParseFile(fileName, fileBytes);
			message.put(Constants.KEY_FILE, file);

			return message;
		}
	}
	
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
		final ArrayList<String> recipientIds = new ArrayList<String>();
		recipientIds.add(ParseUser.getCurrentUser().getObjectId());
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(Constants.KEY_USERNAME);
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				
				if (e == null) {
					mFriends = friends;
					
					String[] usernames = new String[mFriends.size()];
					int i = 0;
					for(ParseUser user : mFriends) {
						usernames[i] = user.getUsername();
						recipientIds.add(usernames[i]);
						i++;
					}
					
				}
				else {
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
					builder.setMessage(e.getMessage())
						.setTitle(R.string.error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
		return recipientIds;
	}
	
	protected void send(ParseObject message) {
		
		message.saveInBackground(new SaveCallback() {
			
			
			@Override
			public void done(ParseException e) {
				if (e == null) {
					// success!
					
					Toast.makeText(RecipientsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
					finish();
				}
				else {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
					builder.setMessage(R.string.error_sending_message)
						.setTitle(R.string.error_selecting_file_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
					finish();
				}
			}
		}
		
				);
	}
}






