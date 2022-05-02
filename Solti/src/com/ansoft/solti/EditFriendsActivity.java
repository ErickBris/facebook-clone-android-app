package com.ansoft.solti;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class EditFriendsActivity extends ListActivity {
	
	
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected String searchString;
	public static final String TAG = EditFriendsActivity.class.getSimpleName();
	public static final String TAG2 = "check check";
	protected List<ParseUser> mUsers;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_edit_friends);
		// Show the Up button in the action bar.
		setupActionBar();
		
		getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		handleIntent(getIntent());
		
	}
	
	private void handleIntent(Intent intent) {
		// TODO Auto-generated method stub
		 if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            searchString = intent.getStringExtra(SearchManager.QUERY);
	            //use the query to search your data somehow
	        }
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(Constants.KEY_FRIENDS_RELATION);
		
		setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		if (searchString!=null) {
		query.whereContains(Constants.KEY_FIRST_NAME, searchString);}
		query.orderByAscending(Constants.KEY_USERNAME);
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				
				if (e == null) {
					// Success
					mUsers = users;
					String[] userinfo = new String[mUsers.size()];
					ParseUser[] puser=new ParseUser[mUsers.size()];
					int i = 0;
					
					for(ParseUser user : mUsers) {
						
							
							
						userinfo[i] = (String)user.get("firstname")+" "+(String)user.get("lastname")+"\n@"+(String) user.get(Constants.KEY_USERNAME);
						
						puser[i]=user;
						
						 i++;
					}
					
					/*
					FriendAdapter imgadapter = new FriendAdapter(EditFriendsActivity.this, R.layout.listview_item_row, data);
					
					View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
					listView1.addHeaderView(header);
					listView1.setAdapter(imgadapter);
					*/
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this, android.R.layout.simple_list_item_1,userinfo);
					setListAdapter(adapter); 
					
					
					
				}
				else {
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
					builder.setMessage(e.getMessage())
						.setTitle(R.string.error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		globalvariable.setuser(mUsers.get(position));
		startActivity(new Intent(EditFriendsActivity.this, ShowProfile.class));
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);

	    SearchManager searchManager =
	            (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	     SearchView searchView =
	             (SearchView) menu.findItem(R.id.search).getActionView();
	     searchView.setSearchableInfo(
	             searchManager.getSearchableInfo(getComponentName()));
	    return true;
	}

}







