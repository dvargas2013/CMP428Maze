package bases;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import tetris.Tetris;

/**
 * The MainMiniGame for the MainGameFrame. Click on it 5 times, it changes colors 5
 * times, and it spawns a new MiniGame(SmallGame) on the last click.
 * 
 * @author danv
 */
public class Click5Game extends MiniGame {
	public Click5Game(MainFrame main) {
		super(main);
	}

	Listener listen = new Listener();
	Color c = Color.WHITE;
	Random rand = new Random();
	int clicks;
	MiniGame sg;

	@Override
	public void painting(Graphics g) {
		g.setColor(c);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.drawString("MAIN " + (5 - clicks),getWidth()/ 2, getHeight() / 2);
	}

	@Override
	public void prepaint() {
		if (listen.mousePressed) {
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			c = new Color(r, g, b);
			// make a new color when clicked

			listen.getLastMousePress(); // make the boolean turn off (im using
										// it for side effects sorry)

			clicks += 1; // increment click counter and check if clicked 5x
			if (clicks > 5) {
				sg = new Tetris(parent);
				parent.addCanvas(sg); // if clicked enough make new game and
										// tell parent to play that instead
			}
		}
	}

	@Override
	public void loseControl() {
		parent.removeKeyListener(listen);
		parent.removeMouseListener(listen);
	}

	// remember lose and gain control are called by the parent when
	// transition-ing between games they do what they say

	@Override
	public void gainControl() {
		clicks = 0;
		parent.addKeyListener(listen);
		parent.addMouseListener(listen);
	}
}
