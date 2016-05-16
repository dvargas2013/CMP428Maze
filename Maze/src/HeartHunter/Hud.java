package hearthunter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Contains a reference to the Player. 
 * Draws all relevant information at the bottom of the screen.
 */
public class Hud {

	private int yoffset;
	private int yoffscreen;
	private int counter;

	private BufferedImage bar;
	private BufferedImage hearts;
	private BufferedImage boat;
	private BufferedImage key;

	private Player player;

	private int numHearts;

	private Font font;
	private Color textColor;

	public Hud(Player p, ArrayList<Heart> d) {

		player = p;
		numHearts = d.size();
		yoffset = GamePanel.HEIGHT2;
		yoffscreen = yoffset + 32 * GamePanel.SCALE;
		counter = 0;

		bar = Content.BAR[0][0];
		hearts = Content.HEART[0][0];
		boat = Content.ITEMS[0][0];
		key = Content.ITEMS[0][1];

		font = new Font("Arial", Font.PLAIN, 10 * GamePanel.SCALE);
		textColor = new Color(47, 64, 126);

	}

	public void show() {
		yoffset = GamePanel.HEIGHT2;
		counter = 0;
	}

	public void hide() {
		yoffset = yoffscreen;
	}

	public void draw(Graphics g) {
		if (yoffset != yoffscreen) { // If Not hidden
			if (counter > 150)
				hide();
			else
				counter++;
		}

		// draw hud
		g.drawImage(bar, 0, yoffset * GamePanel.SCALE, bar.getWidth() * GamePanel.SCALE,
				bar.getHeight() * GamePanel.SCALE, null);

		// draw diamond bar
		g.setColor(textColor);
		g.fillRect(8 * GamePanel.SCALE, (yoffset + 6) * GamePanel.SCALE,
				(int) ((28.0 * player.numHearts() / numHearts) * GamePanel.SCALE), 4 * GamePanel.SCALE);

		// draw diamond amount
		g.setColor(textColor);
		g.setFont(font);
		String s = player.numHearts() + "/" + numHearts;
		Content.drawString(g, s, 40, yoffset + 3);
		if (player.numHearts() >= 10)
			g.drawImage(hearts, 80 * GamePanel.SCALE, yoffset * GamePanel.SCALE, hearts.getWidth() * GamePanel.SCALE,
					hearts.getHeight() * GamePanel.SCALE, null);
		else
			g.drawImage(hearts, 72 * GamePanel.SCALE, yoffset * GamePanel.SCALE, hearts.getWidth() * GamePanel.SCALE,
					hearts.getHeight() * GamePanel.SCALE, null);

		// draw items
		if (player.hasBoat())
			g.drawImage(boat, 100 * GamePanel.SCALE, yoffset * GamePanel.SCALE, boat.getWidth() * GamePanel.SCALE,
					boat.getHeight() * GamePanel.SCALE, null);
		if (player.hasKey())
			g.drawImage(key, 112 * GamePanel.SCALE, yoffset * GamePanel.SCALE, key.getWidth() * GamePanel.SCALE,
					key.getHeight() * GamePanel.SCALE, null);
	}

}
