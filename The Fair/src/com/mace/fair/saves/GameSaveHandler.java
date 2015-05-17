package com.mace.fair.saves;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

import com.mace.fair.entities.Player;
import com.mace.fair.entities.Zombie;
import com.mace.fair.gui.LivesGUI;
import com.mace.fair.map.Map;
import com.mace.fair.pills.Pill;

public class GameSaveHandler {
	private Formatter save_file;
	private Player player;
	private ArrayList<Zombie> zombies;
	private ArrayList<Pill> pills;
	private Map map;

	public GameSaveHandler(Player player, Map map, ArrayList<Zombie> zombies, ArrayList<Pill> pills) {
		this.player = player;
		this.map = map;
		this.zombies = zombies;
		this.pills = pills;
	}

	public void saveGame() throws IOException {
		/*
		 * Save order = Player -> Zombies -> Pills -> Holes (map)
		 */
		try {
			save_file = new Formatter("spot_save_game.scr");
			savePlayer();
			saveZombies();
			savePills();
			saveMap();

		} finally {
			if (save_file != null)
				save_file.close();
		}
	}

	private void savePlayer() throws IOException {
		save_file.format("%d\n%d", (int) player.getTilePosition().x, (int)player.getTilePosition().y); //Saving player position
		save_file.format("\n%s\n%s", Boolean.toString(player.auraState()), Boolean.toString(player.isFlippedSprite())); //Saving sprite data
		save_file.format("\n%d", LivesGUI.getLives());
	}

	private void saveZombies() {

	}

	private void savePills() {

	}

	private void saveMap() {

	}
}
