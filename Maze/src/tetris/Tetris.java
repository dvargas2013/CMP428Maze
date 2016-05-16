package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import bases.MainFrame;
import bases.MiniGame;

public class Tetris extends MiniGame implements KeyListener {
	public Tetris(MainFrame main) {
		super(main);
		setBounds();
		init();
	}
	
	private void setBounds() {
		HEIGHT = getHeight();
		WIDTH = getWidth();
		BLOCK_SIZE = (int)(Math.min(WIDTH,HEIGHT) / 20.8);
		X_OFFSET = WIDTH / 25;
		Y_OFFSET = HEIGHT / 50;
		NEXTPIECEX = 18*WIDTH/25;
		NEXTPIECEY = 2*HEIGHT/25;
	}
	
	private void init() {
		count = 0;
		score -= 500;
		if (score<0) score=0;
		gameOver = false;
		currentPiece = new Piece();
		nextPiece = new Piece();
		board = new Board();
		timer = new Timer(); // Calls timer() every second
		timerTask = new TetrisTimer(this);
		timer.scheduleAtFixedRate(timerTask, 1000, 1000);
	}

	private static int HEIGHT = 500, WIDTH = 500, BLOCK_SIZE = 24;
	private static int X_OFFSET = 20, Y_OFFSET = 10, NEXTPIECEX = 360, NEXTPIECEY = 40;
	private final static Color[] colors = { Color.WHITE, Color.ORANGE, Color.GREEN, Color.CYAN, Color.MAGENTA,
			Color.BLUE, Color.YELLOW, Color.RED };
	
	private Piece currentPiece, nextPiece;
	private Board board;
	private int level;
	private double score;
	private int lines = 0;
	private TetrisTimer timerTask;
	private Timer timer;
	private boolean gameOver;
	private int count;

	private void drawPiece(Graphics g) {
		int x = currentPiece.getxPos();
		int y = currentPiece.getyPos();
		for (int j = 0; j < Piece.PIECE_SIZE; j++){
			for (int i = 0; i < Piece.PIECE_SIZE; i++) {
				if (currentPiece.getBlock(j, i) != 0) {
					g.setColor(colors[currentPiece.getBlock(j, i)]);
					g.fillRect(X_OFFSET + (x + i) * BLOCK_SIZE, Y_OFFSET + (y + j) * BLOCK_SIZE, BLOCK_SIZE,
							BLOCK_SIZE);
				}
			}
		}
	}

	private void drawNextPiece(Graphics g) {
		for (int j = 0; j < Piece.PIECE_SIZE; j++)
			for (int i = 0; i < Piece.PIECE_SIZE; i++) {
				g.setColor(colors[nextPiece.getBlock(j, i)]);
				g.fillRect(NEXTPIECEX + i * BLOCK_SIZE, NEXTPIECEY + j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
			}
	}

	private void drawBoard(Graphics g) {
		for (int j = 0; j < Board.HEIGHT; j++) {
			for (int i = 0; i < Board.WIDTH; i++) {
				g.setColor(colors[board.getBlock(j, i)]);
				g.fillRect(X_OFFSET + i * BLOCK_SIZE, Y_OFFSET + j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_DOWN:
			if (board.canMove(currentPiece, 0, 1)) {
				currentPiece.moveDown();
				score += 10 + level;
			} else
				updateBoard();
			break;
		case KeyEvent.VK_LEFT:
			if (board.canMove(currentPiece, -1, 0))
				currentPiece.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			if (board.canMove(currentPiece, 1, 0))
				currentPiece.moveRight();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			if (board.canMove(currentPiece, 0, -1))
				currentPiece.rotate();
			break;
		case KeyEvent.VK_SPACE:
			while (board.canMove(currentPiece, 0, 1)) {
				currentPiece.moveDown();
				score += 10 + level;
			}
			updateBoard();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void timer() {
		if (board.canMove(currentPiece, 0, 1))
			currentPiece.moveDown();
		else
			updateBoard();
	}

	private void updateBoard() {
		board.store(currentPiece);
		currentPiece = nextPiece;
		nextPiece = new Piece();
		int num = board.deletePossibleLines();
		lines += num;
		score += ((num - 1) / 10.0 * num + num) * (Board.WIDTH * 10) + level * 10;
		level = (int) (score / 10000) + 1;
		if (!board.canMove(currentPiece, 0, 1))
			gameOver = true;
	}

	@Override
	public void painting(Graphics g) {
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.drawString("Next Piece:", NEXTPIECEX, NEXTPIECEY - 10);
		g.drawString("Score: " + (int) score, NEXTPIECEX, HEIGHT/2);
		g.drawString("Lines: " + lines, NEXTPIECEX, HEIGHT/2+40);
		g.drawString("Level: " + level, NEXTPIECEX, HEIGHT/2+80);

		drawBoard(g);
		drawPiece(g);
		drawNextPiece(g);

		if (gameOver) {
			timerTask.cancel();
			g.setColor(Color.GRAY);
			g.fillRect(X_OFFSET + 2 * BLOCK_SIZE, Y_OFFSET + 8 * BLOCK_SIZE, 6 * BLOCK_SIZE, 4 * BLOCK_SIZE);
			g.setColor(Color.WHITE);
			g.drawString("GAME OVER", X_OFFSET + 3 * BLOCK_SIZE, Y_OFFSET + 10 * BLOCK_SIZE - 10);
			g.drawString("Score: " + (int) score, X_OFFSET + 3 * BLOCK_SIZE, Y_OFFSET + 11 * BLOCK_SIZE - 10);
		}
	}

	@Override
	public void prepaint() {
		if (gameOver) {
			count ++;
			if (count > 70) {
				if (lines > 4 && score > 5000) {
					gameOver();
				} else {
					init();
				}
			}
		}
	}

	@Override
	public void loseControl() {
		parent.removeKeyListener(this);
	}

	@Override
	public void gainControl() {
		parent.addKeyListener(this);
	}

}
