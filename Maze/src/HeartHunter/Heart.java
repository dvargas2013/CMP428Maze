package hearthunter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import hearthunter.bases.Entity;
import hearthunter.bases.TileMap;

/**
 * May contain a list of tileChanges.
 * 
 * These tileChanges are used to modify the tile map upon collection.
 */
public class Heart extends Entity {

	BufferedImage[] sprites;

	private ArrayList<int[]> tileChanges;

	public Heart(TileMap tm) {
		super(tm);

		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;

		sprites = Content.HEART[0];
		animation.setFrames(sprites);
		animation.setDelay(10);

		tileChanges = new ArrayList<int[]>();
	}

	public void addChange(int[] i) {
		tileChanges.add(i);
	}

	public ArrayList<int[]> getChanges() {
		return tileChanges;
	}

	@Override
	public void update() {
		animation.update();
	}
}
