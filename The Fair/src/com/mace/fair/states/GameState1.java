package com.mace.fair.states;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.mace.fair.camera.Camera;
import com.mace.fair.constants.Constants;
import com.mace.fair.entities.Zombie;
import com.mace.fair.gui.LivesGUI;
import com.mace.fair.map.Map;
import com.mace.fair.pills.Pill;
import com.mace.fair.entities.Player;

public class GameState1 extends BasicGameState {

	private final int MAX_ZOMBIES;
	private final int MAX_PILLS;
	private int stateID;
	private int mouseX, mouseY;
	private Map map;
	private Player player;
	private Camera camera;
	private ArrayList<Zombie> zombies;
	private ArrayList<Pill> pills;
	private static boolean updatingZombie = false;
	private boolean isGameActive = true; // For if they hit escape
	private Image menu, resume, overlay; // TODO implement
	private Rectangle resumeButton, menuButton;
	private float buttonX, buttonY;
	private boolean gameWon, cheatFrozen, cheatExterminate;

	public GameState1(int stateID) {
		this.stateID = stateID;
		MAX_ZOMBIES = Constants.MAX_ZOMBIES;
		MAX_PILLS = Constants.MAX_PILLS;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		gameWon = false;
		cheatFrozen = false;
		cheatExterminate = false;

		LivesGUI.init();

		if (map == null) {
			map = new Map();
			map.init(gc, sbg);
		}else{ 
			resetState();
		}
		if (player == null) {
			player = new Player(map);
			player.init(gc, sbg);
		} else {
			player.resetPlayer();
		}

		// Zombie initialisation
		if (zombies == null) {
			zombies = new ArrayList<Zombie>();
			for (int i = 0; i < MAX_ZOMBIES; ++i) {
				zombies.add(new Zombie(map, i));
				zombies.get(i).init(gc, sbg);
			}
		} else {
			for(Zombie z : zombies){ 
				z.resetZombie();
			}
		}
		// Pill initialisation
		if (pills == null) {
			pills = new ArrayList<Pill>();
			for (int i = 0; i < MAX_PILLS; ++i) {
				pills.add(new Pill(map));
				pills.get(i).init(gc);
			}
		} else {
			for (Pill p : pills) {
				p.resetPill();
			}
			
		}

		if (camera == null)
			camera = new Camera(map.getPixelWidth(), map.getPixelHeight());

		if (menu == null)
			menu = new Image(Constants.menuButton_img_loc);

		if (resume == null)
			resume = new Image(Constants.resumeButton_img_loc);

		if (overlay == null)
			overlay = new Image(Constants.overlay_img_loc);

		buttonX = resume.getWidth() / 0.9f;
		buttonY = resume.getHeight() / 1.35f;
		if (resumeButton == null)
			resumeButton = new Rectangle(buttonX, buttonY, resume.getWidth(), resume.getHeight());
		if (menuButton == null)
			menuButton = new Rectangle(buttonX, buttonY, resume.getWidth(), resume.getHeight());

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		camera.translate(g, player);

		map.render(gc, sbg, g);
		for (Zombie z : zombies) {
			if (z.isZombieAlive()) {
				z.render(gc, sbg, g);
			}
		}
		for (Pill p : pills) {
			p.render(g);
		}
		player.render(gc, sbg, g);
		LivesGUI.render(g, player, camera);

		if (!isGameActive) {
			int localX = (int) camera.getTranslation().x;
			int localY = (int) camera.getTranslation().y;

			resumeButton.setLocation(localX + buttonX, localY + buttonY);
			menuButton.setLocation(localX + buttonX, localY + (buttonY * 2.4f));

			g.drawImage(overlay, camera.getTranslation().x, camera.getTranslation().y);
			g.drawImage(resume, localX + buttonX, localY + buttonY);
			g.drawImage(menu, (localX + buttonX), localY + buttonY * 2.4f);

		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY(); // Convert the mouse
														// co-ordinates
		if (input.isKeyPressed(Input.KEY_P) || input.isKeyPressed(Input.KEY_ESCAPE)) {
			isGameActive = !isGameActive;
			if (isGameActive)
				input.clearKeyPressedRecord();
		}
		if (input.isKeyPressed(Input.KEY_F))
			cheatFrozen = !cheatFrozen;
		if (input.isKeyPressed(Input.KEY_X))
			cheatExterminate = !cheatExterminate;

		if (isGameActive) {
			checkWinState();
			player.update(gc, sbg, delta, pills);
			if (updatingZombie) {
				for (Zombie z : zombies) {
					if (z.isZombieAlive()) {
						z.update(gc, sbg, delta, player, cheatFrozen, cheatExterminate);
						z.isFleeing(player.auraState());
					}
				}
				updatingZombie = !updatingZombie;
			}

		} else {
			int localX = (int) camera.getTranslation().x;
			int localY = (int) camera.getTranslation().y;
			if (resumeButton.contains(mouseX + localX, mouseY + localY)) {

				if (input.isMousePressed(0)) {
					input.clearKeyPressedRecord();
					isGameActive = true;
				}
			}
			if (menuButton.contains(mouseX + localX, mouseY + localY)) {
				if (input.isMousePressed(0))
					sbg.enterState(Constants.MenuState);
			}
		}

		if (LivesGUI.isPlayerDead() || gameWon) {
			// LivesGUI.resetLives();
			sbg.enterState(0);
		}

	}

	public static void moveEntities() {
		updatingZombie = true;
	}

	private void checkWinState() {
		int pillCounter = 0;
		int zombieCounter = 0;
		// for Pill p in the list pills
		for (Pill p : pills) {
			if (p.isPillEaten())
				++pillCounter;
		}

		for (Zombie z : zombies) {
			if (!z.isZombieAlive())
				++zombieCounter;
		}
		if (zombieCounter == MAX_ZOMBIES && pillCounter == Constants.MAX_PILLS) {
			// LivesGUI.resetLives();
			gameWon = true;
		}

	}

	public void resetState() {

		LivesGUI.resetLives();
		cheatFrozen = false; 
		cheatExterminate = false;
		isGameActive = true;
	}

	public int getID() {
		return stateID;
	}

}
