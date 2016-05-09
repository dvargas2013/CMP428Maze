package bases;

import java.awt.Frame;
import java.awt.Graphics;

/**
 * This is the BackBone Frame that holds everything together. It is a
 * Container.Frame that holds a Thread every 16millis <br>
 * <br>
 * Don't forget to start() the Frame. The Thread is running 2x/sec waiting for
 * you to begin.
 * 
 * @author danv
 */
public abstract class MainFrame extends Frame implements Runnable {
	private static final long serialVersionUID = 1L;

	private Thread t;
	private boolean on;
	private MiniGame active;
	private Stack<MiniGame> previous = new Stack<MiniGame>();

	/**
	 * Make the gameloop display the active canvas every 16 milliseconds
	 */
	public void start() {
		if (!on) {
			on = true;
		}
	}

	/**
	 * Make the gameloop call sleeping() every 500 milliseconds
	 */
	public void stop() {
		if (on) {
			on = false;
		}
	}

	public MainFrame(int w, int h) {
		super();
		// Don't draw the first time.
		setIgnoreRepaint(true);
		// I'll tell you when to draw when I'm ready

		setResizable(false);
		setUndecorated(true); // Sorta-full screen type thing
		setSize(w, h);

		t = new Thread(this);
		stop();
		t.start();

		setVisible(true); // Double buffering or whatever requires 2 buffers
		createBufferStrategy(2); // Like Page-flipping
	}

	/**
	 * Remove the active game and add the previous game
	 */
	public void popCanvas() {
		if (active != null) {
			remove(active);
			active.loseControl();
		}

		if (!previous.isEmpty()) {
			active = previous.pop();
			add(active);
			active.gainControl();
		} else {
			this.gainControl(); // If no previous
		}
	}

	/**
	 * Add active game to previous and add the new game
	 * 
	 * @param mg
	 *            Another minigame
	 */
	public void addCanvas(MiniGame mg) {
		if (active != null) {
			remove(active);
			previous.push(active);
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
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					System.out.println(e);
				}

				if (active != null)
					active.prepaint();
				else
					System.out.println("Active is Null");
				repaint();

			} else { // If Frame is off wait for the start() command.
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println(e);
				}

				if (active != null)
					// Doesn't do anything by default
					// but you never know
					active.sleeping(); 
				else
					System.out.println("Active is Null");

			}
		}
	}

	@Override
	public void paint(Graphics g) {
		if (active != null)
			active.painting(g);
		else
			System.out.println("Active is Null");
	}

	/**
	 * I recommend having a main Canvas that you add back to the Frame when you
	 * gain control.
	 */
	public abstract void gainControl();
}
