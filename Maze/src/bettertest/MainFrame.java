package bettertest;

import java.awt.*;

/**
 * This is the BackBone Frame that holds everything together. It is a
 * Container.Frame that holds a Thread every 16millis
 * 
 * Don't forget to start() the Frame. If you don't the Thread runs twice per second
 * waiting for start()
 * 
 * @author danv
 */
public abstract class MainFrame extends Frame implements Runnable {
	private static final long serialVersionUID = 1L;

	protected int width, height;
	private Thread t;
	private boolean on;
	private MiniGame active;

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

	MainFrame(int w, int h) {
		super();
		setIgnoreRepaint(true);
		// frame.setUndecorated(true);
		setSize(w, h);
		width = w;
		height = h;
		setVisible(true);
		createBufferStrategy(2);
		setResizable(false);

		t = new Thread(this);
		t.start();

		stop();
	}

	public void addCanvas(MiniGame mg) {
		if (active != null) {
			remove(active);
			active.loseControl();
		}

		add(mg);
		mg.gainControl();

		active = mg;
	}

	@Override
	public void run() {
		while (true) {// GameLoop
			if (on) { // If the Frame is on Draw the active Canvas
				active.prepaint();
				repaint();
				try {
					Thread.sleep((long) (16 + 2 / 3.0));
				} catch (InterruptedException e) {
				}
			} else { // If Frame is off wait for the start() command.
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		try {
			active.painting(g);
		} catch (NullPointerException e) {
		}
	}

	/**
	 * I recommend having a main Canvas that you add back to the Frame when you
	 * gain control. 
	 */
	public abstract void gainControl();
}
