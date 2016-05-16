package maze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import bases.MainFrame;
import bases.MiniGame;

public class Connect4 extends MiniGame implements MouseListener {
	private static final long serialVersionUID = 1L;
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int YELLOW = 2;
	public static final int MAXROW = 6; // 6 rows
	public static final int MAXCOL = 7; // 5 columns
	public final static int PLAYING = 0, TIE = -2, WON = 1, LOSS = -1;
	
	private int activeColour;
	private int state;
	private int[][] theArray;
	private boolean end = false;

	public Connect4(MainFrame main) {
		super(main);
		initialize();
	}

	@Override
	public void painting(Graphics g) {
		// for (int i = 0; i <MAXCOL; i++) {
		// int x = i * ( getWidth() / MAXCOL );
		// g.drawLine(x, 0, x, getHeight());
		// }
		g.setColor(Color.BLACK);
		g.fillRect(0, 50, 10 + 100 * MAXCOL, 50 + 100 * MAXROW);
		for (int row = 0; row < MAXROW; row++) {
			for (int col = 0; col < MAXCOL; col++) {
				if (theArray[row][col] == BLANK)
					g.setColor(Color.WHITE);
				if (theArray[row][col] == RED)
					g.setColor(Color.RED);
				if (theArray[row][col] == YELLOW)
					g.setColor(Color.YELLOW);
				g.fillOval(15 + 70 * col, 70 + 70 * row, 55, 55);
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
		g.drawString(str, getWidth()/2 - g.getFontMetrics().stringWidth(str)/2, 400);
	}
	
	@Override
	public void prepaint() {
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
		int butNum = e.getX() / (getWidth() / MAXCOL) + 1;
		putDisk(butNum);
	}

	// initialize the game to start with Blank
	public void initialize() {
		activeColour = RED;
		state = PLAYING;
		theArray = new int[MAXROW][MAXCOL];
		for (int row = 0; row < MAXROW; row++)
			for (int col = 0; col < MAXCOL; col++)
				theArray[row][col] = BLANK;
	}

	/**
	 * put a disk on top of column n, if a player wins the game, do nothing
	 * 
	 * @param n
	 *            - a number from 1 to MAXCOL inclusive
	 */
	public void putDisk(int n) {
		if (end) {
			gameOver();
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
				if (activeColour == RED) {
					state = WON;
				} else {
					state = LOSS;
				}
			}
			
			if (activeColour == RED)
				activeColour = YELLOW;
			else
				activeColour = RED;
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
}
