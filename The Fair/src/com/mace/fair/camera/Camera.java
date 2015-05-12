package com.mace.fair.camera;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.mace.fair.entities.Player;

public class Camera {

	private int x, y; //The X & Y position of the viewport
	private int mapWidth, mapHeight; //The width & height of the map
	private Rectangle viewPort; //viewport rectangle

	public Camera(int mapWidth, int mapHeight) {
		x = 0; 
		y = 0;
		//initialise viewport at 0,0 with dimensions of the screen
		viewPort = new Rectangle(0, 0, Display.getWidth(), Display.getHeight()); 
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

	}

	public void translate(Graphics g, Player player) {
		//if the players x position (in the world) minus the screen size over 2 + half the size of the player
		//is lees than 0, set the viewport x to 0
		
		if (player.getPixelPosition().x - Display.getWidth() / 2 + player.getWidth() / 2 < 0) {
			x = 0;
		} else if (player.getPixelPosition().x + Display.getWidth() / 2 + player.getWidth() / 2 > mapWidth) {
			x = -mapWidth + Display.getWidth();
		} else {
			x = (int) -player.getPixelPosition().x + Display.getWidth() / 2 - player.getWidth() / 2;
		}

		if (player.getPixelPosition().y - Display.getHeight() / 2 + player.getWidth() / 2 < 0) {
			y = 0;
		} else if (player.getPixelPosition().y + Display.getHeight() / 2 + player.getHeight() / 2 > mapHeight) {
			y = -mapHeight + Display.getHeight();
		} else {
			y = (int) -player.getPixelPosition().y + Display.getHeight() / 2 - player.getHeight() / 2;
		}
		
		g.translate(x, y);
		viewPort.setX(-x);
		viewPort.setY(-y);
	}

	public Vector2f getTranslation() {
		return new Vector2f(viewPort.getX(), viewPort.getY());
	}

}