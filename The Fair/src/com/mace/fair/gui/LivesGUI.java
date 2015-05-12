package com.mace.fair.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.mace.fair.camera.Camera;
import com.mace.fair.constants.Constants;
import com.mace.fair.entities.Player;

public class LivesGUI {
	/*
	 * Variables declared as static for 1 copy that exists without an object
	 */
	private static int lives;; // number of lives

	/*
	 * TODO - Fix the 'moving' player bar at x co-ordinates
	 */
	public static void init() throws SlickException {
		lives = Constants.START_LIVES;
	}

	public static void render(Graphics g, Player p, Camera c) throws SlickException {
		g.drawString("Lives " + lives, p.getPixelPosition().x, p.getPixelPosition().y - p.getHeight() / 4);
	}

	public static void increment() {
		++lives;
	}

	public static void decrement() {
		--lives;
	}

	public static boolean isPlayerDead() {
		// If lives == 0 then return true, else return false
		return (lives <= 0);
	}

	public static void resetLives() {
		lives = Constants.START_LIVES;
	}
}
