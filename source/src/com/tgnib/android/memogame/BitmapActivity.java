package com.tgnib.android.memogame;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.tgnib.android.memogame.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class BitmapActivity extends Activity {

	private static final int SELECT_PHOTO = 100;
	
	private ArrayList<Bitmap> IMAGES = new ArrayList<Bitmap>();

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bitmap);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
		findViewById(R.id.dummy_button).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				// photoPickerIntent.setType("/image/*");
				// startActivityForResult(photoPickerIntent, SELECT_PHOTO);

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
			}
		});


		// ImageView iv = (ImageView) findViewById(R.id.imageView1);
		// iv.setBackgroundResource(R.drawable.card_6);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@Override
	public void finish() {
		// Prepare data intent
		Intent data = new Intent();
		data.putExtra("returnKey1", "Swinging on a star. ");
		
		// Activity finished ok, return the data
		setResult(RESULT_OK, data);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();

				Bitmap yourSelectedImage = null;
				Bitmap croppedBmp = null;
				Bitmap croppedBmp2 = null;
				Bitmap croppedBmp3 = null;
				Bitmap croppedBmp4 = null;
				Bitmap croppedBmp5 = null;
				Bitmap croppedBmp6 = null;				
				try {
					yourSelectedImage = decodeUri(selectedImage);
//					int middleW = 360;
//					int middleH = 360;
					int middleW = (yourSelectedImage.getWidth()-10)/3;
					int middleH = (yourSelectedImage.getHeight()-50)/2;
					
					croppedBmp = Bitmap.createBitmap(yourSelectedImage, 0, 0, middleW, middleH);
					croppedBmp2 = Bitmap.createBitmap(yourSelectedImage, middleW, 0, middleW, middleH);
					croppedBmp3 = Bitmap.createBitmap(yourSelectedImage, middleW*2, 0, middleW, middleH);

					croppedBmp4= Bitmap.createBitmap(yourSelectedImage, 0, middleH, middleW, middleH);
					croppedBmp5 = Bitmap.createBitmap(yourSelectedImage, middleW, middleH, middleW, middleH);
					croppedBmp6 = Bitmap.createBitmap(yourSelectedImage, middleW*2, middleH, middleW, middleH);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				IMAGES.add(croppedBmp);
				IMAGES.add(croppedBmp2);
				IMAGES.add(croppedBmp3);
				IMAGES.add(croppedBmp4);
				IMAGES.add(croppedBmp5);
				IMAGES.add(croppedBmp6);
				
				ImageView iv = (ImageView) findViewById(R.id.imageView1);
				iv.setImageBitmap(croppedBmp);

				iv = (ImageView) findViewById(R.id.imageView2);
				iv.setImageBitmap(croppedBmp2);

				iv = (ImageView) findViewById(R.id.imageView3);
				iv.setImageBitmap(croppedBmp3);

				iv = (ImageView) findViewById(R.id.ImageView4);
				iv.setImageBitmap(croppedBmp4);

				iv = (ImageView) findViewById(R.id.ImageView5);
				iv.setImageBitmap(croppedBmp5);

				iv = (ImageView) findViewById(R.id.ImageView6);
				iv.setImageBitmap(croppedBmp6);				

			}
		}
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1100;
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

	}
}
