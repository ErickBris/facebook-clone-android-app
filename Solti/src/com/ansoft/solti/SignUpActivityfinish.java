package com.ansoft.solti;

import com.ansoft.solti.R;
import com.ansoft.solti.util.GpsTracer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivityfinish extends Activity {

	protected EditText mPassword;
	protected EditText mEmail;
	protected EditText mFN;
	protected EditText mLN;
	protected EditText mConfirmPassword;
	protected Button mSignUpButton;
	protected String latitude;
	protected String longitude;

	public static final String TAG = "Location";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_activityfinish);
		GpsTracer gps = new GpsTracer(SignUpActivityfinish.this);
		double lat = gps.getLatitude();
		double lon = gps.getLongitude();

		latitude = "" + lat;
		longitude = "" + lon;
		Log.e(TAG, latitude + " " + longitude);

		mPassword = (EditText) findViewById(R.id.passwordField);
		mConfirmPassword = (EditText) findViewById(R.id.confirmpasswordField);
		mEmail = (EditText) findViewById(R.id.emailField);
		mFN = (EditText) findViewById(R.id.fnField);
		mLN = (EditText) findViewById(R.id.lnField);
		mSignUpButton = (Button) findViewById(R.id.signupButton);

		mSignUpButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String password = mPassword.getText().toString();
				String email = mEmail.getText().toString();
				String firstname = mFN.getText().toString();
				String lastname = mLN.getText().toString();
				String confirmPassword = mConfirmPassword.getText().toString();
				Intent finishSignup = new Intent(SignUpActivityfinish.this,
						SignUpActivity.class);

				if ((!password.isEmpty()) && (!email.isEmpty())
						&& (!firstname.isEmpty()) && (!lastname.isEmpty())
						&& (!confirmPassword.isEmpty())) {
					// check if both password are correct
					if (password.equals(confirmPassword)) {
						// check password length
						if (password.length() > 6) {

							finishSignup.putExtra("password", password);
							finishSignup.putExtra("email", email);
							finishSignup.putExtra("firstname", firstname);
							finishSignup.putExtra("lastname", lastname);
							finishSignup.putExtra("latitude", latitude);
							finishSignup.putExtra("longitude", longitude);
							startActivity(finishSignup);
						} else {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									SignUpActivityfinish.this);
							builder.setMessage(
									"Password must be 6 character long")
									.setTitle("Password length")
									.setPositiveButton(android.R.string.ok,
											null);
							AlertDialog dialog = builder.create();
							dialog.show();

						}
						//
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								SignUpActivityfinish.this);
						builder.setMessage(
								R.string.confirm_password_error_message)
								.setTitle(
										R.string.confirm_password_error_message_title)
								.setPositiveButton(android.R.string.ok, null);
						AlertDialog dialog = builder.create();
						dialog.show();

					}
					//
				}

				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SignUpActivityfinish.this);
					builder.setMessage(R.string.signup_error_message)
							.setTitle(R.string.signup_error_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}

		});
	}

}
