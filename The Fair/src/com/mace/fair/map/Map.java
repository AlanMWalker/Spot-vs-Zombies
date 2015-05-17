package com.mace.fair.map;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import com.mace.fair.constants.Constants;

public class Map {

	private final int TILESIZE;
	private final int HOLE_TILE;
	private final int MAX_HOLES;
	private int oldX[], oldY[];
	private TiledMap map;
	private Random rnd;
	private boolean hasBeenPlaced;

	public Map() {
		TILESIZE = Constants.TILESIZE;
		HOLE_TILE = Constants.HOLE_TILE_ID;
		MAX_HOLES = Constants.MAX_HOLES;
		oldX = new int[Constants.MAX_HOLES];
		oldY = new int[Constants.MAX_HOLES];
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		hasBeenPlaced = false;
		rnd = new Random(System.currentTimeMillis());
		map = new TiledMap(Constants.temp_map_1);
		System.out.println(map.getTileId(1, 2, 0));
		placeHolesOnMap();
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		map.render(0, 0);
	}

	public void placeHolesOnMap() {
		int x, y;
		if (!hasBeenPlaced) {
			for (int i = 0; i < 12; ++i) {

				x = rnd.nextInt(map.getWidth());
				y = rnd.nextInt(map.getHeight());
				oldX[i] = x;
				oldY[i] = y;
				map.getTileId(x, y, 0);

				while (map.getTileId(x, y, 0) == Constants.WALL_TILE_ID || map.getTileId(x, y, 0) == Constants.HOLE_TILE_ID) {
					x = rnd.nextInt(map.getWidth());
					y = rnd.nextInt(map.getHeight());
					map.getTileId(x, y, 0);
				}
				map.setTileId(x, y, 0, HOLE_TILE);
			}
			hasBeenPlaced = true;
		} else {

			try {
				map = new TiledMap(Constants.temp_map_1);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 12; ++i) {

				x = rnd.nextInt(map.getWidth());
				y = rnd.nextInt(map.getHeight());
				map.getTileId(x, y, 0);

				while (map.getTileId(x, y, 0) == Constants.WALL_TILE_ID || map.getTileId(x, y, 0) == Constants.HOLE_TILE_ID && isOldPosition(x, y)) {
					x = rnd.nextInt(map.getWidth());
					y = rnd.nextInt(map.getHeight());
					map.getTileId(x, y, 0);
				}
				map.setTileId(x, y, 0, HOLE_TILE);
			}

		}

	}

	private boolean isOldPosition(int x, int y) {

		for (int i = 0; i < MAX_HOLES; ++i) {
			if (x == oldX[i] && y == oldY[i])
				return true;
		}
		return false;
	}

	public int getWidth() {
		return map.getWidth();
	}

	public int getHeight() {
		return map.getHeight();
	}

	public int getPixelWidth() {
		return map.getWidth() * TILESIZE;
	}

	public int getPixelHeight() {
		return map.getHeight() * TILESIZE;
	}

	public Vector2f getPlayerStart() {
		return new Vector2f(map.getObjectX(0, 0), map.getObjectY(0, 0));

	}

	public Vector2f getZombieStart(int index) {
		return new Vector2f(map.getObjectX(1, index), map.getObjectY(1, index));
	}

	public String getTileProperty(int x, int y) {
		int tileID = map.getTileId(x, y, 0);
		String tileProperty = "walkable";

		if ("true".equals(map.getTileProperty(tileID, "blocked", "false"))) {
			tileProperty = "blocked";
		} else {
			if ("true".equals(map.getTileProperty(tileID, "falling", "false"))) {
				tileProperty = "fallable";
			}
		}
		return tileProperty;
	}

	public String getTileProperty(float x, float y) {
		int tileID = map.getTileId((int) x, (int) y, 0);
		String tileProperty = "walkable";

		if ("true".equals(map.getTileProperty(tileID, "blocked", "false"))) {
			tileProperty = "blocked";
		} else {
			if ("true".equals(map.getTileProperty(tileID, "falling", "false"))) {
				tileProperty = "fallable";
			}
		}

		return tileProperty;
	}
}
