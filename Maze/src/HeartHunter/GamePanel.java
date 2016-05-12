package HeartHunter;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import HeartHunter.bases.JukeBox;
import HeartHunter.bases.Keys;
import bases.MainFrame;
import bases.MiniGame;

/**
 * The GamePanel is the drawing canvas.
 * This class is also the one that grabs key events.
 */
public class GamePanel extends MiniGame implements KeyListener {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 128;
	public static final int HEIGHT = 100;
	public static final int HEIGHT2 = HEIGHT + 8;
	public static final int SCALE = 4;
	private PlayState playstate;

	public GamePanel(MainFrame parent) {
		super(parent);
		JukeBox.init();
		playstate = new PlayState(this);
	}

	@Override
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}

	@Override
	public void gainControl() {
		parent.addKeyListener(this);
	}

	@Override
	public void loseControl() {
		parent.removeKeyListener(this);
	}

	@Override
	public void painting(Graphics g) {
		if (playstate != null) {
			playstate.draw(g);
		}
	}

	@Override
	public void prepaint() {
		if (playstate != null) {
			playstate.update();
		}
		Keys.update();
	}
}
