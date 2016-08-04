package connectfour;

public abstract class AI {
	private static Point vert = new Point(1, 0), horz = new Point(0, 1), diagR = new Point(1, 1),
			diagL = new Point(-1, 1);

	public static int[][] deep_copy(int[][] a) {
		int[][] b = new int[a.length][];
		for (int i = 0; i < a.length; i++)
			b[i] = a[i].clone();
		return b;
	}

	/**
	 * @param len
	 * @return a shuffled int[] with [1,len]
	 */
	public static int[] init(int len) {
		int[] prongs = new int[len];
		for (int i = 0; i < len; prongs[i] = ++i)
			;
		int i = len, r, item;
		while (0 != i) {
			r = (int) (Math.random() * i);
			i -= 1;
			item = prongs[i];
			prongs[i] = prongs[r];
			prongs[r] = item;
		}
		return prongs;
	}

	public static boolean valid(int[][] board, int dir) {
		return dir > 0 && dir - 1 < (board[0].length) && board[0][dir - 1] == Connect4.BLANK;
	}

	/**
	 * @param board
	 * @return the valid board moves shuffled
	 */
	public static int[] moves(int[][] board) {
		int[] lis = init(board[0].length);
		int count = 0;
		for (int i = 0; i < lis.length; i++) {
			if (!valid(board, lis[i])) {
				lis[i] = 0;
			} else {
				count++;
			}
		}
		int[] ret = new int[count];
		count = 0;
		for (int i = 0; i < lis.length; i++) {
			if (lis[i] != 0) {
				ret[count++] = lis[i];
			}
		}
		return ret;
	}

	/**
	 * @param board
	 *            - the board
	 * @param start
	 *            - any spot on the board
	 * @param step
	 *            - direction of check
	 * @return the types of points in the 4 range {blank,comp,you}
	 */
	public static int[] counter(int[][] board, Point start, Point step) {
		int[] acc = { 0, 0, 0 };
		int i, piece;
		for (i = 0; i < 4; i++) {
			piece = board[start.x + i * step.x][start.y + i * step.y];
			if (piece == Connect4.COMP) {
				acc[1]++;
			} else if (piece == Connect4.YOU) {
				acc[2]++;
			} else {
				acc[0]++;
			}
		}

		return acc;
	}

	/**
	 * @param board
	 * @return blank if no one has won. else the integer associated with that
	 *         person.
	 */
	public static int Win(int[][] board) {
		int row, col;
		int[] pieces; // number of
		for (row = 0; row < board.length; row++) {
			for (col = 0; col + 3 < board[row].length; col++) {
				pieces = counter(board, new Point(row, col), horz);
				if (pieces[1] == 4 || pieces[2] == 4)
					return board[row][col];
			}
		}
		for (row = 0; row + 3 < board.length; row++) {
			for (col = 0; col < board[row].length; col++) {
				pieces = counter(board, new Point(row, col), vert);
				if (pieces[1] == 4 || pieces[2] == 4)
					return board[row][col];
			}
		}
		for (row = 3; row < board.length; row++) {
			for (col = 0; col + 3 < board[row].length; col++) {
				pieces = counter(board, new Point(row, col), diagL);
				if (pieces[1] == 4 || pieces[2] == 4)
					return board[row][col];
			}
		}
		for (row = 0; row + 3 < board.length; row++) {
			for (col = 0; col + 3 < board[row].length; col++) {
				pieces = counter(board, new Point(row, col), diagR);
				if (pieces[1] == 4 || pieces[2] == 4)
					return board[row][col];
			}
		}
		return Connect4.BLANK;
	}

	public static int[][] makeMove(int[][] board, int dir, int piece) {
		int i = board.length - 1;
		while (i >= 0 && board[i][dir - 1] != Connect4.BLANK)
			i--;
		if (i < 0)
			return new int[0][0];
		int[][] newBoard = deep_copy(board);
		newBoard[i][dir - 1] = piece;
		return newBoard;
	}

	public static int _score(int[] pieces) {
		if (pieces[1] == 4)
			return 1000;
		if (pieces[2] == 4)
			return -1000;
		if (pieces[0] == 1) {
			if (pieces[1] == 3)
				return 50;
			else // if (pieces[2] == 3) {
				return -50;
		} else if (pieces[0] == 2) {
			if (pieces[1] == 2)
				return 10;
			else // if (pieces[2] == 2) {
				return -10;
		}
		return 0;
	}

	public static int score(int[][] board) {
		int row, col, num, acc = 0;
		for (row = 0; row < board.length; row++) {
			for (col = 0; col + 3 < board[row].length; col++) {
				num = _score(counter(board, new Point(row, col), horz));
				if (Math.abs(num) > 500)
					return num;
				acc += num;
			}
		}
		for (row = 0; row + 3 < board.length; row++) {
			for (col = 0; col < board[row].length; col++) {
				num = _score(counter(board, new Point(row, col), vert));
				if (Math.abs(num) > 500)
					return num;
				acc += num;
			}
		}
		for (row = 3; row < board.length; row++) {
			for (col = 0; col + 3 < board[row].length; col++) {
				num = _score(counter(board, new Point(row, col), diagL));
				if (Math.abs(num) > 500)
					return num;
				acc += num;
			}
		}
		for (row = 0; row + 3 < board.length; row++) {
			for (col = 0; col + 3 < board[row].length; col++) {
				num = _score(counter(board, new Point(row, col), diagR));
				if (Math.abs(num) > 500)
					return num;
				acc += num;
			}
		}
		return acc;
	}

	public static int minimax(int[][] board, boolean maxNode, int minOfMaxes, int maxOfMins, int depth) {
		if (depth == 0 || Win(board) != Connect4.BLANK) {
			return (depth + 1) * score(board);
		}
		int[] moves = moves(board);
		for (int dir : moves) {
			int propogated = minimax(makeMove(board, dir, maxNode ? Connect4.COMP : Connect4.YOU), !maxNode, minOfMaxes,
					maxOfMins, depth - 1);

			if (maxNode) {// Raises the minimum
				if (propogated > minOfMaxes)
					minOfMaxes = propogated;
			} else {// Lowers the maximum
				if (maxOfMins > propogated)
					maxOfMins = propogated;
			}

			if (maxOfMins < minOfMaxes)
				break;
		}
		
		return maxNode ? minOfMaxes : maxOfMins;
	}

	public static void print(int[] line) {
		for (int i : line)
			System.out.print(" " + i + " ");
		System.out.println();
	}

	public static int getSmartacularMove(int[][] board, int depth) {
		int[] moves = moves(board);
		print(moves);

		int[] Scores = new int[moves.length];

		int maxScore = Integer.MIN_VALUE;
		for (int i = 0; i < moves.length; i++) {
			Scores[i] = minimax(makeMove(board, moves[i], Connect4.COMP), false, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			maxScore = Math.max(Scores[i], maxScore);
		}
		
		print(Scores);

		for (int i = 0; i < moves.length; i++) {
			if (Scores[i] == maxScore)
				return moves[i];
		}

		return (int) (Math.random() * board.length) + 1;
	}
}

class Point {
	int x;
	int y;

	Point(int xx, int yy) {
		x = xx;
		y = yy;
	}
}
