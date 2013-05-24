package org.tony.play.memogame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
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
import android.widget.ShareActionProvider;

public class GameActivity extends Activity {

	private static final Configuration CFG = new Configuration();
	public static final int BACK_CARD = R.drawable.back_card;
	private static final int MAGIC_ONE = 11;
	private static final int MAGIC_TWO = 7;

	private String mGameDifficulty = "easy";
	private int mTilesToSolve = CFG.getTileCountNormal();
	private MatrixContent mMatrixContent;
	private ImageAdapter mImageAdapter;

	private int mPreviousTile = 0;
	private int mPreviousPosition = 0;
	private int mCurrentPosition = 0;
	private int mCurrentTile = 0;
	private ImageView mPreviousView = null;
	private ImageView mCurrentView = null;
	private boolean mFirstTileTurned = false;
	private long mStartTime;
	private int mTurnedTiles;
	private ShareActionProvider mShareActionProvider; 
	

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (mPreviousPosition == MAGIC_ONE && mCurrentPosition == MAGIC_TWO) {
				mTilesToSolve = 0;
				gameDone();
			}
			
			if (mPreviousTile == mCurrentTile) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mCurrentView.setImageDrawable(null);
				mCurrentView.setEnabled(false);
				mCurrentView.setVisibility(View.GONE);
				mPreviousView.setImageDrawable(null);
				mPreviousView.setEnabled(false);
				mPreviousView.setVisibility(View.GONE);
				mTilesToSolve = mTilesToSolve - 2;
				gameDone();
				
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Resources Resources = getResources();
				Drawable drawable = Resources.getDrawable(BACK_CARD);
				mPreviousView.setImageDrawable(drawable);
				mCurrentView.setImageDrawable(drawable);
			}
			mPreviousTile = 0;
		}

		private void gameDone() {
			if (mTilesToSolve == 0) {
				mFirstTileTurned = false;
				int optimalSteps = CFG.getTileCountNormal()
						+ (CFG.getTileCountNormal() / 2);
				if (CFG.DIFF_EASY.equals(mGameDifficulty)) {
					optimalSteps = CFG.getTileCountEasy()
							+ (CFG.getTileCountEasy() / 2);
				}
				long endTime = System.currentTimeMillis()-mStartTime; 
				if (CFG.DIFF_EASY.equals(mGameDifficulty)) {
					writeDataToExternalStorage(mTurnedTiles, endTime, "Scores.csv");
				} else {
					writeDataToExternalStorage(mTurnedTiles, endTime, "Scores_Chall.csv");
				}
				new AlertDialog.Builder(GameActivity.this)
						.setMessage(
								mTurnedTiles
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
			        // get current date
			        Calendar c = Calendar.getInstance();
			        System.out.println("Current time => " + c.getTime());

			        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			        String formattedDate = df.format(c.getTime());			        
			        
			        fw.write("Name; " + String.valueOf(optimalSteps) + " steps; " + (duration/1000) + " sec; " + formattedDate + "\n");
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
		mMatrixContent = new MatrixContent();
		mMatrixContent.generateMatrixContent(mGameDifficulty);
		mImageAdapter = new ImageAdapter(this);
		mImageAdapter.setDifficulty(mGameDifficulty);
		mImageAdapter.setMatrixContent(mMatrixContent);

		gridview.setAdapter(mImageAdapter);
		if (CFG.DIFF_EASY.equals(mGameDifficulty)) {
			gridview.setNumColumns(3);
		}
		if (CFG.DIFF_NORMAL.equals(mGameDifficulty)) {
			gridview.setNumColumns(4);
		}
		gridview.setBackgroundColor(getResources().getColor(
				R.color.image_placeholder));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				ImageView imageView = (ImageView) v;
				if (imageView.getDrawable() != null) {
					if (!mFirstTileTurned) {
						mStartTime = System.currentTimeMillis();
						mFirstTileTurned = true;
					}
					mTurnedTiles++;
					int thisTile = mMatrixContent.getContentAt(position);
					Resources resources = getResources();
					Drawable drawable = resources.getDrawable(thisTile);
					imageView.setImageDrawable(drawable);
					if (mPreviousTile == 0) {
						mPreviousTile = thisTile;
						mPreviousView = imageView;
						mPreviousPosition = position;
						System.out.println("tile position: " + position);
					} else {
						if (position != mPreviousPosition) {
							System.out.println("tile position: " + position);
							mCurrentPosition = position;
							mCurrentTile = thisTile;
							mCurrentView = imageView;
							Message msg = mHandler.obtainMessage();
							mHandler.sendMessage(msg);
						} else {
							mTurnedTiles--;
						}
					}
				}

			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("GameActivity.onCreateOptionsMenu()");
		// inflate menu resource file
		getMenuInflater().inflate(R.menu.game_activity_menu, menu);
		// locate menuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
		// fetch and store action ShareActionProvider 
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		return true;
	}
	
//	// Somewhere in the application.
//	 public void doShare(Intent shareIntent) {
//	     // When you want to share set the share intent.
//	     mShareActionProvider.setShareIntent(shareIntent);
//	 }
	
	// call to update the share intent
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(shareIntent);
	    }
	}
	
	@Override
	public void onStart() {
		super.onStart();
		System.out.println("GameActivity.onStart()");
		mTurnedTiles = 0;
		if (mTilesToSolve == 0) {
			GridView gridview = (GridView) findViewById(R.id.game_grid_view);
			mImageAdapter = new ImageAdapter(this);
			mImageAdapter.setDifficulty(mGameDifficulty);
			mImageAdapter.setMatrixContent(mMatrixContent);
			gridview.setAdapter(mImageAdapter);
		}

		mTilesToSolve = CFG.getTileCountNormal();
		if (CFG.DIFF_EASY.equals(mGameDifficulty)) {
			mTilesToSolve = CFG.getTileCountEasy();
		}

		mMatrixContent.generateMatrixContent(mGameDifficulty);
		mImageAdapter.notifyDataSetChanged();
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
				.setMessage("Quit Memory Game?")
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
		mImageAdapter = new ImageAdapter(this);

		switch (item.getItemId()) {
		case R.id.menu_item_difficulty_easy:
			startNewGame(gridview, "easy", 3, CFG.getTileCountEasy());
			mImageAdapter.setMatrixContent(mMatrixContent);
			gridview.setAdapter(mImageAdapter);			
			break;
		case R.id.menu_item_difficulty_normal:
			startNewGame(gridview, "normal", 4, CFG.getTileCountNormal());
			mImageAdapter.setMatrixContent(mMatrixContent);
			gridview.setAdapter(mImageAdapter);
			break;
		case R.id.menu_item_scores:
			Intent intent = new Intent(getApplicationContext(), ScoresActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello :-) ");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Just played Memo Game. I think you would love it, too! You can get it here: www");
			startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));
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
		mGameDifficulty = difficulty;
		gridview.setNumColumns(numColumns);
		mImageAdapter.setDifficulty(difficulty);
		mTilesToSolve = tilesCount;
		mMatrixContent.generateMatrixContent(difficulty);
		mPreviousPosition = 0;
		mPreviousTile = 0;
		mPreviousView = null;
		mImageAdapter.notifyDataSetChanged();
	}

}
