package com.ansoft.solti;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ansoft.solti.R;
import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.util.GpsTracer;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

	public static final String TAG = SignUpActivity.class.getSimpleName();
	protected EditText mUsername;
	protected RadioGroup sex;
	protected String sexuser;
	private RadioButton radioSexButton;
	protected boolean ismale;
	protected EditText mPH;
	protected Button mNextButton;

	protected String lat, lon;

	static int GET_PICTURE = 1, CAMERA_PIC_REQUEST = 2, PIC_CROP = 4,
			CAMERA_CROP = 6;
	ImageView img_profile;
	int n = 10000;
	Uri outputFileUri;
	Bitmap bmp;
	static String selectedImagePath = null;
	String imagename = "";
	byte b[];
	SharedPreferences pref;
	ProgressDialog dialog;
	static boolean is_open = false;
	String image;

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:

				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, GET_PICTURE);

				break;

			}
		}

	};

	// result

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri selectedImageUri = null;
			if (requestCode == GET_PICTURE) {
				// if (bmp != null) {
				// bmp.recycle();
				// }
				selectedImageUri = data.getData();
				if (selectedImageUri == null) {
					Toast.makeText(getApplicationContext(), "Invalid image",
							Toast.LENGTH_SHORT).show();
				} else {
					performCrop(selectedImageUri, PIC_CROP);
				}
			}

			else if (requestCode == CAMERA_PIC_REQUEST) {

				int n = Integer.parseInt(pref.getString("originalfile", "0"));
				if (n == 0) {
					Toast.makeText(getApplicationContext(),
							"Device Error \n Please Try Again",
							Toast.LENGTH_SHORT).show();
				} else {
					String root = Environment.getExternalStorageDirectory()
							.toString();
					File myDir = new File(root + "/NudgeApp");
					myDir.mkdirs();
					String fname = "Image-" + n + ".jpg";
					File file = new File(myDir, fname);
					outputFileUri = Uri.fromFile(file);
					System.out.println(outputFileUri);
					if (outputFileUri != null) {
						performCrop(outputFileUri, CAMERA_CROP);
					} else {
						Toast.makeText(getApplicationContext(),
								"Error \n Try Again", Toast.LENGTH_SHORT)
								.show();
					}
				}
			} else if (requestCode == PIC_CROP) {
				if (data != null) {
					// get the returned data
					Bundle extras = data.getExtras();
					// get the cropped bitmap
					Bitmap selectedBitmap = extras.getParcelable("data");
					if (selectedBitmap != null) {
						img_profile.setImageBitmap(selectedBitmap);
						bmp = selectedBitmap;
						Bitmap finalbit = bmp;
						if (finalbit != null) {
							File ff1 = SaveImage(finalbit, n);
							if (ff1 != null) {
								selectedImagePath = ff1.getAbsolutePath();
								System.out.println(selectedImagePath);
							} else {
								System.out.println("rotation pblms");
							}
						} else {
							System.out.println("Not Roatates process doine");
						}
						if (selectedImagePath == null
								&& selectedImagePath.length() <= 0) {
							System.out
									.println("No selected image path is there..");
							Toast.makeText(getApplicationContext(),
									"Invalid image", Toast.LENGTH_SHORT);
						} else {
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inJustDecodeBounds = true;
							BitmapFactory
									.decodeFile(selectedImagePath, options);
							options.inSampleSize = calculateInSampleSize(
									options, 150, 150);
							options.inJustDecodeBounds = false;

							if (bmp != null) {
								// profilepic.setBackgroundResource(R.drawable.imguploaded);
								System.out.println("sele path "
										+ selectedImagePath);
								ExifInterface ei;
								try {
									ei = new ExifInterface(selectedImagePath);
									int orientation = ei.getAttributeInt(
											ExifInterface.TAG_ORIENTATION,
											ExifInterface.ORIENTATION_NORMAL);

									img_profile.setImageBitmap(bmp);
									finalbit = bmp;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}

								ByteArrayOutputStream bout = new ByteArrayOutputStream();
								bmp.compress(Bitmap.CompressFormat.JPEG, 90,
										bout);
								b = bout.toByteArray();
								System.out.println(b);
								image = Base64
										.encodeToString(b, Base64.DEFAULT);
							} else {
								selectedImagePath = "";
								Toast.makeText(getApplicationContext(),
										"Invalid image", Toast.LENGTH_SHORT)
										.show();
							}
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Invalid Image", Toast.LENGTH_SHORT).show();
					}
				}
			}

			else if (requestCode == CAMERA_CROP) {
				if (data != null) {
					// get the returned data
					Bundle extras = data.getExtras();
					// get the cropped bitmap
					Bitmap selectedBitmap = extras.getParcelable("data");
					if (selectedBitmap == null) {
						Toast.makeText(getApplicationContext(),
								"Invalid Image", Toast.LENGTH_SHORT).show();
					} else {
						Bitmap finalbit = selectedBitmap;
						bmp = selectedBitmap;
						img_profile.setImageBitmap(selectedBitmap);
					}
				}
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, bout);
				b = bout.toByteArray();
				System.out.println(b);
				image = Base64.encodeToString(b, Base64.DEFAULT);
			}

		}
		
		// upload to

	}

	//
	//

	// Methods

	// perform crop operation
	private void performCrop(Uri picUri, int ss) {
		try {

			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 200);
			cropIntent.putExtra("outputY", 200);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, ss);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	// save image
	private File SaveImage(Bitmap finalBitmap, int n) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/Solti");
		myDir.mkdirs();
		String fname = "Image-" + n + ".jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			return file;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// calculate sample size
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sign_up);

		mUsername = (EditText) findViewById(R.id.unField);

		mPH = (EditText) findViewById(R.id.phField);
		img_profile = (ImageView) findViewById(R.id.profileImg);

		mNextButton = (Button) findViewById(R.id.nextButton);

		img_profile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						SignUpActivity.this);
				builder.setItems(R.array.choose_picture, mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});
		sex = (RadioGroup) findViewById(R.id.radioGroup1);
		int selectedId = sex.getCheckedRadioButtonId();
		radioSexButton = (RadioButton) findViewById(selectedId);
		sexuser = radioSexButton.getText().toString();

		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String password = getIntent().getStringExtra("password");
				String email = getIntent().getStringExtra("email");
				String firstname = getIntent().getStringExtra("firstname");
				String lastname = getIntent().getStringExtra("lastname");
				String latitude = getIntent().getStringExtra("latitude");
				String longitude = getIntent().getStringExtra("longitude");
				String phnumber = mPH.getText().toString();
				ArrayList<String> followers = new ArrayList<String>();
				ArrayList<String> following = new ArrayList<String>();

				String username = mUsername.getText().toString();

				latitude = latitude.trim();
				longitude = longitude.trim();

				if ((latitude != "0.0") || (longitude != "0.0")) {
					lat = latitude;
					lon = longitude;
				}

				else {
					GpsTracer gps = new GpsTracer(SignUpActivity.this);
					double lati = gps.getLatitude();
					double longi = gps.getLongitude();

					lat = "" + lati;
					lon = "" + longi;
					Log.e(TAG, latitude + " " + longitude);

				}

				username = username.trim();
				password = password.trim();
				email = email.trim();
				firstname = firstname.trim();
				lastname = lastname.trim();
				phnumber = phnumber.trim();

				if (username.isEmpty() || password.isEmpty() || email.isEmpty()
						|| firstname.isEmpty() || lastname.isEmpty()
						|| phnumber.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SignUpActivity.this);
					builder.setMessage(R.string.signup_error_message)
							.setTitle(R.string.signup_error_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				} else {
					if (bmp==null) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								SignUpActivity.this);
						builder.setMessage("Please upload a profile photo")
								.setTitle(R.string.signup_error_title)
								.setPositiveButton(android.R.string.ok, null);
						AlertDialog dialog = builder.create();
						dialog.show();
					} else {

					setProgressBarIndeterminateVisibility(true);

					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					newUser.put(Constants.KEY_FIRST_NAME, firstname);
					newUser.put(Constants.KEY_LAST_NAME, lastname);
					newUser.put(Constants.KEY_PHONE_NUMBER, phnumber);
					newUser.put(Constants.KEY_LATITUDE, lat);
					newUser.put(Constants.KEY_LONGITUDE, lon);
					newUser.put(Constants.KEY_FOLLOWERS, followers);
					newUser.put(Constants.KEY_TOTAL_FOLLOWERS, 0);
					newUser.put(Constants.KEY_TOTAL_FOLLOWINGS, 0);
					newUser.put(Constants.KEY_SEX, sexuser);

					newUser.signUpInBackground(new SignUpCallback() {

						ProgressDialog progdialog = ProgressDialog.show(
								SignUpActivity.this, "",
								"Signing Up... Please wait", true);

						@Override
						public void done(ParseException e) {

							setProgressBarIndeterminateVisibility(false);
							progdialog.dismiss();
							if (e == null) {
								// Success!

								progdialog.dismiss();
								uploadDP();

							} else {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										SignUpActivity.this);
								builder.setMessage(e.getMessage())

										.setTitle(R.string.signup_error_title)
										.setPositiveButton(android.R.string.ok,
												null);
								AlertDialog dialog = builder.create();
								dialog.show();
								progdialog.dismiss();
							}
						}

						private void uploadDP() {

							byte[] bitmapdata;

							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
							bitmapdata = stream.toByteArray();

							final ParseFile imgfile = new ParseFile(
									Constants.USER_PHOTO_FILENAME,
									bitmapdata);

							imgfile.saveInBackground(new SaveCallback() {

								ProgressDialog progdddialog = ProgressDialog
										.show(SignUpActivity.this, "",
												"Please wait...", true);

								@Override
								public void done(ParseException e) {
									if (e == null) {

										progdddialog.dismiss();
										ParseUser.getCurrentUser().put(
												Constants.USER_PHOTO,
												imgfile);
										ParseUser.getCurrentUser()
												.saveInBackground(
														new SaveCallback() {
															ProgressDialog progdddialog = ProgressDialog
																	.show(SignUpActivity.this,
																			"",
																			"Almost There...",
																			true);

															@Override
															public void done(
																	ParseException e) {
																if (e == null) {
																	progdddialog
																			.dismiss();
																	Solti.UpdateParseInstallation(ParseUser.getCurrentUser());
																	Intent intent = new Intent(
																			SignUpActivity.this,
																			MainActivity.class);
																	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
																	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

																	startActivity(intent);

																} else {
																	Log.e("Upload",
																			"Failure");
																	e.printStackTrace();
																}

															}
														});

									} else {

									}
								}
							}, new ProgressCallback() {
								@Override
								public void done(Integer percentDone) {

								}
							});

						}
					});

				}}
			}

		});

	}

}
