package maze;

import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

import bases.MainFrame;
import bases.MiniGame;

public class Enemy<J extends MiniGame> {
	Class<J> game;
	MainFrame parent;
	int i, j;

	public Enemy(MainFrame parent, Class<J> game) {
		this.parent = parent;
		this.game = game;
	}

	public void setLocation(int ii, int jj) {
		i = ii;
		j = jj;
	}

	public void touch() {
		// I am assuming that since J extends MiniGame it needs a constructor
		// with MainFrame in it
		try {
			game.getDeclaredConstructor(MainFrame.class).newInstance(parent).addSelf();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			// But Honestly if it doesn't work it will silently crash and
			// pretend you won the game
		}
	}

	public void draw(Graphics g, int x, int y, int w, int h) {
		g.drawRect(x, y, w, h);
	}
}
