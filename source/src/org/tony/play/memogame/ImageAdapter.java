package org.tony.play.memogame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {

	private static final String DIFF_NORMAL = "normal";
	private Context context;
	private int tileCount;
	private String gameDifficulty = DIFF_NORMAL;
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
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(context);
//			imageView.setPadding(10, 10, 0, 0);
		    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//		    imageView.setLayoutParams(new GridView.LayoutParams(180, 155));
		    imageView.setLayoutParams(new GridView.LayoutParams(250, 260));
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