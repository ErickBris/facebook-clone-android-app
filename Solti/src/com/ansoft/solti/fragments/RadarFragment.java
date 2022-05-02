package com.ansoft.solti.fragments;


import java.util.List;

import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.Solti;
import com.ansoft.solti.ShowProfile;
import com.ansoft.solti.R.id;
import com.ansoft.solti.R.layout;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.constants.globalvariable;
import com.ansoft.solti.util.GpsTracer;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class RadarFragment extends Fragment {

	private static String TAG="map";
	LatLng mylocation;
	String gender, seeker, mystatus, email;
	SharedPreferences pref;
	View markerView;
	MapFragment mapfragment;
	ImageView pickerImage;
	String[] seeker_username, seeker_image, seeker_lng, seeker_lat, seeker_sex;
	String lat, lng;
	private GoogleMap map;
	int seeker_Num ,num;
	boolean flag_search;
	protected List<ParseUser> mUsers;
	View rootView;
	
	ProgressDialog dialog;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		 if (rootView != null) {
			    ViewGroup parent = (ViewGroup) rootView.getParent();
			    if (parent != null) {
			        parent.removeView(rootView);
			    }
			}
			try {
				rootView = inflater.inflate(R.layout.activity_radar_fragment, container, false);
			} catch (InflateException e) {}

			GpsTracer gps = new GpsTracer(getActivity());
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			mylocation = new LatLng(latitude, longitude);
			lat = "" + latitude;
			lng = "" + longitude;
			Log.e(TAG, "location lat = "+lat+" long = "+lng);
			int seeker_Num=0, num = 0;
			
			
       
		
		return rootView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (seeker_username==null){
			getusers();
			}
	}


	private void getusers() {
		getActivity().setProgressBarIndeterminateVisibility(true);
		dialog=new ProgressDialog(getActivity());
		dialog.setMessage("Processing...");
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.orderByAscending(Constants.KEY_USERNAME);
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				
				getActivity().setProgressBarIndeterminateVisibility(false);
				if (e==null) {
					mUsers = users;
					seeker_username = new String[mUsers.size()];
					
			        seeker_image = new String[mUsers.size()];
			        seeker_lng = new String[mUsers.size()];
			        seeker_lat = new String[mUsers.size()];
					
					seeker_sex=new String[mUsers.size()];
					for (ParseUser user :mUsers) {
						seeker_username[seeker_Num]=user.getUsername().toString();
						ParseFile file = user.getParseFile(Constants.USER_PHOTO);
						Uri img=Uri.parse(file.getUrl());
						
						seeker_image[seeker_Num]=img.toString();
						seeker_lat[seeker_Num]=user.getString(Constants.KEY_LATITUDE);
						
						seeker_lng[seeker_Num]=user.getString(Constants.KEY_LONGITUDE);
						seeker_sex[seeker_Num]=user.getString(Constants.KEY_SEX);
						
						Log.e(TAG, "image "+seeker_image[seeker_Num]);
						if(seeker_lat[seeker_Num].equals("")){
							seeker_lat[seeker_Num] = "0";
						}
						if(seeker_lng[seeker_Num].equals("")){
							seeker_lng[seeker_Num] = "0";
						}
						seeker_Num++;
					}
					Log.e(TAG, "data obtained");
					display_onMap();
				}else {
					
				}
				
				
			}
		});
		
	}


	protected void display_onMap() {
		map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		if (map!=null) {
			Log.e(TAG, "map created");
		}
		
		LayoutInflater inflater= (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i = 0; i < seeker_Num; i ++){
			LatLng position = new LatLng(Float.parseFloat(seeker_lat[i]), Float.parseFloat(seeker_lng[i]));
			Log.e(TAG, seeker_sex[i]);
			if (seeker_sex[i].equalsIgnoreCase(Constants.KEY_SEX_MALE)) {
				markerView = inflater.inflate(R.layout.picker_male, null);
				pickerImage=(ImageView) markerView.findViewById(R.id.img_seeker1);
				
				Picasso.with(getActivity()).load(seeker_image[i]).into(pickerImage);
			} else if (seeker_sex[i].equalsIgnoreCase(Constants.KEY_SEX_FEMALE)) {
				markerView = inflater.inflate(R.layout.picker_female, null);
				pickerImage=(ImageView) markerView.findViewById(R.id.img_seeker2);
				Solti.LoadImg(pickerImage, getActivity(), seeker_image[i]);
				
			}
			
			
			
			
			
			Marker friend = map.addMarker(new MarkerOptions()
            .position(position)
            .title(seeker_username[i])
            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(markerView))));
			friend.showInfoWindow();
			Log.e(TAG, "mark created");
			
		}
		dialog.dismiss();
		
			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker position) {
					Toast.makeText(getActivity(), position.getTitle(), Toast.LENGTH_SHORT);
					
					ParseQuery<ParseUser> query = ParseUser.getQuery();
					
					query.whereEqualTo(Constants.KEY_USERNAME, position.getTitle());
					
					query.findInBackground(new FindCallback<ParseUser>() {
						ProgressDialog dialog=ProgressDialog.show(getActivity(), "", "");
						@Override
						public void done(List<ParseUser> users, ParseException e) {
							List<ParseUser> mUsers=users;
							dialog.dismiss();
							if (e==null) {
								ParseUser[] user=new ParseUser[mUsers.size()];
								globalvariable.setuser(mUsers.get(0));
								startActivity(new Intent(getActivity(), ShowProfile.class));
							}
							
						}});
					
					
					
					return false;
				}
			});
		
		
		// Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 10));
        map.getUiSettings().setZoomControlsEnabled(true);
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		
        
	}
	
	private Bitmap createDrawableFromView(View view) {
			DisplayMetrics displayMetrics = new DisplayMetrics();
		    
		    view.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		    view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		    view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		    view.buildDrawingCache();
		    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
		    view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmap);
		    view.draw(canvas);
		    return bitmap;
		}
}


	
