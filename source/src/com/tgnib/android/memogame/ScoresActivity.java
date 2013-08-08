package com.tgnib.android.memogame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

@SuppressLint("NewApi")
public class ScoresActivity extends Activity {

	private static final String SHARE_EXTRA_SUBJECT = "Memo - You will love it!";
	private static final String SHARE_EXTRA_TEXT = "I just achieved a great score on Memo. Want to take a challenge? Link to Google Play Store:\n http://goo.gl/KCEr15";

	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ScoresActivity.onCreate()");
		setContentView(R.layout.activity_scores);

		// read data from external storage
		String[] values = readDataFromExternalStorage("Scores.csv");

		// easy
		final ListView listView = (ListView) findViewById(R.id.scores_easy);
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) {
			list.add(values[i]);
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);

		// challenge
		final ListView listViewChal = (ListView) findViewById(R.id.scores_chal);
		values = readDataFromExternalStorage("Scores_Chall.csv");
		final ArrayList<String> listChal = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) {
			listChal.add(values[i]);
		}
		final ArrayAdapter<String> adapterChal = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				listChal);
		listViewChal.setAdapter(adapterChal);

	}

	private String[] readDataFromExternalStorage(String filename) {
		String[] values = new String[] { "", "", "", "" };
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if (mExternalStorageAvailable) {
			File file = new File(getExternalFilesDir(null), filename);
			try {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String inputString;
				try {
					int i = 0;
					int limit = 4;
					while ((inputString = inputReader.readLine()) != null) {
						values[i++] = inputString;
						if (i == limit) {
							break;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				values = new String[] { "", "", "", "" };
				e.printStackTrace();
			}

		} else {
			values = new String[] { "", "", "", "" };
		}

		return values;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("ScoresActivity.onCreateOptionsMenu()");
		getMenuInflater().inflate(R.menu.scores_menu, menu);
		MenuItem item = menu.findItem(R.id.menu_item_score_share);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mShareActionProvider = (ShareActionProvider) item.getActionProvider();
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(android.content.Intent.EXTRA_SUBJECT, SHARE_EXTRA_SUBJECT);
			intent.putExtra(android.content.Intent.EXTRA_TEXT, SHARE_EXTRA_TEXT);
			mShareActionProvider.setShareIntent(intent);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("ScoresActivity.onOptionsItemSelected()");
		switch (item.getItemId()) {
		case R.id.menu_item_score_share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SHARE_EXTRA_SUBJECT);
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, SHARE_EXTRA_TEXT);
			startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));
			break;
		default:
			break;
		}
		return true;
	}

	// call to update the share intent
	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	// Somewhere in the application.
	public void doShare(Intent shareIntent) {
		// When you want to share set the share intent.
		mShareActionProvider.setShareIntent(shareIntent);
	}

}
