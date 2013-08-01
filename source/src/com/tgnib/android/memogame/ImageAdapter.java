package com.tgnib.android.memogame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private static final Configuration CFG = new Configuration();
	private Context context;
	private int tileCount;
	private String gameDifficulty = CFG.DIFF_NORMAL;
	private MatrixContent matrixContent;

	public void setDifficulty(String difficulty) {
		gameDifficulty = difficulty;
	}

	public ImageAdapter(Context c) {
		context = c;
	}

	public int getCount() {
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
		if (convertView == null) {
			// if it's not recycled, initialize some attributes
			imageView = new ImageView(context);
			// imageView.setPadding(10, 10, 0, 0);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			// int d = context.getResources().getDisplayMetrics().densityDpi;
			int h = context.getResources().getDisplayMetrics().heightPixels;
			// int w = context.getResources().getDisplayMetrics().widthPixels;
			// System.out.println("h w d:  " + h + " " + w + " "+ d);

			// 7 inch and 10 inch display
			if (h >= 1200) {
				imageView.setLayoutParams(new GridView.LayoutParams(300, 270));
				if (gameDifficulty.equals(CFG.DIFF_NORMAL)) {
					imageView.setLayoutParams(new GridView.LayoutParams(205,
							185));
				}
			}

			// 4.7 inch display
			if (h > 1100 && h < 1200) {
				imageView.setLayoutParams(new GridView.LayoutParams(275, 255));
				if (gameDifficulty.equals(CFG.DIFF_NORMAL)) {
					// width, height
					imageView.setLayoutParams(new GridView.LayoutParams(150,
							172));
				}
			}

			// 4 inch and smaller display
			if (h <= 1100) {
				imageView.setLayoutParams(new GridView.LayoutParams(185, 163));
				if (gameDifficulty.equals(CFG.DIFF_NORMAL)) {
					// width, height
					imageView.setLayoutParams(new GridView.LayoutParams(137,
							115));
				}
			}

		} else {
			imageView = (ImageView) convertView;
		}

		int coverTileCode = matrixContent
				.getCoverTile(gameDifficulty, position);
		imageView.setImageResource(coverTileCode);
		if (coverTileCode == 0) {
			imageView.setVisibility(View.GONE);
		}
		return imageView;
	}

	public void setMatrixContent(MatrixContent matrixContent) {
		this.matrixContent = matrixContent;
		this.tileCount = matrixContent.getMatrixContent().length;
	}

}