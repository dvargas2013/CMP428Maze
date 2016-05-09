package maze;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bases.MainFrame;
import bases.MiniGame;

/**
 * Super small game. You click and you lose muahaha.
 * 
 * Basically showing you that you can implement your own action listeners and
 * add them to the parent as you please.
 * 
 * You have your own scope to play your game as you please. All you have to do
 * is promise to call gameOver() when you are done. ^v^
 * 
 * Also don't forget to remove your action listeners from the parent frame. 
 * 
 * @author danv
 */
public class SmallGame extends MiniGame implements MouseListener {
	public MouseEvent lastPress = null;
	public boolean mousePressed = false;

	/**
	 * Returns the last time the Mouse was Pressed. If no new MousePressed was
	 * made before you called this it returns null. You could check state of
	 * whether a new press was made with the mousePresed boolean.
	 * 
	 * @return last MouseEvent of last MousePressed
	 */
	public MouseEvent getLastMousePress() {
		if (mousePressed) {
			mousePressed = false;
			return lastPress;
		} else {
			return null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastPress = e;
		mousePressed = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	private static final long serialVersionUID = 1L;

	public SmallGame(MainFrame main) {
		super(main);
	}

	@Override
	public void painting(Graphics g) {
		g.drawString("Click Screen. Much Win.", getWidth() / 2, getHeight() / 2);
	}

	@Override
	public void prepaint() {
		if (mousePressed) {
			gameOver();
			// Then parent is notified to do what it has to do.
			// The parent then calls your loseControl() function and removes you from itself.
		}
	}

	@Override
	public void loseControl() {
		parent.removeMouseListener(this);
	}

	@Override
	public void gainControl() {
		parent.addMouseListener(this);
	}

}
