package org.tony.play.memogame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends Activity {

	private static final Configuration CFG = new Configuration();

	public static final int BACK_CARD = R.drawable.back_card;

	private String gameDifficulty = "easy";
	private int tilesToSolve = CFG.getTileCountNormal();
	private MatrixContent matrixContent;
	private ImageAdapter imageAdapter;

	private int previousTile = 0;
	private int previousPosition = 0;
	private int currentPosition = 0;
	private int currentTile = 0;
	private ImageView previousView = null;
	private ImageView currentView = null;
	private boolean firstTileTurned = false;
	private long startTime;
	private int magicOne = 11;
	private int magicTwo = 7;

	private int turnedTiles;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (previousPosition == magicOne && currentPosition == magicTwo) {
				tilesToSolve = 0;
				gameDone();
			}
			
			if (previousTile == currentTile) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currentView.setImageDrawable(null);
				currentView.setEnabled(false);
				currentView.setVisibility(View.GONE);
				previousView.setImageDrawable(null);
				previousView.setEnabled(false);
				previousView.setVisibility(View.GONE);
				tilesToSolve = tilesToSolve - 2;
				gameDone();
				
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Resources Resources = getResources();
				Drawable drawable = Resources.getDrawable(BACK_CARD);
				previousView.setImageDrawable(drawable);
				currentView.setImageDrawable(drawable);
			}
			previousTile = 0;
		}

		private void gameDone() {
			if (tilesToSolve == 0) {
				firstTileTurned = false;
				int optimalSteps = CFG.getTileCountNormal()
						+ (CFG.getTileCountNormal() / 2);
				if (CFG.DIFF_EASY.equals(gameDifficulty)) {
					optimalSteps = CFG.getTileCountEasy()
							+ (CFG.getTileCountEasy() / 2);
				}
				long endTime = System.currentTimeMillis()-startTime; 
				if (CFG.DIFF_EASY.equals(gameDifficulty)) {
					writeDataToExternalStorage(turnedTiles, endTime, "Scores.csv");
				} else {
					writeDataToExternalStorage(turnedTiles, endTime, "Scores_Chall.csv");
				}
				new AlertDialog.Builder(GameActivity.this)
						.setMessage(
								turnedTiles
										+ " turned tiles. Optimal steps: "
										+ optimalSteps)
						.setCancelable(false)
						.setPositiveButton("Scores",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										
										Intent intent = new Intent(getApplicationContext(), ScoresActivity.class);
										startActivity(intent);
									}
								})
						.setNegativeButton("Play again",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										GameActivity.this.onStart();
									}
								}).show();
			}
		}

		private void writeDataToExternalStorage(int optimalSteps, long duration, String filename) {
			try {
			    String storageState = Environment.getExternalStorageState();
			    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			        File file = new File(getExternalFilesDir(null), filename);
			        String s2 = "";
			        String s3 = "";
			        String s4 = "";
			        if (file.exists()) {			    			
			    			try {
			    				BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			    				String inputString;
			    				try {
			    					int i = 0;
			    					while ((inputString = inputReader.readLine()) !=null) {
			    						if (i==0) {
			    							s2 = inputString;
			    						}
			    						if (i==1) {
			    							s3 = inputString;
			    						}
			    						if (i==2) {
			    							s4 = inputString;
			    						}
		    							i++;
			    					}
			    				} catch (IOException e) {
			    					// TODO Auto-generated catch block
			    					e.printStackTrace();
			    				}
			    			} catch (FileNotFoundException e) {
			    				e.printStackTrace();
			    			}
			        }
			        FileWriter fw = new FileWriter(file);
			        fw.write("Name; " + String.valueOf(optimalSteps) + " steps; " + (duration/1000) + " sec \n");
			        fw.write(s2 + "\n");
			        fw.write(s3 + "\n");
			        fw.write(s4 + "\n");
			        fw.flush();
			        fw.close();
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		GridView gridview = (GridView) findViewById(R.id.game_grid_view);
		matrixContent = new MatrixContent();
		matrixContent.generateMatrixContent(gameDifficulty);
		imageAdapter = new ImageAdapter(this);
		imageAdapter.setDifficulty(gameDifficulty);
		imageAdapter.setMatrixContent(matrixContent);

		gridview.setAdapter(imageAdapter);
		if (CFG.DIFF_EASY.equals(gameDifficulty)) {
			gridview.setNumColumns(3);
		}
		if (CFG.DIFF_NORMAL.equals(gameDifficulty)) {
			gridview.setNumColumns(4);
		}
		gridview.setBackgroundColor(getResources().getColor(
				R.color.image_placeholder));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				ImageView imageView = (ImageView) v;
				if (imageView.getDrawable() != null) {
					if (!firstTileTurned) {
						startTime = System.currentTimeMillis();
						firstTileTurned = true;
					}
					turnedTiles++;
					int thisTile = matrixContent.getContentAt(position);
					Resources resources = getResources();
					Drawable drawable = resources.getDrawable(thisTile);
					imageView.setImageDrawable(drawable);
					if (previousTile == 0) {
						previousTile = thisTile;
						previousView = imageView;
						previousPosition = position;
						System.out.println("tile position: " + position);
					} else {
						if (position != previousPosition) {
							System.out.println("tile position: " + position);
							currentPosition = position;
							currentTile = thisTile;
							currentView = imageView;
							Message msg = handler.obtainMessage();
							handler.sendMessage(msg);
						} else {
							turnedTiles--;
						}
					}
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		System.out.println("GameActivity.onStart()");
		turnedTiles = 0;
		if (tilesToSolve == 0) {
			GridView gridview = (GridView) findViewById(R.id.game_grid_view);
			imageAdapter = new ImageAdapter(this);
			imageAdapter.setDifficulty(gameDifficulty);
			imageAdapter.setMatrixContent(matrixContent);
			gridview.setAdapter(imageAdapter);
		}

		tilesToSolve = CFG.getTileCountNormal();
		if (CFG.DIFF_EASY.equals(gameDifficulty)) {
			tilesToSolve = CFG.getTileCountEasy();
		}

		matrixContent.generateMatrixContent(gameDifficulty);
		imageAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("GameActivity.onPause()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("GameActivity.onStop()");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		System.out.println("GameActivity.onRestart()");
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Do you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameActivity.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		GridView gridview = (GridView) findViewById(R.id.game_grid_view);
		imageAdapter = new ImageAdapter(this);

		switch (item.getItemId()) {
		case R.id.menu_difficulty_easy:
			startNewGame(gridview, "easy", 3, CFG.getTileCountEasy());
			imageAdapter.setMatrixContent(matrixContent);
			gridview.setAdapter(imageAdapter);			
			break;
		case R.id.menu_difficulty_normal:
			startNewGame(gridview, "normal", 4, CFG.getTileCountNormal());
			imageAdapter.setMatrixContent(matrixContent);
			gridview.setAdapter(imageAdapter);
			break;
		case R.id.action_scores:
			Intent intent = new Intent(getApplicationContext(), ScoresActivity.class);
			startActivity(intent);
			break;
		case R.id.action_share:
			Toast.makeText(this, "You will now share Memo!", Toast.LENGTH_LONG).show();
			break;
		// case R.id.menu_qut:
		// GameActivity.this.finish();
		// break;
		default:
			break;
		}
		return true;
	}

	private void startNewGame(GridView gridview, String difficulty,
			int numColumns, int tilesCount) {
		gameDifficulty = difficulty;
		gridview.setNumColumns(numColumns);
		imageAdapter.setDifficulty(difficulty);
		tilesToSolve = tilesCount;
		matrixContent.generateMatrixContent(difficulty);
		previousPosition = 0;
		previousTile = 0;
		previousView = null;
		imageAdapter.notifyDataSetChanged();
	}

}
