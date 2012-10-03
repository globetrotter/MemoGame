package org.tony.play.memogame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	// a matrix elkeszitese a kepekkel es ures helyekkel a jatek komplexisa szerint

	private static final String DIFF_EASY = "easy";
	private static final String DIFF_NORMAL = "normal";
	private static final String DIFF_HARD = "hard";
	
	private Context context;
	private int tileCount;
	private String difficulty = DIFF_NORMAL;

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public ImageAdapter(Context c) {
		context = c;
	}

	public int getCount() {
		if (DIFF_HARD.equals(difficulty)) {
			tileCount = 42;
		}
		if (DIFF_NORMAL.equals(difficulty)) {
			tileCount = 30;
		}
		if (DIFF_EASY.equals(difficulty)) {
			tileCount = 16;
		}
		return tileCount;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(R.drawable.ic_launcher);
		return imageView;
	}

}