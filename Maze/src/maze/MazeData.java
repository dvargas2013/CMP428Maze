package maze;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import bases.MiniGame;

public class MazeData {
	private enum Tile {
		WALL, ENEMY, BLANK, TOUCHED
	};

	private final Tile[][] data;
	private ArrayList<Enemy<? extends MiniGame>> enemylist = new ArrayList<Enemy<? extends MiniGame>>();
	public final int J, I;

	public int centerI, centerJ;
	int exitI;
	int exitJ;
	public int blocksize;

	public MazeData() {
		this(5, 5, .75, .75, 50);
	}

	public MazeData(int width, int height) {
		this(width, height, .75, .75, 50);
	}

	public MazeData(int width, int height, double complex, double density, int blok) {
		blocksize = blok;
		I = ((width / 2) * 2) + 1; // make sides odd
		J = ((height / 2) * 2) + 1;
		data = new Tile[J][I]; // Initialize maze array to no walls
		encloseBoundaries(); // Add walls around the edges of maze
		fillMaze(complex, density);
	}

	public boolean centerHasMonster() {
		return hasMonster(centerI, centerJ);
	}

	public boolean hasMonster(int i, int j) {
		// Outside bounds is NO Monsters
		if (j >= J || j < 0 || i < 0 || i >= I)
			return false;
		return data[j][i] == Tile.ENEMY;
	}

	public void addMonster(Enemy<? extends MiniGame> e) {
		int[] ij = newMonsterIJ();

		data[ij[1]][ij[0]] = Tile.ENEMY;
		enemylist.add(e);
		e.setLocation(ij[0], ij[1]);
	}
	
	public void addMonster(Enemy<? extends MiniGame> e, int i, int j) {
		data[j][i] = Tile.ENEMY;
		enemylist.add(e);
		e.setLocation(i, j);
	}

	public boolean centerIsOpaque() {
		return isOpaque(centerI, centerJ);
	}

	public boolean isOpaque(int i, int j) {
		// outside bounds can be classified as walls
		if (j >= J || j < 0 || i < 0 || i >= I)
			return true;
		return data[j][i] == Tile.WALL;
	}

	public boolean win() {
		return centerJ == exitJ && centerI == exitI;
	}

	public void draw(Graphics g, int x, int y) {
		int blocksFromEdgeLR = x / blocksize;
		int blocksFromEdgeUD = y / blocksize;

		int is = Math.max(0, centerI - blocksFromEdgeLR - 2);
		int ie = Math.min(centerI + blocksFromEdgeLR + 2, I);
		int js = Math.max(0, centerJ - blocksFromEdgeUD - 2);
		int je = Math.min(centerJ + blocksFromEdgeUD + 2, J);

		for (int i = is; i < ie; i++) {
			int ix = x + (i - centerI) * blocksize;
			for (int j = js; j < je; j++) {
				int jy = y + (j - centerJ) * blocksize;
				
				switch (data[j][i]) {
				case WALL:
					g.setColor(Color.BLACK);
					g.fillRect(ix, jy, blocksize, blocksize);
					break;
				case TOUCHED:
					g.setColor(Color.GREEN);
					g.fillRect(ix + 1, jy + 1, blocksize - 1, blocksize - 1);
					break;
				case ENEMY:
					g.setColor(Color.RED);
					g.fillRect(ix + 1, jy + 1, blocksize - 1, blocksize - 1);
					break;
				case BLANK: break;
				}
				
			}
		}
	}

	public void touch() {
		if (centerHasMonster()) {
			for (Enemy<? extends MiniGame> e: enemylist) { 
				// Find enemy that is at that location
				if (e.i == centerI && e.j == centerJ) {
					enemylist.remove(e);
					e.touch();
					data[centerJ][centerI] = Tile.TOUCHED;
					break;
				}
			}
		}
		
		if (centerHasMonster()) {
			// If you found no enemy at that location grab a random one
			enemylist.remove(0).touch();
		}
		
		data[centerJ][centerI] = Tile.TOUCHED;
	}

	private void encloseBoundaries() {
		// properly initialize block array
		for (int i = 0; i < J; i++) {
			for (int j = 0; j < I; j++)
				data[j][i] = Tile.BLANK;
		}

		// Enclose Borders
		for (int i = 0; i < J; i++) {
			data[0][i] = data[I - 1][i] = Tile.WALL;
		}
		for (int i = 0; i < I; i++) {
			data[i][0] = data[i][J - 1] = Tile.WALL;
		}
	}

	private void fillMaze(double complex, double density) {
		// Adjust relative to maze size
		complex *= 5 * (I + J);
		density *= (I / 2) * (J / 2);

		int[] xy = { -1, -1 };
		int[] _0123 = { 0, 1, 2, 3 }; // Static stuff to pass to pickNeighbors

		int x, y, x_, y_;
		for (int i = 0; i < density; i++) { // Make Aisles
			x = randEven(J);
			y = randEven(I);
			data[y][x] = Tile.WALL; // Random Wall

			for (int j = 0; j < complex; j++) {
				pickNeighbor(x, y, xy, _0123);
				x_ = xy[0];
				y_ = xy[1];
				// If no wall in chosen neighbor
				if (xy[0] != -1 && data[y_][x_] != Tile.WALL) {
					data[y_][x_] = data[y_ + (y - y_) / 2][x_ + (x - x_) / 2] = Tile.WALL;
					x = x_;
					y = y_;
				}
			}
		}

		addEntranceExit();
	}

	public int[] randomMiddleIJ() {
		int i = (int) (Math.random() * I / 2) + I / 2;
		int j = (int) (Math.random() * J / 2) + J / 2;

		while (isOpaque(i, j)) {
			if (!isOpaque(i + 1, j))
				return new int[] { i + 1, j };
			if (!isOpaque(i, j + 1))
				return new int[] { i, j + 1 };
			if (!isOpaque(i - 1, j))
				return new int[] { i - 1, j };
			if (!isOpaque(i, j - 1))
				return new int[] { i, j - 1 };

			i = (int) (Math.random() * I / 2) + I / 2;
			j = (int) (Math.random() * J / 2) + I / 2;
		}

		return new int[] { i, j };
	}

	public int[] newMonsterIJ() {
		double o = 2 * Math.random() * Math.PI;
		int d = 1;
		double ii = Math.cos(o);
		double jj = Math.sin(o);

		int i = exitI + (int) (d * ii);
		int j = exitJ + (int) (d * jj);

		while (isOpaque(i, j) || hasMonster(i, j) || (centerI == i && centerJ == j) || (exitI == i && exitJ == j)) {
			d += Math.min(I, J) / 2 * Math.random() + 1;
			i = exitI + (int) (d * ii);
			j = exitJ + (int) (d * jj);

			if (j >= J || j < 0 || i < 0 || i >= I) {
				d = 1;
				o = 2 * Math.random() * Math.PI;
				ii = Math.cos(o);
				jj = Math.sin(o);
				i = exitI + (int) (d * ii);
				j = exitJ + (int) (d * jj);
			}
		}

		return new int[] { i, j };
	}

	private void addEntranceExit() {
		int[] ij = randomMiddleIJ();
		centerI = ij[0];
		centerJ = ij[1];

		switch ((int) (Math.random() * 4)) {
		case 0:
			exitI = randEven(I - 2) + 1;
			exitJ = 0;
			break;
		case 1:
			exitI = randEven(I - 2) + 1;
			exitJ = J - 1;
			break;
		case 2:
			exitI = 0;
			exitJ = randEven(J - 2) + 1;
			break;
		case 3:
			exitI = I - 1;
			exitJ = randEven(J - 2) + 1;
			break;
		}

		data[centerJ][centerI] = Tile.BLANK;
		data[exitJ][exitI] = Tile.BLANK;
	}

	private void pickNeighbor(int x, int y, int[] chooseNeighbors, int[] _0123) {
		shuffle(_0123);
		// Pick an order to check conditions in
		for (int i : _0123) {
			switch (i) {
			case 0:
				if (x > 1) {
					chooseNeighbors[0] = x - 2;
					chooseNeighbors[1] = y;
					return;
				}
				break;
			case 1:
				if (x < J - 2) {
					chooseNeighbors[0] = x + 2;
					chooseNeighbors[1] = y;
					return;
				}
				break;
			case 2:
				if (y > 1) {
					chooseNeighbors[0] = x;
					chooseNeighbors[1] = y - 2;
					return;
				}
				break;
			case 3:
				if (y < I - 2) {
					chooseNeighbors[0] = x;
					chooseNeighbors[1] = y + 2;
					return;
				}
				break;
			}
		}
		// If no conditions are met. There are no neighbors
		chooseNeighbors[0] = -1;
		chooseNeighbors[1] = -1;
	}

	private void shuffle(int[] list) {
		int r, item;
		for (int i = list.length - 1; i > 0; i--) {
			r = (int) (Math.random() * i);
			item = list[i];
			list[i] = list[r];
			list[r] = item;
		}
	}

	private int randEven(int max) {
		return (int) (Math.random() * (max / 2)) * 2;
	}
}