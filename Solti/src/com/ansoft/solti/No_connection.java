package com.ansoft.solti;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class No_connection extends Activity {

	Button retry;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_connection);
		final ActionBar actionBar = getActionBar();
		
		actionBar.hide();
		retry=(Button)findViewById(R.id.retryButton);
		retry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ProgressDialog pd=ProgressDialog.show(No_connection.this, "", "Checking network connection");
				ConnectivityManager wifimgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifi = wifimgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				
				ConnectivityManager mobilemgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobile = mobilemgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				
				if (wifi.isConnected()||mobile.isConnected()) {
					pd.dismiss();
					Intent intent = new Intent(No_connection.this, MainActivity
							.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					finish();
					
					startActivity(intent);
				} else {
					pd.dismiss();
					Toast.makeText(No_connection.this, "No connection\nTry again later", Toast.LENGTH_LONG);
				}
			}
		});
	}

}
