package com.mace.fair.pills;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Rectangle;

import com.mace.fair.constants.Constants;
import com.mace.fair.map.Map;

/* 
 * TODO Fix endlessly eatable pill
 */
public class Pill {

	private final int tileSize; // length of each tile
	private final int index; // array index of pills
	private Map map; // Map object to check tile properties
	private int x, y; // X & Y position the pill will be rendered at
	private int oldX, oldY;
	private Image img; // Image of the pill
	private Random rnd; // For random x & y postions
	private Rectangle collider; // A rectangle set at the position of the pill
								// for collisions
	private boolean isEaten; // Determine if the pill is eaten

	public Pill(Map m, int index) {
		// setup the final variables & initialise the map object
		this.map = m;
		this.index = index;
		tileSize = Constants.TILESIZE;
	}

	public void init(GameContainer gc) throws SlickException {
		// Initialise random with a seed of the PCs current time in Milliseconds
		rnd = new Random(System.currentTimeMillis() * (index + 1));
		// Set the image to a scaled down version of the pills
		img = new Image(Constants.pill_img_loc).getScaledCopy(0.5f);
		// initialise the isEaten variable to false
		isEaten = false;
		// While the pills x & y fall on an x & y position matching a hole or
		// wall
		// loop through an randomise the position again
		while (map.getTileProperty(x, y).equals("falling") || map.getTileProperty(x, y).equals("blocked")) {
			x = rnd.nextInt(map.getWidth());
			y = rnd.nextInt(map.getHeight());
		}
		oldX = x;
		oldY = y;

		// initialise the pills collider to the location of the image & size
		collider = new Rectangle(x * tileSize, y * tileSize, img.getWidth(), img.getHeight());

	}

	public void render(Graphics g) throws SlickException {
		/*
		 * If the pill isn't eaten, render the pill at the x * tileSize & y *
		 * tileSize (This converts it to a world location rather than screen)
		 */
		if (!isEaten)
			g.drawImage(img, x * tileSize, y * tileSize);
	}

	/*
	 * TODO add check horizontal and vertical for pills
	 */

	public void resetPill() {
		isEaten = false;
		rnd.setSeed(System.currentTimeMillis() / (index + 1));
		while (map.getTileProperty(x, y).equals("falling") || map.getTileProperty(x, y).equals("blocked") || oldX == x || oldY == y) {
			x = rnd.nextInt(map.getWidth());
			y = rnd.nextInt(map.getHeight());
		}
		oldX = x;
		oldY = y;
		collider.setLocation(x * tileSize, y * tileSize);
	}

	public Vector2f location() {
		// Return a vector with an x & y position of the pill (returns a tile
		// position not world)
		return new Vector2f(x, y);
	}

	public Rectangle getCollider() {
		return collider;
	}

	public void eatPill() {
		isEaten = true;
	}

	public boolean isPillEaten() {
		return isEaten;
	}
}
