package maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import bases.MainFrame;
import bases.MiniGame;
import min.SnakeGame;

public class Maze extends MiniGame implements KeyListener {
	public boolean DOWN, UP, LEFT, RIGHT;

	MazeData maze = new MazeData(36, 36, 1, 1, 10);
	int x, y;

	private static final long serialVersionUID = 1L;

	public Maze(MainFrame main) {
		super(main);
		for (int i = 0; i < 10; i++) {
			switch( (int)Math.random()*5 ) {
			case 0: maze.addMonster(new Enemy(new SnakeGame(parent)));
			default: maze.addMonster(new Enemy(new SmallGame(parent)));
			}
		}
	}

	@Override
	public void painting(Graphics g) {
		maze.draw(g, getWidth() / 2 - x, getHeight() / 2 - y);
		if (!winner) {
			g.setColor(Color.RED);
			g.fillOval(getWidth() / 2, getHeight() / 2, csize, csize);
		}
	}

	private int csize = 10;
	private int moveby = 5;
	private boolean winner = false;

	@Override
	public void prepaint() {
		if (winner) {
			if (DOWN) {
				maze.centerJ += 1;
			}
			if (UP) {
				maze.centerJ -= 1;
			}
			if (LEFT) {
				maze.centerI -= 1;
			}
			if (RIGHT) {
				maze.centerI += 1;
			}
			return;
		}

		if (maze.win()) {
			maze.blocksize = (int) Math.min(getWidth() / maze.I / 1.2, getHeight() / maze.J / 1.2);
			maze.centerI = maze.I / 2;
			maze.centerJ = maze.J / 2;
			winner = true;
		} else {
			if (DOWN) {
				y += moveby;
				if (y + csize > maze.blocksize) {
					maze.centerJ += 1;
					if (maze.centerIsOpaque()) {
						maze.centerJ -= 1;
						y -= moveby;
					} else {
						y -= maze.blocksize;
					}
				}
			}
			if (UP) {
				y -= moveby;
				if (y < 0) {
					maze.centerJ -= 1;
					if (maze.centerIsOpaque()) {
						maze.centerJ += 1;
						y += moveby;
					} else {
						y += maze.blocksize;
					}
				}
			}
			if (LEFT) {
				x -= moveby;
				if (x < 0) {
					maze.centerI -= 1;
					if (maze.centerIsOpaque()) {
						maze.centerI += 1;
						x += moveby;
					} else {
						x += maze.blocksize;
					}
				}
			}
			if (RIGHT) {
				x += moveby;
				if (x + csize > maze.blocksize) {
					maze.centerI += 1;
					if (maze.centerIsOpaque()) {
						maze.centerI -= 1;
						x -= moveby;
					} else {
						x -= maze.blocksize;
					}
				}
			}
			maze.touch();
		}
	}

	@Override
	public void loseControl() {
		parent.removeKeyListener(this);
	}

	@Override
	public void gainControl() {
		DOWN = UP = LEFT = RIGHT = false;
		parent.addKeyListener(this);
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
}
