package maze;

import java.awt.Graphics;

public class Block {
	public boolean opaque = false;
	public final int x, y, w, h;

	public Block(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void draw(Graphics g) {
		if (opaque) {
			g.fillRect(x, y, w, h);
		} else {
			// g.drawRect(x, y, w, h);
		}
	}
	public void drawOpaque(Graphics g){
		g.fillRect(x, y, w, h);
	}
}
