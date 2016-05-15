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

	private Thread t; // the game loop thread
	private boolean on; // thread checks if the game is on
	private MiniGame active; // whatever minigame is being displayed at the
								// moment
	private Stack<MiniGame> previous = new Stack<MiniGame>(); // previous
																// minigames to
																// be added when
																// current one
																// is call
																// gameover

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

	/**
	 * It's a Frame. It has a thread. It's not complicated.
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
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
		} else
			this.gainControl(); // If no previous something needs to happen idk
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
					// but you never know when you'll need it
					active.sleeping();
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
	 * gain control. This gets called when there is nothing left in your
	 * previous stack of minigames.
	 */
	public abstract void gainControl();
}
