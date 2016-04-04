package bettertest;

import java.awt.event.*;

/**
 * This is the Listener I put in my games. It doesn't do much. Feel Free to Make
 * your own action listeners instead.
 * 
 * @author danv
 */
public class Listener implements MouseListener, KeyListener {
	public boolean DOWN, UP, LEFT, RIGHT;
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
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			DOWN = true;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			UP = true;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			LEFT = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			RIGHT = true;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			DOWN = false;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			UP = false;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			LEFT = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			RIGHT = false;
			break;
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
}
