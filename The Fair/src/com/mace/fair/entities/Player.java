package com.mace.fair.entities;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.mace.fair.constants.Constants;
import com.mace.fair.gui.LivesGUI;
import com.mace.fair.map.Map;
import com.mace.fair.pills.Pill;
import com.mace.fair.states.GameState1;

public class Player {

	private final int tileSize, MAX_MOVE; // Maximum moves with magic protection
	private int x, y; // Players X & Y co-ordinates
	private int moveCount;
	private Image player1, player2; // Player texture
	private Image playerAura1, playerAura2;
	private Map map; // Map object taken from GameState1 for collisions
	private Rectangle collider;
	private boolean flipped;
	private boolean isAuraActive;

	// TODO add flipping aura image

	public Player(Map map) {
		tileSize = Constants.TILESIZE;
		MAX_MOVE = Constants.MAX_MOVE;
		this.map = map;
		x = (int) (map.getPlayerStart().x / tileSize);
		y = (int) (map.getPlayerStart().y / tileSize);
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		isAuraActive = false;
		player1 = new Image(Constants.player1_image_loc);
		player2 = new Image(Constants.player2_image_loc);
		playerAura1 = new Image(Constants.playerAura1_img_loc);
		playerAura2 = new Image(Constants.playerAura2_img_loc);
		flipped = false;
		collider = new Rectangle(x * tileSize, y * tileSize, player1.getWidth(), player2.getHeight());
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// Render the correct image
		if (!flipped) {
			if (!isAuraActive)
				g.drawImage(player1, x * tileSize, y * tileSize);
			else
				g.drawImage(playerAura1, x * tileSize, y * tileSize);
		} else {
			if (!isAuraActive)
				g.drawImage(player2, x * tileSize, y * tileSize);
			else
				g.drawImage(playerAura2, x * tileSize, y * tileSize);
		}
//		g.draw(collider);
	}

	public void update(GameContainer gc, StateBasedGame sbg, long delta, ArrayList<Pill> pills) throws SlickException {
		Input input = gc.getInput(); // Input handling object
		
		if (input.isKeyPressed(Input.KEY_LEFT) || input.isKeyPressed(Input.KEY_A)) {
			input.clearKeyPressedRecord();
			if (map.getTileProperty(x - 1, y).equals("walkable") || map.getTileProperty(x - 1, y).equals("fallable")) {
				if (map.getTileProperty(x - 1, y).equals("fallable"))
					LivesGUI.decrement();
				x -= 1;
				updateRect();
				GameState1.moveEntities();
				flipped = !flipped;
				CountStep();
			}
		} else if (input.isKeyPressed(Input.KEY_RIGHT) || input.isKeyPressed(Input.KEY_D)) {
			input.clearKeyPressedRecord();
			if (map.getTileProperty(x + 1, y).equals("walkable") || map.getTileProperty(x + 1, y).equals("fallable")) {
				if (map.getTileProperty(x + 1, y).equals("fallable"))
					LivesGUI.decrement();
				x += 1;
				updateRect();
				GameState1.moveEntities();
				flipped = !flipped;
				CountStep();
			}
		} else if (input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_W)) {
			input.clearKeyPressedRecord();
			if (map.getTileProperty(x, y - 1).equals("walkable") || map.getTileProperty(x, y - 1).equals("fallable")) {
				if (map.getTileProperty(x, y - 1).equals("fallable"))
					LivesGUI.decrement();
				y -= 1;
				updateRect();
				GameState1.moveEntities();
				flipped = !flipped;
				CountStep();
			}
		} else if (input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)) {
			input.clearKeyPressedRecord();
			if (map.getTileProperty(x, y + 1).equals("walkable") || map.getTileProperty(x, y + 1).equals("fallable")) {
				if (map.getTileProperty(x, y + 1).equals("fallable"))
					LivesGUI.decrement();
				y += 1;
				updateRect();
				GameState1.moveEntities();
				flipped = !flipped;
				CountStep();
			}
		}

		for (Pill p : pills) {
			if (collider.intersects(p.getCollider()) && !p.isPillEaten() && !isAuraActive) {
				p.eatPill();
				LivesGUI.increment();
				isAuraActive = true;
			}
		}
		if (moveCount > MAX_MOVE) {
			isAuraActive = false;
			moveCount = 0;
		}

	}

	public void resetPlayer() {
		isAuraActive = false;
		flipped = false;
		moveCount = 0;
		x = (int) map.getPlayerStart().x / tileSize;
		y = (int) map.getPlayerStart().y / tileSize;
		collider.setLocation(x * tileSize, y * tileSize);
	}

	private void updateRect() {
		collider.setLocation(x * tileSize, y * tileSize);
	}

	public Vector2f getTilePosition() {
		return new Vector2f(x, y);
	}

	public Vector2f getPixelPosition() {
		return new Vector2f(x * tileSize, y * tileSize);
	}

	public Rectangle getCollider() {
		return collider;
	}

	public int getWidth() {
		// return the image width
		return player1.getWidth();
	}

	public int getHeight() {
		return player2.getHeight();
	}

	private void CountStep() {
		if (isAuraActive) {
			++moveCount;
		}
	}
	
	public boolean auraState() {
		return isAuraActive;
	}
	public boolean isFlippedSprite(){ 
		return flipped;
	}
}
