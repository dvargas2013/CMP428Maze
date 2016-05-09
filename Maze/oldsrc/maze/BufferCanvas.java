package maze;

import java.awt.*;

/**
 * Initialize and start();
 * 
 * @author danv
 */

public abstract class BufferCanvas extends Canvas implements Runnable {
	private static final long serialVersionUID = 6673547979472855176L;

	protected boolean on = false;

	protected int width, height;
	private Thread t;

	public void start() {
		if (!on) {
			on = true;
		}
	}

	public void stop() {
		if (on) {
			on = false;
		}
	}

	public BufferCanvas(int w, int h) {
		width = w;
		height = h;

		Frame frame = new Frame();
		frame.setIgnoreRepaint(true);
		frame.setUndecorated(true);
		frame.add(this);
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.createBufferStrategy(2);
		frame.setResizable(false);

		t = new Thread(this);
		t.start();
	}

	@Override
	public void paint(Graphics g) {
		try {
			painting(g);
		} catch (NullPointerException e) {
		}
	}

	/**
	 * If you need a graphics object you do it here ;P
	 * 
	 * @param g
	 *            The Graphics object same as paint() method.
	 */
	public abstract void painting(Graphics g);

	/**
	 * This should be where you do things like user input checking and AI and
	 * all the movements and collision checks stuff like that.
	 */
	public abstract void prepaint();

	/**
	 * When it is off it waits 500 milliseconds When it's on it draws the canvas
	 * and waits 16.666666666 milliseconds.
	 */
	public void run() {
		while (true) {// GameLoop
			if (on) {
				prepaint();
				repaint();
				try {
					Thread.sleep((long) (16 + 2 / 3.0));
				} catch (InterruptedException e) {
				}
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
