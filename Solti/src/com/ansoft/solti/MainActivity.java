package com.ansoft.solti;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.util.SectionsPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends FragmentActivity implements OnClickListener , ActionBar.TabListener  {
	
	public static final String TAG = MainActivity.class.getSimpleName();
	
	public static final String TAG2 = "total contact";
	Context mcontext=MainActivity.this;
	
	public static String own_number="";
	
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
			super.onCreate(savedInstanceState);
		
		
		getActionBar().setDisplayShowHomeEnabled(false);              
		getActionBar().setDisplayShowTitleEnabled(false);
		
		
		setContentView(R.layout.activity_main);
		ConnectivityManager wifimgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = wifimgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		ConnectivityManager mobilemgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = mobilemgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (wifi.isConnected()||mobile.isConnected()) {
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			navigateToLogin();
		}
		else {
			getcon();
			globalvariable.initialize();
			Log.i(TAG, currentUser.getUsername());
			globalvariable.setuser(currentUser);
			own_number=(String) currentUser.get("phnumber");
			globalvariable.setph(own_number);
			Log.i(TAG2, globalvariable.getprof()+"hello");
			
		}

		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayUseLogoEnabled(false);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(this, 
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			
			
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i)).setIcon(mSectionsPagerAdapter.getPageIcon(i))
					
					.setTabListener(this));
		}} else {
			
			Intent intent = new Intent(this, No_connection.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			finish();
			
			startActivity(intent);
			/*
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage("You are not conected to internet. Connect with network and try again")
				.setTitle("No connecton");
			AlertDialog dialog = builder.create();
			dialog.show(); */
		}
	}
	
	

	private void navigateToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		finish();
		
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		switch(itemId) {
			case R.id.action_logout:
				ParseUser.logOut();
				navigateToLogin();
				break;
			case R.id.action_edit_friends:
				Intent intent = new Intent(this, EditFriendsActivity.class);
				
				startActivity(intent);
				break;
				
			case R.id.action_edit_profile:
				Intent intent_edit_profile = new Intent(this, EditProfile.class);
				
				startActivity(intent_edit_profile);
				break;
				
			case R.id.action_myprofile:
				Intent intent_my_profile = new Intent(this, ShowProfile.class);
				globalvariable.setuser(ParseUser.getCurrentUser());
				startActivity(intent_my_profile);
				break;
			
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	public void getcon() {
		
		
		Cursor cursor = null;
		cursor = mcontext.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
		String phcon[]=new String[cursor.getCount()];
	    try {
	    	
	        
	        cursor.getColumnIndex(BaseColumns._ID);
	        
	        cursor.getColumnIndex(Phone.DISPLAY_NAME);
	        int phoneNumberIdx = cursor.getColumnIndex(Phone.NUMBER);
	        cursor.getColumnIndex(Phone.PHOTO_ID);
	        cursor.moveToFirst();
	        int i=0;
	        do {
	          
	            phcon[i] = cursor.getString(phoneNumberIdx);
	            
	            i++;
	            
	            //...
	        } while (cursor.moveToNext());  
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (cursor != null) {
	            cursor.close();
	            globalvariable.setContactlist(phcon);
	            
	            globalvariable.erasenull();
	            for ( int i=0; i<phcon.length; i++){
	            	
		            Log.e(TAG2, "Phnumber ="+globalvariable.contactlist[i]);
		            }
	        }
	    }
	}

	@Override
	public void onClick(View v) {
		int pos=v.getId();
		switch(pos) {
		case R.id.PhotoBtn:
			
		}
		
	}
}
