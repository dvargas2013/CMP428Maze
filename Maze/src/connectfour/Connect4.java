package connectfour;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import bases.MainFrame;
import bases.MiniGame;

public class Connect4 extends MiniGame implements MouseListener {
	public static final int BLANK = 0;
	public static final int YOU = 1;
	public static final int COMP = 2;
	public static final int MAXROW = 6; // 6 rows
	public static final int MAXCOL = 7; // 5 columns
	public final static int PLAYING = 0, TIE = -2, WON = 1, LOSS = -1;
	
	private int activeColour;
	private int difficulty;
	private int state;
	private int[][] theArray;
	private boolean end = false;
	private int wait = 0; // just a little variable for prepaint
	private static int square=100,circle=70,circles=55;
	
	public Connect4(MainFrame main) {
		super(main);
		initialize();
	}
	
	public void initialize() {
		square = Math.min(getWidth(),getHeight()) / 5;
		circle = (int)(square * .7);
		circles = (int)(square * .55);
		activeColour = (Math.random() < .5)?COMP:YOU;
		difficulty = Math.random()<.2?5:6;
		System.out.println("Difficulty:"+difficulty);
		state = PLAYING;
		theArray = new int[MAXROW][MAXCOL];
		for (int row = 0; row < MAXROW; row++)
			for (int col = 0; col < MAXCOL; col++)
				theArray[row][col] = BLANK;
	}
	
	@Override
	public void painting(Graphics g) {
		g.setColor(activeColour == YOU ? Color.GRAY : Color.BLACK);
		g.fillRect(0, 50, 10 + square * MAXCOL, square/2 + square * MAXROW);
		for (int row = 0; row < MAXROW; row++) {
			for (int col = 0; col < MAXCOL; col++) {
				if (theArray[row][col] == BLANK)
					g.setColor(Color.WHITE);
				if (theArray[row][col] == YOU)
					g.setColor(Color.RED);
				if (theArray[row][col] == COMP)
					g.setColor(Color.BLUE);
				g.fillOval(15 + circle * col, circle + circle * row, circles, circles);
			}
		}
		displayWinner(g);
	}

	// Display the color of the winner
	public void displayWinner(Graphics g) {
		g.setColor(Color.PINK);
		g.setFont(new Font("Courier", Font.BOLD, 50));
		String str = "";
		switch (state) {
		case (WON):
			str = "You win!";
			end = true;
			break;
		case (LOSS):
			str = "Computer wins!";
			break;
		case (TIE):
			str = "It's a tie!";
			break;
		}
		g.drawString(str, getWidth() / 2 - g.getFontMetrics().stringWidth(str) / 2, 400);
	}

	@Override
	public void prepaint() {
		if (end) {
			if (wait < 50) {
				wait++;
			} else {
				gameOver();
			}
		} else if (activeColour == COMP) {
			if (wait < 5) {
				wait++;
			} else {
				makeMove();
				wait = 0;
			}
		}
	}

	@Override
	public void loseControl() {
		parent.removeMouseListener(this);
	}

	@Override
	public void gainControl() {
		parent.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (activeColour == YOU) {
			int butNum = e.getX() / (getWidth() / MAXCOL) + 1;
			putDisk(butNum);
		}
	}

	/**
	 * put a disk on top of column n, if a player wins the game, do nothing
	 * 
	 * @param n
	 *            - a number from 1 to MAXCOL inclusive
	 */
	public void putDisk(int n) {
		if (end) {
			return;
		}
		if (state != PLAYING) {
			initialize();
			return;
		}

		int row;
		n--;
		for (row = 0; row < MAXROW; row++)
			if (theArray[row][n] > 0)
				break;

		if (row > 0) { // something has to be inserted
			theArray[--row][n] = activeColour;

			if (check4()) {
				if (activeColour == YOU) {
					state = WON;
				} else {
					state = LOSS;
				}
			}

			if (activeColour == YOU)
				activeColour = COMP;
			else
				activeColour = YOU;
		} else {
			checkOtherRows();
		}
	}

	private void checkOtherRows() {
		boolean oneisempty = false;
		for (int col = 0; col < MAXCOL; col++) {
			if (theArray[0][col] == BLANK) {
				oneisempty = true;
				break;
			}
		}

		if (!oneisempty)
			state = TIE;
	}

	// Check the Columns to see who wins by checking for 4 disks in a row:
	// horizontal, vertical or diagonal
	public boolean check4() {
		// horizontal rows
		for (int row = 0; row < MAXROW; row++) {
			for (int col = 0; col < MAXCOL - 3; col++) {
				int curr = theArray[row][col];
				if (curr > 0 && curr == theArray[row][col + 1] && curr == theArray[row][col + 2]
						&& curr == theArray[row][col + 3]) {
					return true;
				}
			}
		}
		// vertical columns
		for (int col = 0; col < MAXCOL; col++) {
			for (int row = 0; row < MAXROW - 3; row++) {
				int curr = theArray[row][col];
				if (curr > 0 && curr == theArray[row + 1][col] && curr == theArray[row + 2][col]
						&& curr == theArray[row + 3][col]) {
					return true;
				}
			}
		}
		// diagonal lower left to upper right
		for (int row = 0; row < MAXROW - 3; row++) {
			for (int col = 0; col < MAXCOL - 3; col++) {
				int curr = theArray[row][col];
				if (curr > 0 && curr == theArray[row + 1][col + 1] && curr == theArray[row + 2][col + 2]
						&& curr == theArray[row + 3][col + 3]) {
					return true;
				}
			}
		}
		// diagonal upper left to lower right
		for (int row = MAXROW - 1; row >= 3; row--) {
			for (int col = 0; col < MAXCOL - 3; col++) {
				int curr = theArray[row][col];
				if (curr > 0 && curr == theArray[row - 1][col + 1] && curr == theArray[row - 2][col + 2]
						&& curr == theArray[row - 3][col + 3]) {
					return true;
				}
			}
		}

		return false;
	} // End of the checking if any 4 matches

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	private void makeMove() {
		int i = AI.getSmartacularMove(theArray, difficulty);
		System.out.println(i);
		putDisk(i);
	}
}
