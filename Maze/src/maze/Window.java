package maze;

import bases.MainFrame;

public class Window extends MainFrame {
	public static void main(String[] args) {
		new Window(500,500);
	}
	
	private static final long serialVersionUID = 1L;

	Maze maze = new Maze(this);
	
	public Window(int w, int h) {
		super(w, h);
		gainControl();
		start();
	}

	@Override
	public void gainControl() {
		addCanvas(maze);
	}

}
