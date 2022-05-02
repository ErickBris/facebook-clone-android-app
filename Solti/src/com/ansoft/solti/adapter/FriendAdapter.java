package com.ansoft.solti.adapter;

import com.ansoft.solti.R;
import com.ansoft.solti.R.id;
import com.ansoft.solti.constants.globalvariable;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<Friend>{

    Context context; 
    int layoutResourceId;    
    Friend data[] = null;
    
    public FriendAdapter(Context context, int layoutResourceId, Friend[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new WeatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
        
        Friend weather = data[position];
        holder.txtTitle.setText(weather.title);
        
        
        
        String id=weather.puser.getObjectId();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("dp");
		
		
		query.getInBackground(id,new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject object,ParseException e) {
						
						ParseFile fileObject = (ParseFile) object.get("photo.jpg");
						fileObject.getDataInBackground(new GetDataCallback() {
							
									@Override
									public void done(byte[] data, ParseException e) {
										
										Bitmap img = BitmapFactory.decodeByteArray(data, 0,data.length);
										globalvariable.setBmp(img);
										if (e == null) {
											Log.d("test","We've got data in data.");
											
											
										} else {
											Log.d("test","There was a problem downloading the data.");
										}
										}

									

								});
					}
				});
        
        
        
        holder.imgIcon.setImageBitmap(globalvariable.getBmp());
        
        
        
        return row;
    }
    
    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
