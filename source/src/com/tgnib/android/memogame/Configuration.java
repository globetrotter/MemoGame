package com.tgnib.android.memogame;

import java.util.ArrayList;

/**
 * contains matrix configuration.
 * 
 * @author globetrotter
 *
 */
public class Configuration {

	protected final int TILE_COUNT_EASY = 12;
	protected final int TILE_COUNT_NORMAL = 24;
	protected final int COLUMNS_EASY = 3;
	protected final int COLUMNS_NORMAL = 4;
	protected final String DIFF_EASY = "easy";
	protected final String DIFF_NORMAL = "normal";

	protected final ArrayList<Integer> IMAGES = new ArrayList<Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 22604654732086531L;

		{
			
			add(R.drawable.card_2);
			add(R.drawable.card_3);
			add(R.drawable.card_4);
			add(R.drawable.card_5);
			add(R.drawable.card_6);
			add(R.drawable.card_7);
			add(R.drawable.card_8);
			add(R.drawable.card_9);
			add(R.drawable.card_10);
			add(R.drawable.card_11);
			add(R.drawable.card_14);
			add(R.drawable.card_19);
		}
	};

	public int getTileCountEasy() {
		return TILE_COUNT_EASY;
	}

	public int getTileCountNormal() {
		return TILE_COUNT_NORMAL;
	}
		
}
