package maze;

import java.awt.*;

public class Game extends BufferCanvas {
	public static void main(String[] args) {
		new Game();
	}

	private static final long serialVersionUID = -4972024197320984485L;

	Listener listen = new Listener();
	Maze maze = new Maze(49, 49);

	public Game() {
		super(490, 490);
		setFocusable(true);
		requestFocus();
		addKeyListener(listen);
		// addMouseListener(listen);
		start();
	}

	public void init() {
	}

	@Override
	public void painting(Graphics g) {
		maze.draw(g);
	}

	public void prepaint() {
		userInput();
		aiInput();
		collisions();
	}

	public void userInput() {
	}

	public void aiInput() {
	}

	public void collisions() {
	}
}
