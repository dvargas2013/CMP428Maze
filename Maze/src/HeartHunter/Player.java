package HeartHunter;

import java.awt.image.BufferedImage;

import HeartHunter.bases.Entity;
import HeartHunter.bases.JukeBox;
import HeartHunter.bases.TileMap;

/**
 * The only subclass the fully utilizes the Entity superclass
 * 
 * (no other class requires movement in a tile based map).
 * 
 * Contains all the gameplay associated with the Player.
 */
public class Player extends Entity {
	// sprites
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	private BufferedImage[] upSprites;
	private BufferedImage[] downBoatSprites;
	private BufferedImage[] leftBoatSprites;
	private BufferedImage[] rightBoatSprites;
	private BufferedImage[] upBoatSprites;

	// animation
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DOWNBOAT = 4;
	private final int LEFTBOAT = 5;
	private final int RIGHTBOAT = 6;
	private final int UPBOAT = 7;

	// gameplay
	private int numHearts;
	private int totalHearts;
	private boolean hasBoat;
	private boolean hasAxe;
	private boolean onWater;
	private long ticks;

	public Player(TileMap tm) {
		super(tm);

		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;

		moveSpeed = 2;

		numHearts = 0;

		downSprites = Content.PLAYER[0];
		leftSprites = Content.PLAYER[1];
		rightSprites = Content.PLAYER[2];
		upSprites = Content.PLAYER[3];
		downBoatSprites = Content.PLAYER[4];
		leftBoatSprites = Content.PLAYER[5];
		rightBoatSprites = Content.PLAYER[6];
		upBoatSprites = Content.PLAYER[7];

		animation.setFrames(downSprites);
		animation.setDelay(10);
	}

	private void setAnimation(int i, BufferedImage[] bi, int d) {
		currentAnimation = i;
		animation.setFrames(bi);
		animation.setDelay(d);
	}

	public void collectedHeart() {
		numHearts++;
	}

	public int numHearts() {
		return numHearts;
	}

	public int getTotalHearts() {
		return totalHearts;
	}

	public void setTotalHearts(int i) {
		totalHearts = i;
	}

	public void gotBoat() {
		hasBoat = true;
		tileMap.replace(22, 4);
	}

	public void gotKey() {
		hasAxe = true;
	}

	public boolean hasBoat() {
		return hasBoat;
	}

	public boolean hasAxe() {
		return hasAxe;
	}

	// Used to update time.
	public long getTicks() {
		return ticks;
	}

	public void setAction() {
		if (hasAxe) {
			if (currentAnimation == UP && tileMap.getIndex(rowTile - 1, colTile) == 21) {
				tileMap.setTile(rowTile - 1, colTile, 1);
				JukeBox.play("tilechange");
			}
			if (currentAnimation == DOWN && tileMap.getIndex(rowTile + 1, colTile) == 21) {
				tileMap.setTile(rowTile + 1, colTile, 1);
				JukeBox.play("tilechange");
			}
			if (currentAnimation == LEFT && tileMap.getIndex(rowTile, colTile - 1) == 21) {
				tileMap.setTile(rowTile, colTile - 1, 1);
				JukeBox.play("tilechange");
			}
			if (currentAnimation == RIGHT && tileMap.getIndex(rowTile, colTile + 1) == 21) {
				tileMap.setTile(rowTile, colTile + 1, 1);
				JukeBox.play("tilechange");
			}
		}
	}

	@Override
	public void update() {
		ticks++;

		// check if on water
		boolean current = onWater;
		if (tileMap.getIndex(ydest / tileSize, xdest / tileSize) == 4) {
			onWater = true;
		} else {
			onWater = false;
		}
		// if going from land to water
		if (!current && onWater)
			JukeBox.play("splash");

		// set animation
		if (down) {
			if (onWater && currentAnimation != DOWNBOAT) {
				setAnimation(DOWNBOAT, downBoatSprites, 10);
			} else if (!onWater && currentAnimation != DOWN) {
				setAnimation(DOWN, downSprites, 10);
			}
		}
		if (left) {
			if (onWater && currentAnimation != LEFTBOAT) {
				setAnimation(LEFTBOAT, leftBoatSprites, 10);
			} else if (!onWater && currentAnimation != LEFT) {
				setAnimation(LEFT, leftSprites, 10);
			}
		}
		if (right) {
			if (onWater && currentAnimation != RIGHTBOAT) {
				setAnimation(RIGHTBOAT, rightBoatSprites, 10);
			} else if (!onWater && currentAnimation != RIGHT) {
				setAnimation(RIGHT, rightSprites, 10);
			}
		}
		if (up) {
			if (onWater && currentAnimation != UPBOAT) {
				setAnimation(UPBOAT, upBoatSprites, 10);
			} else if (!onWater && currentAnimation != UP) {
				setAnimation(UP, upSprites, 10);
			}
		}

		// update position
		super.update();
	}
}