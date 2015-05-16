package com.mace.fair.entities;

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
	private Image zombie1, zombie2;
	private Image run1, run2;
	private Rectangle collider;
	private boolean resetNextMove = false;
	private boolean flipped;
	private boolean isRunning; // Is this zombie running away from spot
	private boolean isAlive;
	private boolean cheatExterminate;

	public Zombie(Map m, int index) {
		this.map = m;
		this.index = index;
		tileSize = Constants.TILESIZE;
		x = (int) (m.getZombieStart(index).x / tileSize);
		y = (int) (m.getZombieStart(index).y / tileSize);
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		zombie1 = new Image(Constants.zombie1_image_loc);
		zombie2 = new Image(Constants.zombie2_image_loc);
		run1 = new Image(Constants.zombieAway1_img_loc);
		run2 = new Image(Constants.zombieAway2_img_loc);
		collider = new Rectangle(x * tileSize, y * tileSize, zombie1.getWidth(), zombie1.getHeight());
		isAlive = true;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!cheatExterminate) {
			if (!flipped) {
				if (isRunning)
					g.drawImage(zombie1, x * tileSize, y * tileSize);
				else
					g.drawImage(run1, x * tileSize, y * tileSize);
			} else {
				if (isRunning)
					g.drawImage(zombie2, x * tileSize, y * tileSize);
				else
					g.drawImage(run2, x * tileSize, y * tileSize);
			}
		}
		g.draw(collider);
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
		collider.setLocation(x * tileSize, y * tileSize);
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

		// collider.setLocation((x + dx) * 64, (y + dy) * 64);
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
}
