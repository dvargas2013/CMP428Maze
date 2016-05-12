package HeartHunter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import HeartHunter.bases.Entity;
import HeartHunter.bases.TileMap;

/**
 * There are two types of items: Key and boat.
 * 
 * When collected, tells Player.
 */
public class Item extends Entity {
	private BufferedImage sprite;
	private int type;
	public static final int BOAT = 0;
	public static final int KEY = 1;

	public Item(TileMap tm) {
		super(tm);
		type = -1;
		width = height = 16;
		cwidth = cheight = 12;
	}

	public void setType(int i) {
		type = i;
		if (type == BOAT) {
			sprite = Content.ITEMS[1][0];
		} else if (type == KEY) {
			sprite = Content.ITEMS[1][1];
		}
	}

	public void collected(Player p) {
		if (type == BOAT) {
			p.gotBoat();
		}
		if (type == KEY) {
			p.gotKey();
		}
	}

	@Override
	public void draw(Graphics g) {
		setMapPosition();
		g.drawImage(sprite, (x + xmap - width / 2) * GamePanel.SCALE, (y + ymap - height / 2) * GamePanel.SCALE,
				sprite.getWidth() * GamePanel.SCALE, sprite.getHeight() * GamePanel.SCALE, null);
	}

}
