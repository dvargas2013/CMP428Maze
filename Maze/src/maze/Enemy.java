package maze;

import java.awt.Graphics;

import bases.MiniGame;

public class Enemy {
	MiniGame game;
	
	public Enemy(MiniGame game) {
		this.game = game;
	}
	
	public void touch() {
		game.addSelf();
	}
	
	public void draw(Graphics g, int x, int y, int w, int h) {
		g.drawRect(x, y, w, h);
	}
}
