package com.mace.fair.saves;

import java.util.ArrayList;
import java.util.Scanner;

import com.mace.fair.entities.Player;
import com.mace.fair.entities.Zombie;
import com.mace.fair.map.Map;
import com.mace.fair.pills.Pill;

public class GameLoadHandler {
	/***********************************************************
	 * Will handle loading all the games data from a save file.*
	 ***********************************************************/
	private Scanner readSaveData;
	private Player player;
	private Map map;
	private ArrayList<Zombie> zombies;
	private ArrayList<Pill> pills;

	public GameLoadHandler(Player player, Map map, ArrayList<Zombie> zombies, ArrayList<Pill> pills) {
		this.player = player;
		this.map = map;
		this.zombies = zombies;
		this.pills = pills;
	}

	public void loadGame() {

	}
}
