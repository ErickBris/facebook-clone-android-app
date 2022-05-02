package com.ansoft.solti.util;

import android.app.AlertDialog;
import android.content.Context;

public class ErrorDialog {
	public void Show(Context context, int msg) {
	
		String message=null;
		switch (msg) {
		case 1:
			message="Error Connecting to Internet";
			break;
			
		case 2:
				message="Error in connection";	
					break;
		case 3:
			message="Username not available\nChoose another username";
			break;
		case 4:
			
			break;

		
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		AlertDialog dialog=builder.create();
		dialog.show();
	
	}
}
