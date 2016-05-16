package hearthunter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import hearthunter.bases.JukeBox;
import hearthunter.bases.Keys;
import hearthunter.bases.TileMap;

/**
 * The main playing GameState.
 * 
 * Contains everything you need for game: Player, TileMap, Diamonds, etc.
 * 
 * Updates and draws all game objects.
 */
public class PlayState {
	private Player player;
	private TileMap tileMap;
	private Hud hud;
	private ArrayList<Heart> hearts;
	private ArrayList<Item> items;
	private ArrayList<Sparkle> sparkles;
	private GamePanel gp;
	
	// camera position
	private int xsector;
	private int ysector;
	private int sectorSize;

	// events
	private boolean blockInput;
	private boolean eventStart;
	private boolean eventFinish;
	private int eventTick;

	// transition box
	private ArrayList<Rectangle> boxes;

	public PlayState(GamePanel gp) {
		this.gp = gp;
		// create lists
		hearts = new ArrayList<Heart>();
		sparkles = new ArrayList<Sparkle>();
		items = new ArrayList<Item>();

		// load map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/testtileset.gif");
		tileMap.loadMap("/Maps/testmap.map");

		// create player
		player = new Player(tileMap);

		// fill lists
		populateHearts();
		populateItems();

		// initialize player
		player.setTilePosition(17, 17);
		player.setTotalHearts(hearts.size());

		// set up camera position
		sectorSize = GamePanel.WIDTH;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPositionImmediately(-xsector * sectorSize, -ysector * sectorSize);

		// load hud
		hud = new Hud(player, hearts);

//		// load music
//		JukeBox.load("/Music/bgmusic.mp3", "music1");
//		JukeBox.setVolume("music1", -10);
//		JukeBox.loop("music1", 1000, 1000, JukeBox.getFrames("music1") - 1000);
//		JukeBox.load("/Music/finish.mp3", "finish");
//		JukeBox.setVolume("finish", -10);
//
//		// load sfx
//		JukeBox.load("/SFX/collect.wav", "collect");
//		JukeBox.load("/SFX/mapmove.wav", "mapmove");
//		JukeBox.load("/SFX/tilechange.wav", "tilechange");
//		JukeBox.load("/SFX/splash.wav", "splash");

		// start event
		boxes = new ArrayList<Rectangle>();
		eventStart = true;
		eventStart();
	}

	private void populateHearts() {
		Heart d;

		d = new Heart(tileMap);
		d.setTilePosition(20, 20);
		d.addChange(new int[] { 23, 19, 1 });
		d.addChange(new int[] { 23, 20, 1 });
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(12, 36);
		d.addChange(new int[] { 31, 17, 1 });
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(28, 4);
		d.addChange(new int[] { 27, 7, 1 });
		d.addChange(new int[] { 28, 7, 1 });
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(4, 34);
		d.addChange(new int[] { 31, 21, 1 });
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(28, 19);
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(35, 26);
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(9, 14);
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(4, 3);
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(20, 14);
		hearts.add(d);
		d = new Heart(tileMap);
		d.setTilePosition(13, 20);
		hearts.add(d);
	}

	private void populateItems() {
		Item item;

		item = new Item(tileMap);
		item.setType(Item.KEY);
		item.setTilePosition(26, 37);
		items.add(item);

		item = new Item(tileMap);
		item.setType(Item.BOAT);
		item.setTilePosition(12, 4);
		items.add(item);
	}

	public void update() {
		// check keys
		handleInput();

		// check events
		if (eventStart)
			eventStart();
		if (eventFinish)
			eventFinish();

		if (player.numHearts() == player.getTotalHearts())
			eventFinish = blockInput = true;

		// update camera
		int oldxs = xsector;
		int oldys = ysector;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPosition(-xsector * sectorSize, -ysector * sectorSize);
		tileMap.update();

		if (oldxs != xsector || oldys != ysector)
			JukeBox.play("mapmove");

		if (tileMap.isMoving())
			return;

		// update player
		player.update();

		// update diamonds
		for (int i = 0; i < hearts.size(); i++) {

			Heart d = hearts.get(i);
			d.update();

			// player collects diamond
			if (player.intersects(d)) {
				// remove from list
				hearts.remove(i);
				i--;

				// increment amount of collected diamonds
				player.collectedHeart();
				hud.show();

				// play collect sound
				JukeBox.play("collect");

				// add new sparkle
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(d.getx(), d.gety());
				sparkles.add(s);

				// make any changes to tile map
				ArrayList<int[]> ali = d.getChanges();
				for (int[] j : ali)
					tileMap.setTile(j[0], j[1], j[2]);

				if (ali.size() != 0)
					JukeBox.play("tilechange");
				
				gp.anotherGame();
			}
		}

		// update sparkles
		for (int i = 0; i < sparkles.size(); i++) {
			Sparkle s = sparkles.get(i);
			s.update();
			if (s.shouldRemove()) {
				sparkles.remove(i);
				i--;
			}
		}

		// update items
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if (player.intersects(item)) {
				items.remove(i);
				i--;
				item.collected(player);
				hud.show();
				JukeBox.play("collect");
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(item.getx(), item.gety());
				sparkles.add(s);
			}
		}

	}

	public void draw(Graphics g) {
		// draw tilemap
		tileMap.draw(g);

		// draw player
		player.draw(g);

		// draw diamonds
		for (Heart d : hearts)
			d.draw(g);

		// draw sparkles
		for (Sparkle s : sparkles)
			s.draw(g);

		// draw items
		for (Item i : items)
			i.draw(g);

		// draw hud
		hud.draw(g);

		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for (int i = 0; i < boxes.size(); i++) {
			Rectangle r = boxes.get(i);
			g.fillRect(r.x * GamePanel.SCALE, r.y * GamePanel.SCALE, r.width * GamePanel.SCALE,
					r.height * GamePanel.SCALE);
		}

	}

	public void handleInput() {
		if (blockInput)
			return;
		if (Keys.isDown(Keys.LEFT))
			player.setLeft();
		if (Keys.isDown(Keys.RIGHT))
			player.setRight();
		if (Keys.isDown(Keys.UP))
			player.setUp();
		if (Keys.isDown(Keys.DOWN))
			player.setDown();
		if (Keys.isPressed(Keys.SPACE))
			player.setAction();
		if (Keys.isPressed(Keys.ENTER))
			gp.anotherGame();
	}

	private void eventStart() {
		eventTick++;
		if (eventTick == 1) {
			boxes.clear();
			for (int i = 0; i < 9; i++) {
				boxes.add(new Rectangle(0, i * 16, GamePanel.WIDTH, 16));
			}
		}
		if (eventTick > 1 && eventTick < 32) {
			for (int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if (i % 2 == 0) {
					r.x -= 4;
				} else {
					r.x += 4;
				}
			}
		}
		if (eventTick == 33) {
			boxes.clear();
			eventStart = false;
			eventTick = 0;
		}
	}

	private void eventFinish() {
		eventTick++;
		if (eventTick == 1) {
			boxes.clear();
			for (int i = 0; i < 9; i++) {
				if (i % 2 == 0)
					boxes.add(new Rectangle(-128, i * 16, GamePanel.WIDTH, 16));
				else
					boxes.add(new Rectangle(128, i * 16, GamePanel.WIDTH, 16));
			}
			JukeBox.stop("music1");
			JukeBox.play("finish");
		}
		if (eventTick > 1) {
			for (int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if (i % 2 == 0) {
					if (r.x < 0)
						r.x += 4;
				} else {
					if (r.x > 0)
						r.x -= 4;
				}
			}
		}
		if (eventTick > 33) {
			if (!JukeBox.isPlaying("finish")) {
				gp.gameOver();
			}
		}
	}
}
