package com.mace.fair.entities;

import java.util.ArrayList;

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
				wallSlide(moveBy);
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

	private void wallSlide(Vector2f moveBy) {
		/*
		 * Zombie AI plan -> 1) Check where's free around the zombie 2)
		 * Whichever closest moves them to spot wins
		 */
		checkWalls();
		findBestRoute(moveBy);
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
		for (int i = 0; i < directions.length; ++i)
			System.out.println("My value is " + directions[i]);
	}

	private void findBestRoute(Vector2f moveBy) {

		byte counter = 0;
		while (counter < directions.length && !directions[counter]) {
			++counter;
		}
		switch (counter) {
		case Constants.TOP_LEFT:
			moveBy.x = -1;
			moveBy.y = -1;
			break;
		case Constants.TOP_CENTRE:
			moveBy.x = 0;
			moveBy.y = -1;
			break;
		case Constants.TOP_RIGHT:
			moveBy.x = 1;
			moveBy.y = -1;
			break;
		case Constants.MIDDLE_LEFT:
			moveBy.x = -1;
			moveBy.y = 0;
			break;
		case Constants.MIDDLE_RIGHT:
			moveBy.x = 1;
			moveBy.y = 0;
			break;
		case Constants.BOTTOM_LEFT:
			moveBy.x = -1;
			moveBy.y = 1;
			break;
		case Constants.BOTTOM_CENTRE:
			moveBy.x = 0;
			moveBy.y = 1;
			break;
		case Constants.BOTTOM_RIGHT:
			moveBy.x = 1;
			moveBy.y = 1;
			break;
		}
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
