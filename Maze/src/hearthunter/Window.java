package hearthunter;

import bases.MainFrame;
import bases.MiniGame;

public class Window extends MainFrame {
	public static void main(String[] args) {
		new Window(500,500); // multiple of 125. also pls square
	}
	
	private static final long serialVersionUID = 1L;
	
	MiniGame maingame = new GamePanel(this);
	
	public Window(int w, int h) {
		super(w, h);
		gainControl();
		start();
	}
	
	@Override
	public void gainControl() {
		addCanvas(maingame);
	}
}
