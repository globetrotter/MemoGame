package org.tony.play.memogame;

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
	protected final String DIFF_EASY = "easy";
	protected final String DIFF_NORMAL = "normal";

	protected final int[] BLANKS_EASY = { 
		1, 1, 1, 
		1, 1, 1, 
		1, 1, 1, 
		1, 1, 1
		};

	protected final int[] BLANKS_NORMAL = { 
			1, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1 
			};

	protected final ArrayList<Integer> IMAGES = new ArrayList<Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 22604654732086531L;

		{
			add(R.drawable.card_1);
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
			add(R.drawable.card_12);
			add(R.drawable.card_13);
			add(R.drawable.card_14);
			add(R.drawable.card_15);
			add(R.drawable.card_16);
			add(R.drawable.card_17);
			add(R.drawable.card_18);
			add(R.drawable.card_19);
			add(R.drawable.card_20);
			add(R.drawable.card_3);
			add(R.drawable.card_4);
			add(R.drawable.card_5);
			add(R.drawable.card_6);
		}
	};

	public int getTileCountEasy() {
		return TILE_COUNT_EASY;
	}

	public int getTileCountNormal() {
		return TILE_COUNT_NORMAL;
	}
		
}
