package maze;

import java.awt.Color;
import java.awt.Graphics;

public class Maze {
	private final Block[][] data;
	private final int w, h;
	private Block enter, exit;

	public Maze() {
		this(5, 5, .75, .75);
	}

	public Maze(int width, int height) {
		this(width, height, .75, .75);
	}

	public Maze(int width, int height, double complex, double density) {
		w = ((width / 2) * 2) + 1; // make sides odd
		h = ((height / 2) * 2) + 1;
		data = new Block[h][w]; // Initialize maze array to no walls
		encloseBoundaries(); // Add walls around the edges of maze
		fillMaze(complex, density);
	}

	private void addEntranceExit(int[] _0123) {
		shuffle(_0123);
		_0123 = new int[] { _0123[0], _0123[1] };
		Block[] haha = new Block[2];
		int j = 0;
		for (int i : _0123) {
			switch (i) {
			case 0: haha[j++] = data[randEven(h - 2) + 1][0    ]; break;
			case 1: haha[j++] = data[randEven(h - 2) + 1][w - 1]; break;
			case 2: haha[j++] = data[0    ][randEven(w - 2) + 1]; break;
			case 3: haha[j++] = data[h - 1][randEven(w - 2) + 1]; break;
			}
		}
		enter = haha[0];
		exit = haha[1];
		enter.opaque = false;
		exit.opaque = false;
	}

	private void encloseBoundaries() {
		// properly initialize block array
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				data[j][i] = new Block(i * 10, j * 10, 10, 10);
			}
		}

		// Enclose Borders
		for (int i = 0; i < w; i++) {
			data[0][i].opaque = data[h - 1][i].opaque = true;
		}
		for (int i = 0; i < h; i++) {
			data[i][0].opaque = data[i][w - 1].opaque = true;
		}
	}

	private void fillMaze(double complex, double density) {
		// Adjust relative to maze size
		complex *= 5 * (h + w);
		density *= (h / 2) * (w / 2);

		int[] xy = { -1, -1 };
		int[] _0123 = { 0, 1, 2, 3 }; // Static stuff to pass to pickNeighbors

		int x, y, x_, y_;
		for (int i = 0; i < density; i++) { // Make Aisles
			x = randEven(w);
			y = randEven(h);
			data[y][x].opaque = true; // Random Wall

			for (int j = 0; j < complex; j++) {
				pickNeighbor(x, y, xy, _0123);
				x_ = xy[0];
				y_ = xy[1];
				// If no wall in chosen neighbor
				if (xy[0] != -1 && !data[y_][x_].opaque) { 
					data[y_][x_].opaque = data[y_ + (y - y_) / 2][x_ + (x - x_) / 2].opaque = true;
					x = x_;
					y = y_;
				}
			}
		}

		addEntranceExit(_0123);
	}

	private void pickNeighbor(int x, int y, int[] chooseNeighbors, int[] _0123) {
		shuffle(_0123);
		//Pick an order to check conditions in
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
				if (x < w - 2) {
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
				if (y < h - 2) {
					chooseNeighbors[0] = x;
					chooseNeighbors[1] = y + 2;
					return;
				}
				break;
			}
		}
		//If no conditions are met. There are no neighbors
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

	public void draw(Graphics g) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				data[j][i].draw(g);
			}
		}
		g.setColor(Color.GREEN);
		enter.drawOpaque(g);
		g.setColor(Color.RED);
		exit.drawOpaque(g);
	}
}
