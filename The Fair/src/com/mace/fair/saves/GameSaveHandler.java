package com.mace.fair.saves;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

import com.mace.fair.constants.Constants;
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

	public void saveGame(boolean updatingZombies) throws IOException {
		/*
		 * Save order = Player -> Zombies -> Pills -> Holes (map)
		 */
		try {
			save_file = new Formatter("spot_save_game.scr");

			String updating = Boolean.toString(updatingZombies);
			save_file.format("%s\n\n", updating);
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
		save_file.format("%d\n%d", (int) player.getTilePosition().x, (int) player.getTilePosition().y); // Saving
																										// player
																										// position
		save_file.format("\n%s\n%s", Boolean.toString(player.auraState()), Boolean.toString(player.isFlippedSprite())); // Saving
																														// sprite
																														// data
		save_file.format("\n%d\n\n", LivesGUI.getLives());
	}

	private void saveZombies() {

		for (int i = 0; i < Constants.MAX_ZOMBIES; ++i) {
			int x = (int) zombies.get(i).getTilePosition().x;
			int y = (int) zombies.get(i).getTilePosition().y;
			String isRunning, isFlipped, isAlive;

			isRunning = Boolean.toString(player.auraState());
			isFlipped = Boolean.toString(zombies.get(i).isSpriteFlipped());
			isAlive = Boolean.toString(zombies.get(i).isZombieAlive());

			save_file.format("%d\n%d\n", x, y); // Save zombie posititions
			// Save zombie sprite details
			save_file.format("%s\n%s\n%s\n", isRunning, isFlipped, isAlive);
		}
		save_file.format("\n\n", (Object) null);
	}

	private void savePills() {
		for (int i = 0; i < Constants.MAX_PILLS; ++i) {
			int x = (int) pills.get(i).location().x;
			int y = (int) pills.get(i).location().y;
			String isEaten = Boolean.toString(pills.get(i).isPillEaten());
			save_file.format("%d\n%d\n%s\n", x, y, isEaten);

		}
		save_file.format("\n\n", (Object) null);
	}

	private void saveMap() {
		/*
		 * Save any hole map locations to the game save file
		 */
		for (int i = 0; i < map.getWidth(); ++i) {
			for (int j = 0; j < map.getHeight(); ++j) {
				if (map.getTileProperty(i, j).equals("fallable")) {
					save_file.format("%d\n%d\n", i, j);
				}
			}
		}
	}
}
