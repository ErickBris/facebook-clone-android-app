package com.ansoft.solti;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ansoft.solti.constants.Constants;
import com.ansoft.solti.util.ErrorDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditProfile extends Activity {

	ImageView editProf;
	Button editName, editUsername, editPassowrd, editEmail, changeProf;
	static int GET_PICTURE = 1, CAMERA_PIC_REQUEST = 2, PIC_CROP = 4, CAMERA_CROP = 6;
	SharedPreferences pref;
	Uri outputFileUri=null;
	int n = 10000;
	Bitmap bmp;
	static String selectedImagePath = null;
	byte b[];
	String image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		editProf=(ImageView)findViewById(R.id.editProfileImg);
		editName=(Button)findViewById(R.id.editFullName);
		editUsername=(Button)findViewById(R.id.editUsername);
		editPassowrd=(Button)findViewById(R.id.editPassword);
		editEmail=(Button)findViewById(R.id.editEmailAddress);
		changeProf=(Button)findViewById(R.id.editProfilePhoto);
		
		editProf.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 startActivityForResult(intent, GET_PICTURE); 
				
			}
		});
		changeProf.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (bmp==null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
					builder.setMessage("Please Choose a Photo")
						
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}else {
					
						

						byte[] bitmapdata;
						
						
						ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
		                bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream); 
		                bitmapdata = stream.toByteArray();

		                final ParseFile imgfile = new ParseFile(Constants.USER_PHOTO_FILENAME, bitmapdata);

		                imgfile.saveInBackground(new SaveCallback() {
		                	
		                	ProgressDialog progdddialog = ProgressDialog.show(EditProfile.this, "","Uploading Photo...", true);
		                	
		                      @Override
							public void done(ParseException e) {                          
		                          if (e == null) {
		                        	  
		                        	  
		                        	  progdddialog.dismiss();
		                        	  ParseUser.getCurrentUser().put(Constants.USER_PHOTO, imgfile);
		                          	ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
		                          		ProgressDialog progdddialog = ProgressDialog.show(EditProfile.this, "","Almost Finished...", true);
		                      			
		                          		@Override
		                      			public void done(ParseException e) {
		                      				if (e==null) {
		                      					 progdddialog.dismiss();
		                      					 
		                      					
		                          	} else {
		                          		Log.e("Upload", "Failure");
		                                   e.printStackTrace();
		                          	}
		                      				
		                      			}
		                      		});
		                        	  
		                        	
		                        	   
		                          }else {
		                             
		                          }
		                        }   
		                       }, new ProgressCallback() {
		                          @Override
								public void done(Integer percentDone) {
		                        	  
		                          }
		                        });
		                
		                
		                	
		                	
					}
				
				
			}
		});

		editUsername.setOnClickListener(new View.OnClickListener() {
			
			 @Override
			public void onClick(View v) {
				AlertDialog.Builder alertdi=new AlertDialog.Builder(EditProfile.this);
				final AlertDialog alert=alertdi.create();
				final EditText edittext=new EditText(EditProfile.this);
				final Button checkavailable=new Button(EditProfile.this);
				
				checkavailable.setText("Check availability");
				checkavailable.setTextColor(Color.WHITE);
				checkavailable.setBackgroundResource(R.drawable.login_button);
				edittext.setHint("Username");
				
				LinearLayout layout=new LinearLayout(EditProfile.this);
				layout.setOrientation(layout.VERTICAL);
				layout.addView(edittext);
				layout.addView(checkavailable);
				
				alert.setMessage("Enter a new Username");
				alert.setCancelable(true);
				
				alert.setView(layout);
				checkavailable.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final Editable username=edittext.getText();
						ParseQuery<ParseUser> query=ParseUser.getQuery();
						query.whereEqualTo(Constants.KEY_USERNAME, username.toString());
						query.getFirstInBackground(new GetCallback<ParseUser>() {
							ProgressDialog pro=ProgressDialog.show(EditProfile.this, "", "Checking Availability..");
							@Override
							public void done(ParseUser user, ParseException e) {
								pro.dismiss();
								if (user==null) {
									ParseUser.getCurrentUser().put(Constants.KEY_USERNAME, username.toString());
									ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
										ProgressDialog prdlg=ProgressDialog.show(EditProfile.this, "", "Updating username");
										@Override
										public void done(ParseException e) {
											alert.dismiss();
											prdlg.dismiss();
											
											if (e==null) {
												Toast.makeText(EditProfile.this, "Username Updated", Toast.LENGTH_SHORT);
											} else {
												ErrorDialog err = new ErrorDialog();
												err.Show(EditProfile.this, 1);
											}
										}
									});
									
								} else {
									//not available
									ErrorDialog err=new ErrorDialog();
									err.Show(EditProfile.this, 3);
								}
								
							}
						});
					}
				});
				
				
				alert.show();
			}
		});
		editName.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertdi=new AlertDialog.Builder(EditProfile.this);
				final AlertDialog alert=alertdi.create();
				final EditText Firstname=new EditText(EditProfile.this);
				Firstname.setHint("First Name");
				final EditText Lastname=new EditText(EditProfile.this);
				Lastname.setHint("Last Name");
				LinearLayout layout=new LinearLayout(EditProfile.this);
				layout.setOrientation(layout.VERTICAL);
				layout.addView(Firstname);
				layout.addView(Lastname);
				alert.setView(layout);
				alert.setMessage("Enter new name");
				alert.setButton("Update", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						final Editable FN=Firstname.getText();
						final Editable LN=Lastname.getText();
						ParseUser.getCurrentUser().put(Constants.KEY_FIRST_NAME, FN.toString());
						ParseUser.getCurrentUser().put(Constants.KEY_LAST_NAME, LN.toString());
						ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
							ProgressDialog prd=ProgressDialog.show(EditProfile.this, "", "Updating name...");
							@Override
							public void done(ParseException e) {
								prd.dismiss();
								if (e==null) {
									//success
								} else {
									ErrorDialog err=new ErrorDialog();
									err.Show(getApplicationContext(), 1);
								}
								
							}
						});
						
					}
				});
			alert.show();
			}
		});
		
		
		editEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert=new AlertDialog.Builder(EditProfile.this);
				final EditText Email=new EditText(EditProfile.this);
				Email.setHint("Email");
				alert.setTitle("Enter new email");
				alert.setView(Email);
				
				alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						final Editable FN=Email.getText();
						
						ParseUser.getCurrentUser().setEmail(FN.toString());
						
						ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
							ProgressDialog prd=ProgressDialog.show(EditProfile.this, "", "Updating email...");
							@Override
							public void done(ParseException e) {
								prd.dismiss();
								if (e==null) {
									//success
								} else {
									ErrorDialog err=new ErrorDialog();
									err.Show(getApplicationContext(), 1);
								}
								
							}
						});
						
					}
				});
			alert.show();
			}
		});
		
		editPassowrd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert=new AlertDialog.Builder(EditProfile.this);
				
				final EditText newPassword=new EditText(EditProfile.this);
				final EditText confirmPassword=new EditText(EditProfile.this);
				
				newPassword.setHint("Enter New Password");
				confirmPassword.setHint("Confirm New Password");
				LinearLayout lin=new LinearLayout(EditProfile.this);
				
				lin.addView(newPassword);
				lin.addView(confirmPassword);
				lin.setOrientation(lin.VERTICAL);
				alert.setView(lin);
				alert.setTitle("Change Password");
				
				alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
							
						
						
						final Editable FN=newPassword.getText();
						
						ParseUser.getCurrentUser().setPassword(FN.toString());
						
						ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
							ProgressDialog prd=ProgressDialog.show(EditProfile.this, "", "Updating password...");
							@Override
							public void done(ParseException e) {
								prd.dismiss();
								if (e==null) {
									//success
								} else {
									ErrorDialog err=new ErrorDialog();
									err.Show(getApplicationContext(), 1);
								}
								
							}
						});
						} else {
							AlertDialog.Builder builder=new AlertDialog.Builder(EditProfile.this);
							AlertDialog alert=builder.create();
							alert.setMessage("Please confirm both password are correct");
							alert.setTitle("Password Mismatch");
							alert.setCanceledOnTouchOutside(true);
						}
					}
				});
				
			alert.show();
			}
		});
	}
	
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
								"Error \n Try Again", Toast.LENGTH_SHORT).show();
					}
				}
			} 
			else if (requestCode == PIC_CROP) {
				if (data != null) {
					// get the returned data
					Bundle extras = data.getExtras();
					// get the cropped bitmap
					Bitmap selectedBitmap = extras.getParcelable("data");
					if (selectedBitmap != null) {
						editProf.setImageBitmap(selectedBitmap);
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
														
										editProf.setImageBitmap(bmp);
										finalbit = bmp;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}

								ByteArrayOutputStream bout = new ByteArrayOutputStream();
								bmp.compress(Bitmap.CompressFormat.JPEG, 90, bout);
								b = bout.toByteArray();
								System.out.println(b);
								image = Base64.encodeToString(b, Base64.DEFAULT);
							} else {
								selectedImagePath = "";
								Toast.makeText(getApplicationContext(),	"Invalid image", Toast.LENGTH_SHORT).show();
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
						editProf.setImageBitmap(selectedBitmap);
					}
				}
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, bout);
				b = bout.toByteArray();
				System.out.println(b);
				image = Base64.encodeToString(b, Base64.DEFAULT);
			}

		}
	editProf.setImageBitmap(bmp);
	//upload to 
	
	
	
	
	}
	
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
	private File SaveImage(Bitmap finalBitmap, int n) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/NudgeApp");
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

	public static int calculateInSampleSize(BitmapFactory.Options options,	int reqWidth, int reqHeight) {
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
	
	
}
