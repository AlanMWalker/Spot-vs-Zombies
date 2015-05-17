package com.mace.fair.saves;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
			}
		} catch (Exception e) {
			System.out.println("No loadable file!");
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
		System.out.println("X = " + x + " Y = " + y);
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
		boolean flipped, running;
		String temp;

	}

	private void loadPills() {

	}

	private void loadMap() {

	}
}
