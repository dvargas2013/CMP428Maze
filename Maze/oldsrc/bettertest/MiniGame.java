package bettertest;

import java.awt.*;

/**
 * Canvas that saves parent Frame
 * 
 * @author danv
 */
abstract public class MiniGame extends Canvas {
	private static final long serialVersionUID = 1L;
	MainFrame parent;

	public MiniGame(MainFrame main) {
		parent = main;
	}

	/**
	 * If you need a graphics object you do it here ;P
	 * 
	 * @param g
	 *            The Graphics object same as a paint() method.
	 */
	public abstract void painting(Graphics g);

	/**
	 * This should be where you should do things like user input checking and AI
	 * and all the movements and collision checks stuff like that.
	 */
	public abstract void prepaint();

	/**
	 * This is where you should remove your listeners from the parent
	 */
	public abstract void loseControl();

	/**
	 * This is where you should add your listeners to the parent
	 */
	public abstract void gainControl();
	

	public void gameOver() {
		parent.gainControl();
	}
}
