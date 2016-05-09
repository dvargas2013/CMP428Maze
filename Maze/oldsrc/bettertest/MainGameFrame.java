package bettertest;

/**
 * Extends MainFrame and Implements gainControl to give control back to MainMiniGame
 * 
 * @author danv
 */
public class MainGameFrame extends MainFrame {
	public static void main(String[] args) {
		new MainGameFrame(500, 500);
	}

	private static final long serialVersionUID = 1L;
	MainMiniGame main;

	MainGameFrame(int width, int height) {
		super(width, height);
		main = new MainMiniGame(this);
		gainControl();
		start();
	}

	@Override
	public void gainControl() {
		addCanvas(main);
	}
}
