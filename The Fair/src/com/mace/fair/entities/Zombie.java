package com.mace.fair.entities;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.mace.fair.constants.Constants;
import com.mace.fair.gui.LivesGUI;
import com.mace.fair.map.Map;
import com.mace.fair.entities.Player;

public class Zombie {
	private Map map;
	private final int tileSize, index;
	private int x, y;
	private ArrayList<Zombie> zombies;
	private Image zombie1, zombie2;
	private Image run1, run2;
	private Rectangle collider;
	private Random rnd;
	private boolean directions[];
	private boolean resetNextMove = false;
	private boolean flipped;
	private boolean isRunning; // Is this zombie running away from spot
	private boolean isAlive;
	private boolean cheatExterminate;

	public Zombie(Map m, int index, ArrayList<Zombie> zombies) {
		this.map = m;
		this.index = index;
		this.zombies = zombies;
		tileSize = Constants.TILESIZE;
		x = (int) (m.getZombieStart(index).x / tileSize);
		y = (int) (m.getZombieStart(index).y / tileSize);
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		flipped = false;
		isRunning = false;
		directions = new boolean[8];
		zombie1 = new Image(Constants.zombie1_image_loc);
		zombie2 = new Image(Constants.zombie2_image_loc);
		run1 = new Image(Constants.zombieAway1_img_loc);
		run2 = new Image(Constants.zombieAway2_img_loc);
		collider = new Rectangle(x * tileSize + zombie1.getWidth() / 4, y * tileSize + zombie1.getHeight() / 4, zombie1.getWidth() / 2, zombie1.getHeight() / 2);
		isAlive = true;
		rnd = new Random(System.currentTimeMillis());
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!cheatExterminate) {
			if (!flipped) {
				if (!isRunning)
					g.drawImage(zombie1, x * tileSize, y * tileSize);
				else
					g.drawImage(run1, x * tileSize, y * tileSize);
			} else {
				if (!isRunning)
					g.drawImage(zombie2, x * tileSize, y * tileSize);
				else
					g.drawImage(run2, x * tileSize, y * tileSize);
			}
		}
		// g.draw(collider);
	}

	public void update(GameContainer gc, StateBasedGame sbg, long delta, Player p, boolean cheatFrozen, boolean cheatExterminate) throws SlickException {
		Vector2f moveBy = determineDirection(p);
		this.cheatExterminate = cheatExterminate;

		if (!cheatFrozen && !cheatExterminate) {

			if (map.getTileProperty(x + moveBy.x, y + moveBy.y).equals("walkable")) {
				x += moveBy.x;
				y += moveBy.y;
				flipped = !flipped;
				updateRect();
			} else if (map.getTileProperty(x + moveBy.x, y + moveBy.y).equals("fallable")) {
				isAlive = false;
			} else {
				wallSlide(moveBy, new Vector2f(Math.abs(x - p.getTilePosition().x), Math.abs(y - p.getTilePosition().y)));
				x += moveBy.x;
				y += moveBy.y;
				flipped = !flipped;
				updateRect();
			}

			for (Zombie z : zombies) {
				if (z != this) {
					if (collider.intersects(z.getCollider())) {
						this.resetZombie();
					}
				}
			}

			if (collider.intersects(p.getCollider()) && !isRunning) {
				if (resetNextMove) {
					x = (int) (map.getZombieStart(index).x / tileSize);
					y = (int) (map.getZombieStart(index).y / tileSize);
					LivesGUI.decrement();
				} else {
					resetNextMove = !resetNextMove;
				}
				updateRect();
			} else if (collider.intersects(p.getCollider()) && isRunning) {
				isAlive = false;
			}
		}

	}

	private void updateRect() {
		collider.setLocation(x * tileSize + zombie1.getWidth() / 4, y * tileSize + zombie1.getHeight() / 4);
	}

	private void wallSlide(Vector2f moveBy, Vector2f distanceFromPlayer) {
		/*
		 * Zombie AI plan -> 1) Check where's free around the zombie 2)
		 * Whichever closest moves them to spot wins
		 */
		checkWalls();
		Vector2f temp = findBestRoute(moveBy, distanceFromPlayer);
		moveBy.x = temp.x;
		moveBy.y = temp.y;
	}

	private void checkWalls() {

		if (map.getTileProperty(x - 1, y - 1).equals("blocked")) {
			directions[Constants.TOP_LEFT] = false;
		} else {
			directions[Constants.TOP_LEFT] = true;
		}

		if (map.getTileProperty(x, y - 1).equals("blocked")) {
			directions[Constants.TOP_CENTRE] = false;
		} else {
			directions[Constants.TOP_CENTRE] = true;
		}

		if (map.getTileProperty(x + 1, y - 1).equals("blocked")) {
			directions[Constants.TOP_RIGHT] = false;
		} else {
			directions[Constants.TOP_RIGHT] = true;
		}

		if (map.getTileProperty(x - 1, y).equals("blocked")) {
			directions[Constants.MIDDLE_LEFT] = false;
		} else {
			directions[Constants.MIDDLE_LEFT] = true;
		}

		if (map.getTileProperty(x + 1, y).equals("blocked")) {
			directions[Constants.MIDDLE_RIGHT] = false;
		} else {
			directions[Constants.MIDDLE_RIGHT] = true;
		}

		if (map.getTileProperty(x - 1, y + 1).equals("blocked")) {
			directions[Constants.BOTTOM_LEFT] = false;
		} else {
			directions[Constants.BOTTOM_LEFT] = true;
		}

		if (map.getTileProperty(x, y + 1).equals("blocked")) {
			directions[Constants.BOTTOM_CENTRE] = false;
		} else {
			directions[Constants.BOTTOM_CENTRE] = true;
		}

		if (map.getTileProperty(x + 1, y + 1).equals("blocked")) {
			directions[Constants.BOTTOM_RIGHT] = false;
		} else {
			directions[Constants.BOTTOM_RIGHT] = true;
		}
	}

	private Vector2f findBestRoute(Vector2f moveBy, Vector2f distanceFromPlayer) {
		ArrayList<Vector2f> vectors = new ArrayList<Vector2f>();
		ArrayList<Byte> listOfValues = new ArrayList<Byte>();
		Vector2f selectedRoute = new Vector2f(0, 0);

		byte counter = 0;

		while (counter < directions.length) {
			if (directions[counter])
				listOfValues.add(counter);
			++counter;
		}
		for (int i = 0; i < listOfValues.size(); ++i) {
			switch (listOfValues.get(i)) {
			case Constants.TOP_LEFT:
				selectedRoute.x = -1;
				selectedRoute.y = -1;
				break;
			case Constants.TOP_CENTRE:
				selectedRoute.x = 0;
				selectedRoute.y = -1;
				break;
			case Constants.TOP_RIGHT:
				selectedRoute.x = 1;
				selectedRoute.y = -1;
				break;
			case Constants.MIDDLE_LEFT:
				selectedRoute.x = -1;
				selectedRoute.y = 0;
				break;
			case Constants.MIDDLE_RIGHT:
				selectedRoute.x = 1;
				selectedRoute.y = 0;
				break;
			case Constants.BOTTOM_LEFT:
				selectedRoute.x = -1;
				selectedRoute.y = 1;
				break;
			case Constants.BOTTOM_CENTRE:
				selectedRoute.x = 0;
				selectedRoute.y = 1;
				break;
			case Constants.BOTTOM_RIGHT:
				selectedRoute.x = 1;
				selectedRoute.y = 1;
				break;
			}
			if (distanceFromPlayer.x < distanceFromPlayer.y) {
				if (selectedRoute.x != moveBy.x)
					directions[listOfValues.get(i)] = false;
				else
					vectors.add(selectedRoute);
			} else {
				if (selectedRoute.y != moveBy.y)
					directions[listOfValues.get(i)] = false;
				else
					vectors.add(selectedRoute);
			}
		}
		if (vectors.size() > 0)
			return (vectors.get(rnd.nextInt(vectors.size())));
		else
			return new Vector2f(0, 0);
	}

	private Vector2f determineDirection(Player p) {
		/*
		 * Determines whether the zombie needs to move positively, negatively or
		 * not at all in each direction
		 */
		int dx = 0, dy = 0;
		Vector2f temp = new Vector2f();

		dx = (int) p.getTilePosition().x - x;
		dy = (int) p.getTilePosition().y - y;
		if (dx != 0) {
			dx = dx / Math.abs(dx);
		} else {
			dx = 0;
		}
		if (dy != 0) {
			dy = dy / Math.abs(dy);
		} else {
			dy = 0;
		}

		if (!isRunning) {
			temp.x = dx;
			temp.y = dy;
		} else {
			// If the player has eaten a pill and has magic protection, invert
			// the direction.
			temp.x = dx * -1;
			temp.y = dy * -1;
		}

		return temp;
	}

	public void isFleeing(boolean playerState) {
		isRunning = playerState;
	}

	public boolean isZombieAlive() {
		return isAlive;
	}

	public void resetZombie() {

		isAlive = true;
		flipped = false;
		isRunning = false;
		cheatExterminate = false;
		x = (int) (map.getZombieStart(index).x / tileSize);
		y = (int) (map.getZombieStart(index).y / tileSize);
	}

	public void loadZombieData(int x, int y, boolean running, boolean flipped, boolean alive) {
		this.x = x;
		this.y = y;
		this.flipped = flipped;
		this.isRunning = running;
		this.isAlive = alive;
	}

	public Vector2f getTilePosition() {
		return new Vector2f(x, y);
	}

	public boolean isSpriteFlipped() {
		return flipped;
	}

	public Rectangle getCollider() {
		return collider;
	}
}
