package org.tony.play.memogame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class GameActivity extends Activity {

	private static final Configuration CFG = new Configuration();

	public static final int BACK_CARD = R.drawable.back_card;

	private String gameDifficulty = "easy";
	private int trackSolvedTiles = CFG.getTileCountNormal();
	private MatrixContent matrixContent;
	private ImageAdapter imageAdapter;

	private int previousTile = 0;
	private int previousPosition = 0;
	private int currentTile = 0;
	private ImageView previousView = null;
	private ImageView currentView =  null;

	private int turnedTiles;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

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
				trackSolvedTiles = trackSolvedTiles - 2;

				if (trackSolvedTiles == 0) {
					int optimalSteps = CFG.getTileCountNormal()
							+ (CFG.getTileCountNormal() / 2);
					if (CFG.DIFF_EASY.equals(gameDifficulty)) {
						optimalSteps = CFG.getTileCountEasy()
								+ (CFG.getTileCountEasy() / 2);
					}

					new AlertDialog.Builder(GameActivity.this)
							.setMessage(
									"Good job! With " + turnedTiles
											+ " turned tiles. Optimal steps: "
											+ optimalSteps)
							.setCancelable(false)
							.setPositiveButton("Quit",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											GameActivity.this.finish();
										}
									})
							.setNegativeButton("I can beat that",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											GameActivity.this.onStart();
										}
									}).show();
				}
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
					turnedTiles++;
					int thisTile = matrixContent.getContentAt(position);
					Resources resources = getResources();
					Drawable drawable = resources.getDrawable(thisTile);
					imageView.setImageDrawable(drawable);
					if (previousTile == 0) {
						previousTile = thisTile;
						previousView = imageView;
						previousPosition = position;
					} else {
						if (position != previousPosition) {
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
		turnedTiles = 0;
		if (trackSolvedTiles == 0) {
			GridView gridview = (GridView) findViewById(R.id.game_grid_view);
			imageAdapter = new ImageAdapter(this);
			imageAdapter.setDifficulty(gameDifficulty);
			imageAdapter.setMatrixContent(matrixContent);
			gridview.setAdapter(imageAdapter);
		}

		trackSolvedTiles = CFG.getTileCountNormal();
		if (CFG.DIFF_EASY.equals(gameDifficulty)) {
			trackSolvedTiles = CFG.getTileCountEasy();
		}

		matrixContent.generateMatrixContent(gameDifficulty);
		imageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameActivity.this.finish();
							}
						}).setNegativeButton("Nope", null).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		GridView gridview = (GridView) findViewById(R.id.game_grid_view);
		imageAdapter = new ImageAdapter(this);

		switch (item.getItemId()) {
		case R.id.menu_difficulty_easy:
			gameDifficulty = "easy";
			gridview.setNumColumns(3);
			imageAdapter.setDifficulty(gameDifficulty);
			trackSolvedTiles = CFG.getTileCountEasy();
			matrixContent.generateMatrixContent(gameDifficulty);
			imageAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_difficulty_normal:
			gameDifficulty = "normal";
			gridview.setNumColumns(4);
			imageAdapter.setDifficulty(gameDifficulty);
			trackSolvedTiles = CFG.getTileCountNormal();
			matrixContent.generateMatrixContent(gameDifficulty);
			imageAdapter.notifyDataSetChanged();
			break;
		// case R.id.menu_quit:
		// GameActivity.this.finish();
		// break;
		default:
			break;
		}
		imageAdapter.setMatrixContent(matrixContent);
		gridview.setAdapter(imageAdapter);
		return true;
	}

}
