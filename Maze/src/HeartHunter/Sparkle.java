package HeartHunter;

import HeartHunter.bases.Entity;
import HeartHunter.bases.TileMap;

/**
 * Simple class that plays animation once and is removed.
 */
public class Sparkle extends Entity {
	private boolean remove;

	public Sparkle(TileMap tm) {
		super(tm);
		animation.setFrames(Content.SPARKLE[0]);
		animation.setDelay(5);
		width = height = 16;
	}

	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public void update() {
		animation.update();
		if (animation.hasPlayedOnce())
			remove = true;
	}
}
