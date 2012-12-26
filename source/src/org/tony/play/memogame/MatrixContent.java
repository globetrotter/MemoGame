package org.tony.play.memogame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MatrixContent {

	private static Configuration CFG = new Configuration();

	private int[] matrixContent;

	public MatrixContent() {
	}

	public void generateMatrixContent(String difficulty) {

		int k = 0;

		if (CFG.DIFF_EASY.equals(difficulty)) {
			matrixContent = new int[CFG.getTileCountEasy()];
			ArrayList<Integer> shuffledImages = new ArrayList<Integer>();
			for (int j = 0; j < CFG.getTileCountEasy(); j++) {
				shuffledImages.add(CFG.IMAGES.get(j / 2));
			}
			Random r = new Random();
			Collections.shuffle(shuffledImages, r);
			for (int i = 0; i < CFG.getTileCountEasy(); i++) {
				if (CFG.BLANKS_EASY[i] == 0) {
					matrixContent[i] = 0;
				} else {
					matrixContent[i] = shuffledImages.get(k);
					k++;
				}
			}
		}

		if (CFG.DIFF_NORMAL.equals(difficulty)) {
			matrixContent = new int[CFG.getTileCountNormal()];
			ArrayList<Integer> shuffledImages = new ArrayList<Integer>();
			for (int j = 0; j < CFG.getTileCountNormal(); j++) {
				shuffledImages.add(CFG.IMAGES.get(j / 2));
			}
			Random r = new Random();
			Collections.shuffle(shuffledImages, r);
			for (int i = 0; i < CFG.getTileCountNormal(); i++) {
				if (CFG.BLANKS_NORMAL[i] == 0) {
					matrixContent[i] = 0;
				} else {
					matrixContent[i] = shuffledImages.get(k);
					k++;
				}
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
		if (CFG.DIFF_EASY.equals(difficulty)) {
			if (CFG.BLANKS_EASY[position] == 0) {
				cover = 0;
			}
		}
		if (CFG.DIFF_NORMAL.equals(difficulty)) {
			if (CFG.BLANKS_NORMAL[position] == 0) {
				cover = 0;
			}
		}
		return cover;
	}
}
