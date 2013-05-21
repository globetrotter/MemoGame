package org.tony.play.memogame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		final ArrayAdapter<String> adapterChal = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listChal);
		listViewChal.setAdapter(adapterChal);
		
	}

	private String[] readDataFromExternalStorage(String filename) {
		String[] values 
			= new String[]
					{
				"", 
				"",
				"",
				""};
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
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
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
					while ((inputString = inputReader.readLine()) !=null) {
						values [i++] = inputString;
						if (i == limit) {
							break;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				values 
				= new String[] 
						{
					"", 
					"",
					"",
					""};				
				e.printStackTrace();
			}
		
		} else {
			values 
			= new String[] 
					{
				"", 
				"",
				"",
				""};			
		}
			
		return values;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.scores, menu);
		return true;
	}

}
