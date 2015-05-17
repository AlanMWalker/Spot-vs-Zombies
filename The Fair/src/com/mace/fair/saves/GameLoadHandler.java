package com.mace.fair.saves;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.mace.fair.constants.Constants;
import com.mace.fair.entities.Player;
import com.mace.fair.entities.Zombie;
import com.mace.fair.gui.LivesGUI;
import com.mace.fair.map.Map;
import com.mace.fair.pills.Pill;
import com.mace.fair.states.GameState1;

public class GameLoadHandler {
	/***********************************************************
	 * Will handle loading all the games data from a save file.*
	 ***********************************************************/
	private ArrayList<Zombie> zombies;
	private ArrayList<Pill> pills;
	private GameState1 playState;
	private Scanner loadData;
	private Player player;
	private Map map;

	public GameLoadHandler(Player player, Map map, ArrayList<Zombie> zombies, ArrayList<Pill> pills, GameState1 playState) {
		this.player = player;
		this.map = map;
		this.zombies = zombies;
		this.pills = pills;
		this.playState = playState;

	}

	public void loadGame() {
		try {
			loadData = new Scanner(new File("spot_save_game.scr"));
			while (loadData.hasNext()) {
				boolean active = Boolean.parseBoolean(loadData.next());
				boolean frozen = Boolean.parseBoolean(loadData.next());
				boolean exterminate = Boolean.parseBoolean(loadData.next());
				playState.loadData(active, frozen, exterminate);
				loadPlayer();
				loadZombies();
				loadPills();
				loadMap();
			}
		} catch (Exception e) {
		}

	}

	private void loadPlayer() {
		int x, y, lives;
		boolean flipped, aura;
		String temp;
		temp = loadData.next();
		x = Integer.parseInt(temp);
		temp = loadData.next();
		y = Integer.parseInt(temp);
		temp = loadData.next();
		aura = Boolean.parseBoolean(temp);
		temp = loadData.next();
		flipped = Boolean.parseBoolean(temp);
		temp = loadData.next();
		lives = Integer.parseInt(temp);
		player.loadPlayerData(x, y, flipped, aura);
		LivesGUI.setLives(lives);
	}

	private void loadZombies() {
		int x, y;
		boolean flipped, running, alive;
		// String temp;
		for (int i = 0; i < Constants.MAX_ZOMBIES; ++i) {
			x = Integer.parseInt(loadData.next());
			y = Integer.parseInt(loadData.next());
			running = Boolean.parseBoolean(loadData.next());
			flipped = Boolean.parseBoolean(loadData.next());
			alive = Boolean.parseBoolean(loadData.next());

			zombies.get(i).loadZombieData(x, y, running, flipped, alive);
		}

	}

	private void loadPills() {
		int x, y;
		boolean eaten;
		for (int i = 0; i < Constants.MAX_PILLS; ++i) {
			x = Integer.parseInt(loadData.next());
			y = Integer.parseInt(loadData.next());
			eaten = Boolean.parseBoolean(loadData.next());
			pills.get(i).loadData(x, y, eaten);
		}
	}

	private void loadMap() {
		int x, y;
		for (int i = 0; i < Constants.MAX_HOLES; ++i) {
			x = Integer.parseInt(loadData.next());
			y = Integer.parseInt(loadData.next());
			map.setHolePosition(x, y);
		}
	}
}
