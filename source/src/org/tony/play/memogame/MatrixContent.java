package org.tony.play.memogame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MatrixContent {
	private static final int ROWS = 7;
	private static final int COLS = 6;
	private static final ArrayList<Integer> IMAGES = new ArrayList<Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 22604654732086531L;

		{
			add(R.drawable.fruits1);
			add(R.drawable.fruits2);
			add(R.drawable.fruits3);
			add(R.drawable.fruits4);
			add(R.drawable.fruits5);
			add(R.drawable.fruits6);
			add(R.drawable.fruits7);
			add(R.drawable.fruits8);
			add(R.drawable.fruits9);
			add(R.drawable.fruits10);
			add(R.drawable.fruits11);
			add(R.drawable.fruits12);
			add(R.drawable.fruits13);
			add(R.drawable.fruits14);
			add(R.drawable.fruits15);
			add(R.drawable.flower1);
			add(R.drawable.flower2);
			add(R.drawable.flower3);
			add(R.drawable.flower4);
			add(R.drawable.flower5);
			add(R.drawable.flower6);

		}
	};

	private static final String DIFF_EASY = "easy";
	private static final String DIFF_NORMAL = "normal";
	private static final String DIFF_HARD = "hard";

	private static final int TILE_COUNT_EASY = 12;
	private static final int TILE_COUNT_NORMAL = 30;
	private static final int TILE_COUNT_HARD = 42;

	private static final int[] BLANKS_EASY = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, };

	private static final int[] BLANKS_NORMAL = { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 0, 0, 0, 0, 0, 0, };

	private int[] matrixContent = new int[ROWS * COLS];

	public MatrixContent() {
	}

	public void generateMatrixContent(String difficulty) {

		int k = 0;
		matrixContent = new int[ROWS * COLS];

		if (DIFF_EASY.equals(difficulty)) {
			ArrayList<Integer> shuffledImages = new ArrayList<Integer>();
			for (int j = 0; j < TILE_COUNT_EASY; j++) {
				shuffledImages.add(IMAGES.get(j / 2));
			}
			Random r = new Random();
			Collections.shuffle(shuffledImages, r);
			for (int i = 0; i < ROWS * COLS; i++) {
				if (BLANKS_EASY[i] == 0) {
					matrixContent[i] = 0;
				} else {
					matrixContent[i] = shuffledImages.get(k);
					k++;
				}
			}
		}

		if (DIFF_NORMAL.equals(difficulty)) {
			ArrayList<Integer> shuffledImages = new ArrayList<Integer>();
			for (int j = 0; j < TILE_COUNT_NORMAL; j++) {
				shuffledImages.add(IMAGES.get(j / 2));
			}
			Random r = new Random();
			Collections.shuffle(shuffledImages, r);
			for (int i = 0; i < ROWS * COLS; i++) {
				if (BLANKS_NORMAL[i] == 0) {
					matrixContent[i] = 0;
				} else {
					matrixContent[i] = shuffledImages.get(k);
					k++;
				}
			}
		}

		if (DIFF_HARD.equals(difficulty)) {
			ArrayList<Integer> shuffledImages = new ArrayList<Integer>();
			for (int j = 0; j < TILE_COUNT_HARD; j++) {
				shuffledImages.add(IMAGES.get(j / 2));
			}
			Random r = new Random();
			Collections.shuffle(shuffledImages, r);
			for (int i = 0; i < ROWS * COLS; i++) {
				matrixContent[i] = shuffledImages.get(k);
				k++;
			}
		}
	}

	public int[] getMatrixContent() {
		return matrixContent;
	}

	public int getContentAt(int position) {
		return matrixContent[position];
	}

	public int getCoverTile(String difficulty, int position) {
		int cover = GameActivity.BACK_CARD;
		if (DIFF_EASY.equals(difficulty)) {
			if (BLANKS_EASY[position] == 0) {
				cover = 0;
			}
		}
		if (DIFF_NORMAL.equals(difficulty)) {
			if (BLANKS_NORMAL[position] == 0) {
				cover = 0;
			}
		}
		return cover;
	}
}
