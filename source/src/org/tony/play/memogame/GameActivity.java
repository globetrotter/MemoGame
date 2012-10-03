package org.tony.play.memogame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class GameActivity extends Activity {

	private String gameDifficulty = "normal";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		GridView gridview = (GridView) findViewById(R.id.gameGridView);
		ImageAdapter imageAdapter = new ImageAdapter(this);
		imageAdapter.setDifficulty(gameDifficulty);
		gridview.setAdapter(imageAdapter);
		gridview.setBackgroundColor(getResources().getColor(
				R.color.image_placeholder));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				ImageView imageView = (ImageView) v;
				Resources resources = getResources();
				Drawable drawable = resources.getDrawable(R.drawable.fruits1);
				imageView.setImageDrawable(drawable);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
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
						}).setNegativeButton("No", null).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		GridView gridview = (GridView) findViewById(R.id.gameGridView);
		ImageAdapter imageAdapter = (ImageAdapter) gridview.getAdapter();

		switch (item.getItemId()) {
		case R.id.menu_difficulty_easy:
			gameDifficulty = "easy";
			imageAdapter.setDifficulty(gameDifficulty);
			imageAdapter.notifyDataSetChanged();
			
			break;
		case R.id.menu_difficulty_normal:
			gameDifficulty = "normal";
			imageAdapter.setDifficulty(gameDifficulty);
			imageAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_difficulty_hard:
			gameDifficulty = "hard";
			imageAdapter.setDifficulty(gameDifficulty);
			imageAdapter.notifyDataSetChanged();
			break;
		// case R.id.menu_quit:
		// GameActivity.this.finish();
		// break;
		default:
			break;
		}
		return true;
	}

}
