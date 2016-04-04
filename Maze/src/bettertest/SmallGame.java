package bettertest;

import java.awt.Graphics;

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
public class SmallGame extends MiniGame {
	private static final long serialVersionUID = 1L;
	Listener listen = new Listener();

	public SmallGame(MainFrame main) {
		super(main);
	}

	@Override
	public void painting(Graphics g) {
		g.drawString("If You Click You Will Lose", parent.width / 2, parent.height / 2);
	}

	@Override
	public void prepaint() {
		if (listen.mousePressed) {
			gameOver();
			// Then parent is notified to do what it has to do.
			// The parent then calls your loseControl() function and removes you from itself.
		}
	}

	@Override
	public void loseControl() {
		parent.removeMouseListener(listen);
	}

	@Override
	public void gainControl() {
		parent.addMouseListener(listen);
	}

}
